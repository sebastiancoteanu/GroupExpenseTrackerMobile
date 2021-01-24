package com.example.groupexpensetrackermobile.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.adapters.SelectedTripUserAdapter;
import com.example.groupexpensetrackermobile.adapters.TripAdapter;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.customcomponents.MultiSpinnerSearch;
import com.example.groupexpensetrackermobile.entities.SelectableUser;
import com.example.groupexpensetrackermobile.entities.Trip;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.listeners.MultiSpinnerListener;
import com.example.groupexpensetrackermobile.services.RequestService;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;
import com.example.groupexpensetrackermobile.utilities.ToastHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class CreateTrip extends AppCompatActivity {

    private SelectedTripUserAdapter selectedUsersAdapter;
    private RecyclerView selectedUsersRecycleView;
    private List<SelectableUser> selectedUsers = new ArrayList<>();
    private List<SelectableUser> selectableUsers = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create trip");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MultiSpinnerSearch multiSelectSpinnerWithSearch = findViewById(R.id.multipleItemSelectionSpinner);
        multiSelectSpinnerWithSearch.setSpinnerText("Add users...");
        multiSelectSpinnerWithSearch.setSearchHint("Type at least 3 characters...");

        disableInputMethods();

        Response.Listener<JSONArray> responseListener = response -> {
            if(response != null) {
                System.out.println("Selectable users fetched successful. -> " + response.length() + " users");
            }
            selectableUsers = parseSelectableUsers(response);

            multiSelectSpinnerWithSearch.setItems(selectableUsers, new MultiSpinnerListener() {
                @SuppressLint("NewApi")
                @Override
                public void onSelectionEnd() {
                    selectedUsers = selectableUsers.stream().filter(SelectableUser::isSelected).collect(Collectors.toList());
                    selectedUsersAdapter.setUserList(selectedUsers);
                    selectedUsersAdapter.notifyDataSetChanged();
                }
            });

            selectedUsers = selectableUsers.stream().filter(SelectableUser::isSelected).collect(Collectors.toList());
            selectedUsersAdapter = new SelectedTripUserAdapter(selectedUsers, this);
            selectedUsersRecycleView = findViewById(R.id.selectedUsersRecycleView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            selectedUsersRecycleView.setLayoutManager(layoutManager);
            selectedUsersRecycleView.setAdapter(selectedUsersAdapter);
            enableInputMethods();
        };

        Response.ErrorListener errorListener =  error -> {
            error.printStackTrace();
            enableInputMethods();
        };

        JsonArrayRequest jsonArrayRequest = HttpUtils.getInstance().getCustomJsonArrayRequest(
                Request.Method.GET,
                Constants.API_URL + "custom/candidates/" + CredentialManager.getInstance().getCurrentUser().getAppUserId(),
                null,
                responseListener,
                errorListener,
                CredentialManager.getInstance().getCurrentToken()
        );

        RequestService.getInstance().addRequest(jsonArrayRequest);
    }

    private List<SelectableUser> parseSelectableUsers(JSONArray jsonArray) {
        List<SelectableUser> selectableUsers = new ArrayList<>();

        if(jsonArray == null) {
            return selectableUsers;
        }

        try {
            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject == null) {
                    continue;
                }

                long appUserId = jsonObject.getLong("appUserId");
                selectableUsers.add(new SelectableUser(
                        jsonObject.getLong("id"),
                        appUserId,
                        jsonObject.getString("login"),
                        jsonObject.getString("firstName"),
                        jsonObject.getString("lastName"),
                        "",
                        appUserId == CredentialManager.getInstance().getCurrentUser().getAppUserId()));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return selectableUsers;
        }

        return selectableUsers;
    }

    private void disableInputMethods() {
        TextView titleTV = findViewById(R.id.titleEditText);
        TextView descriptionTV = findViewById(R.id.descriptionEditText);
        Button b = findViewById(R.id.createTripButton);

        titleTV.setEnabled(false);
        descriptionTV.setEnabled(false);
        b.setEnabled(false);
    }

    private void enableInputMethods() {
        TextView titleTV = findViewById(R.id.titleEditText);
        TextView descriptionTV = findViewById(R.id.descriptionEditText);
        Button b = findViewById(R.id.createTripButton);

        titleTV.setEnabled(true);
        descriptionTV.setEnabled(true);
        b.setEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("NewApi")
    public void onCreateButtonClicked(View v) {
        TextView titleTV = findViewById(R.id.titleEditText);
        TextView descriptionTV = findViewById(R.id.descriptionEditText);

        JSONObject postData = new JSONObject();

        disableInputMethods();

        long[] participantsAppUserId = selectedUsers.stream().mapToLong(User::getAppUserId).toArray();
        if(participantsAppUserId == null) {
            participantsAppUserId = new long[]{};
        }
        System.out.print("Users are: ");
        for(int i = 0; i < participantsAppUserId.length; i ++) {
            System.out.print(participantsAppUserId[i] + ", ");
        }
        System.out.println("");

        try {
            postData.put("title", titleTV.getText().toString());
            postData.put("description", descriptionTV.getText().toString());
            postData.put("createdBy", CredentialManager.getInstance().getCurrentUser().getAppUserId());
            postData.put("participantsAppUserId", JSONObject.wrap(participantsAppUserId));
            System.out.println(postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            ToastHelper.getInstance().getErrorMessageToast(v.getContext(), "Invalid values", Toast.LENGTH_SHORT).show();
            enableInputMethods();
        }

        Response.Listener<JSONObject> responseListener = response -> {
            enableInputMethods();
            ToastHelper.getInstance().getSuccessfulMessageToast(v.getContext(), "Trip created", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        };

        Response.ErrorListener errorListener = error -> {
            System.out.println(error.getMessage());
            error.printStackTrace();
            enableInputMethods();
            ToastHelper.getInstance().getErrorMessageToast(v.getContext(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        };

        JsonObjectRequest jsonObjectRequest = HttpUtils.getInstance().getCustomJsonObjectRequest(
                Request.Method.POST,
                Constants.API_URL + "custom/create-trip",
                postData,
                responseListener,
                errorListener,
                CredentialManager.getInstance().getCurrentToken()
        );

        RequestService.getInstance().addRequest(jsonObjectRequest);
    }
}