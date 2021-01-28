package com.example.groupexpensetrackermobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.adapters.TripAdapter;
import com.example.groupexpensetrackermobile.adapters.TripExpensesAdapter;
import com.example.groupexpensetrackermobile.adapters.TripMembersAdapter;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.entities.Expense;
import com.example.groupexpensetrackermobile.entities.ExpenseType;
import com.example.groupexpensetrackermobile.entities.Trip;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.services.RequestService;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripDetails extends AppCompatActivity {
    private TripMembersAdapter tripMembersAdapter;
    private RecyclerView tripMembersRecyclerView;
    private List<User> tripMembers = new ArrayList<>();

    private TripExpensesAdapter tripExpensesAdapter;
    private RecyclerView tripExpensesRecyclerView;
    private List<Expense> tripExpenses = new ArrayList<>();
    private ImageButton expensesToggle;
    private ImageButton membersToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trip details");
        setSupportActionBar(toolbar);

        expensesToggle = findViewById(R.id.expensesToggle);
        membersToggle = findViewById(R.id.membersToggle);

        expensesToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecyclerViewVisibility(tripExpensesRecyclerView, v, expensesToggle);
            }
        });

        membersToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecyclerViewVisibility(tripMembersRecyclerView, v, membersToggle);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initializeTripData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<User> parseTripMembers(JSONObject response) {
        List<User> userList = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray("tripParticipants");
            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject == null) {
                    continue;
                }
                userList.add(new User(
                        jsonObject.getLong("id"),
                        jsonObject.getLong("appUserId"),
                        jsonObject.getString("login"),
                        jsonObject.getString("firstName"),
                        jsonObject.getString("lastName")
                ));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return userList;
        }
        return userList;
    }

    public List<Expense> parseTripExpenses(JSONObject response) {
        List<Expense> tripExpenses = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray("tripExpenses");
            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject == null) {
                    continue;
                }
                tripExpenses.add(new Expense(
                    jsonObject.getLong("id"),
                    jsonObject.getLong("amount"),
                    jsonObject.getString("description"),
                    jsonObject.getLong("createdById"),
                    jsonObject.getLong("tripId"),
                    ExpenseType.valueOf(jsonObject.getString("type"))
                ));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return tripExpenses;
        }
        return tripExpenses;
    }

    public void initializeTripData() {
        tripMembersRecyclerView = findViewById(R.id.membersRecycler);
        LinearLayoutManager layoutManagerMembers = new LinearLayoutManager(this);
        tripMembersRecyclerView.setLayoutManager(layoutManagerMembers);

        tripExpensesRecyclerView = findViewById(R.id.expensesRecycler);
        LinearLayoutManager layoutManagerExpenses = new LinearLayoutManager(this);
        tripExpensesRecyclerView.setLayoutManager(layoutManagerExpenses);

        Response.Listener<JSONObject> responseListener = response -> {
            if(response != null) {
                System.out.println("User trips fetched successful. -> " + response.length() + " trips");
            }

            tripMembers = parseTripMembers(response);
            tripExpenses = parseTripExpenses(response);

            tripMembersAdapter = new TripMembersAdapter(tripMembers, this);
            tripMembersRecyclerView.setAdapter(tripMembersAdapter);

            tripExpensesAdapter = new TripExpensesAdapter(tripExpenses, this);
            tripExpensesRecyclerView.setAdapter(tripExpensesAdapter);
        };

        Response.ErrorListener errorListener =  error -> {
            error.printStackTrace();
        };

        String tripId = getIntent().getStringExtra("tripId");
        JsonObjectRequest jsonObjectRequest = HttpUtils.getInstance().getCustomJsonObjectRequest(
                Request.Method.GET,
                Constants.API_URL + "custom/trips/trip-details/" + tripId,
                null,
                responseListener,
                errorListener,
                CredentialManager.getInstance().getCurrentToken()
        );

        RequestService.getInstance().addRequest(jsonObjectRequest);
    }

    public List<Expense> createMockedListOfExpenses() {
        List<Expense> expensesList = new ArrayList<>();
        expensesList.add(new Expense(1, 25, "Lunch", 2, 4, ExpenseType.INDIVIDUAL));
        expensesList.add(new Expense(2, 122, "Brunch", 3, 2, ExpenseType.GROUP));
        expensesList.add(new Expense(3, 343, "Dinner", 1, 3, ExpenseType.INDIVIDUAL));
        expensesList.add(new Expense(4, 122, "Shopping", 4, 1, ExpenseType.GROUP));
        expensesList.add(new Expense(5, 222, "Lunch", 6, 4, ExpenseType.GROUP));
        expensesList.add(new Expense(6, 122, "Shopping", 3, 5, ExpenseType.INDIVIDUAL));
        expensesList.add(new Expense(7, 77, "Shopping", 5, 7, ExpenseType.INDIVIDUAL));
        expensesList.add(new Expense(8, 567, "Lunch", 2, 3, ExpenseType.INDIVIDUAL));
        return expensesList;
    }

    public void toggleRecyclerViewVisibility(RecyclerView recyclerView, View v, ImageButton buttonToggle) {
        boolean isVisible = recyclerView.getVisibility() == View.VISIBLE;
        buttonToggle.setImageResource(isVisible ? R.drawable.ic_baseline_expand_more_24 : R.drawable.ic_baseline_expand_less_24);
        recyclerView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }
}
