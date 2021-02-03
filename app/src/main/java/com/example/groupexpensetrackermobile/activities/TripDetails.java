package com.example.groupexpensetrackermobile.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.adapters.AddUsersAdapter;
import com.example.groupexpensetrackermobile.adapters.TripExpensesAdapter;
import com.example.groupexpensetrackermobile.adapters.TripMembersAdapter;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.customcomponents.MultiSpinnerSearch;
import com.example.groupexpensetrackermobile.entities.Expense;
import com.example.groupexpensetrackermobile.entities.ExpenseType;
import com.example.groupexpensetrackermobile.entities.SelectableUser;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.listeners.MultiSpinnerListener;
import com.example.groupexpensetrackermobile.listeners.OnItemClickListener;
import com.example.groupexpensetrackermobile.services.RequestService;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;
import com.example.groupexpensetrackermobile.utilities.ToastHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TripDetails extends AppCompatActivity {
    private TripMembersAdapter tripMembersAdapter;
    private RecyclerView tripMembersRecyclerView;
    private List<SelectableUser> tripMembers = new ArrayList<>();
    private List<SelectableUser> tripMembersCandidates = new ArrayList<>();

    private TripExpensesAdapter tripExpensesAdapter;
    private RecyclerView tripExpensesRecyclerView;
    private List<Expense> tripExpenses = new ArrayList<>();
    private ImageButton expensesToggle;
    private ImageButton membersToggle;

    public static AlertDialog.Builder builder;
    public static AlertDialog ad;
    private AddUsersAdapter selectExpenseParticipantsAdapter;
    private List<SelectableUser> expensePartcipants;
    private RecyclerView expenseParticipantsRecycleView;

    private boolean isExpenseEditMode = false;

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
        initializeUserDialog();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<SelectableUser> parseExpenseMembers(JSONObject response) {
        List<SelectableUser> userList = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray("expenseParticipants");
            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject == null) {
                    continue;
                }
                User user = new User(
                        jsonObject.getLong("id"),
                        jsonObject.getLong("appUserId"),
                        jsonObject.getString("login"),
                        jsonObject.getString("firstName"),
                        jsonObject.getString("lastName"),
                        ""
                );
                SelectableUser su = new SelectableUser(user);
                su.setSelected(true);
                userList.add(su);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return userList;
        }
        return userList;
    }

    public List<SelectableUser> parseTripMembers(JSONObject response) {
        List<SelectableUser> userList = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray("tripParticipants");
            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject == null) {
                    continue;
                }
                User user = new User(
                        jsonObject.getLong("id"),
                        jsonObject.getLong("appUserId"),
                        jsonObject.getString("login"),
                        jsonObject.getString("firstName"),
                        jsonObject.getString("lastName"),
                        ""
                );
                user.setBalance(BigDecimal.valueOf(jsonObject.getDouble("balanceForTrip")));
                SelectableUser su = new SelectableUser(user);
                if(su.getAppUserId() == CredentialManager.getInstance().getCurrentUser().getAppUserId()) {
                    su.setSelected(true);
                }
                userList.add(su);
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

    @SuppressLint("NewApi")
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

            tripMembersAdapter = new TripMembersAdapter(tripMembers.stream().map(p -> (User) p).collect(Collectors.toList()), this);
            tripMembersRecyclerView.setAdapter(tripMembersAdapter);

            tripExpensesAdapter = new TripExpensesAdapter(tripExpenses, this, new OnItemClickListener() {
                @Override
                public boolean onItemLongClick(long itemId) {
                    openExpenseDetailsDialog(itemId);
                    return true;
                }
            });
            tripExpensesRecyclerView.setAdapter(tripExpensesAdapter);

            TextView tripNameTV = findViewById(R.id.tripDetails_tripName);
            try {
                tripNameTV.setText(response.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView tripDetailsTV = findViewById(R.id.tripDetails_tripDescription);
            try {
                tripDetailsTV.setText(response.getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

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

    public void toggleRecyclerViewVisibility(RecyclerView recyclerView, View v, ImageButton buttonToggle) {
        boolean isVisible = recyclerView.getVisibility() == View.VISIBLE;
        buttonToggle.setImageResource(isVisible ? R.drawable.ic_baseline_expand_more_24 : R.drawable.ic_baseline_expand_less_24);
        recyclerView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    @SuppressLint("NewApi")
    public void openExpenseDetailsDialog(long expenseId) {

        Response.Listener<JSONObject> responseListener = response -> {
            if(response != null) {
                System.out.println("Expense details. -> " + response);
            }

            List<SelectableUser> currentExpensePartcipants = parseExpenseMembers(response);

            Expense expense = new Expense();
            expense.setId(expenseId);
            try {
                expense.setDescription(response.getString("description"));
                expense.setAmount((long) response.getDouble("amount"));
                String respType = response.getString("expenseType");
                if(respType != null) {
                    expense.setType(respType.equals("INDIVIDUAL") ? ExpenseType.INDIVIDUAL : ExpenseType.GROUP);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.add_expense, null);

            builder = new AlertDialog.Builder(this);
            builder.setView(popupView);

            TextView nameTv = popupView.findViewById(R.id.addExpense_expenseNameEdit);
            nameTv.setText(expense.getDescription());
            TextView amountTv = popupView.findViewById(R.id.addExpense_expenseAmountEdit);
            amountTv.setText(String.valueOf(Long.valueOf(expense.getAmount())));
            RadioButton groupRadioButton = popupView.findViewById(R.id.addExpense_groupRadio);
            RadioButton individualRadioButton = popupView.findViewById(R.id.addExpense_individualRadio);
            int recycleViewVisibility = View.VISIBLE;
            if(expense.getType() == ExpenseType.GROUP) {
                groupRadioButton.setChecked(true);
            } else {
                individualRadioButton.setChecked(true);
                recycleViewVisibility = View.INVISIBLE;
            }

            Runnable disableInput = new Runnable() {
                @Override
                public void run() {
                    groupRadioButton.setEnabled(false);
                    individualRadioButton.setEnabled(false);
                    nameTv.setEnabled(false);
                    amountTv.setEnabled(false);
                    RadioGroup radioGroup = popupView.findViewById(R.id.addExpense_expenseType);
                    radioGroup.setEnabled(false);
                    popupView.findViewById(R.id.addExpense_expenseParticipants).setEnabled(false);
                    ad.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                }
            };

            Runnable enableInput = new Runnable() {
                @Override
                public void run() {
                    groupRadioButton.setEnabled(true);
                    individualRadioButton.setEnabled(true);
                    nameTv.setEnabled(true);
                    amountTv.setEnabled(true);
                    RadioGroup radioGroup = popupView.findViewById(R.id.addExpense_expenseType);
                    radioGroup.setEnabled(true);
                    popupView.findViewById(R.id.addExpense_expenseParticipants).setEnabled(true);
                    ad.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                }
            };

            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                String expenseName = nameTv.getText().toString();
                double amount = Double.parseDouble(amountTv.getText().toString());

                int type = 0;
                if(groupRadioButton.isChecked()) {
                    type = 1;
                }

                disableInput.run();
                addExpense_updateExpense(expenseId, expenseName, amount, type);
            });

            builder.setNeutralButton("Edit", (dialog, which) -> {
                isExpenseEditMode = !isExpenseEditMode;
                if(isExpenseEditMode) {
                    enableInput.run();
                } else {
                    disableInput.run();
                }
            });



            ad = builder.show();
            disableInput.run();

            Button positiveButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setBackgroundColor(Color.WHITE);
            positiveButton.setTextColor(Color.rgb(1, 87, 155));
            positiveButton.setGravity(Gravity.END);

            Button neutralButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
            neutralButton.setBackgroundColor(Color.WHITE);
            neutralButton.setTextColor(Color.rgb(1, 87, 155));
            neutralButton.setGravity(Gravity.START);
            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isExpenseEditMode = !isExpenseEditMode;
                    if(isExpenseEditMode) {
                        enableInput.run();
                    } else {
                        disableInput.run();
                    }
                }
            });

            expensePartcipants = tripMembers.stream().map(SelectableUser::new).collect(Collectors.toList());
            for(SelectableUser selectableUser : expensePartcipants) {
                if(currentExpensePartcipants.contains(selectableUser)) {
                    selectableUser.setSelected(true);
                }
            }
            selectExpenseParticipantsAdapter = new AddUsersAdapter(expensePartcipants, popupView.getContext(), false);
            expenseParticipantsRecycleView = popupView.findViewById(R.id.addExpense_expenseParticipants);
            expenseParticipantsRecycleView.setVisibility(recycleViewVisibility);
            LinearLayoutManager layoutManager = new LinearLayoutManager(popupView.getContext());
            expenseParticipantsRecycleView.setLayoutManager(layoutManager);
            expenseParticipantsRecycleView.setAdapter(selectExpenseParticipantsAdapter);

            RadioGroup radioGroup = popupView.findViewById(R.id.addExpense_expenseType);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.addExpense_groupRadio) {
                        expenseParticipantsRecycleView.setVisibility(View.VISIBLE);
                    } else {
                        expenseParticipantsRecycleView.setVisibility(View.INVISIBLE);
                        for(SelectableUser selectableUser : expensePartcipants) {
                            if(!selectableUser.getLogin().equals(CredentialManager.getInstance().getCurrentUser().getLogin())) {
                                selectableUser.setSelected(false);
                            }
                            selectExpenseParticipantsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        };

        Response.ErrorListener errorListener = Throwable::printStackTrace;
        JsonObjectRequest jsonObjectRequest = HttpUtils.getInstance().getCustomJsonObjectRequest(
                Request.Method.GET,
                Constants.API_URL + "custom/expense/expense-details/" + expenseId,
                null,
                responseListener,
                errorListener,
                CredentialManager.getInstance().getCurrentToken()
        );
        RequestService.getInstance().addRequest(jsonObjectRequest);

    }

    @SuppressLint("NewApi")
    public void openCreateExpenseDialog(View v) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_expense, null);

        builder = new AlertDialog.Builder(v.getContext());
        builder.setView(popupView);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            TextView nameTv = popupView.findViewById(R.id.addExpense_expenseNameEdit);
            String expenseName = nameTv.getText().toString();

            TextView amountTv = popupView.findViewById(R.id.addExpense_expenseAmountEdit);
            double amount = Double.parseDouble(amountTv.getText().toString());

            RadioButton groupRadioButton = popupView.findViewById(R.id.addExpense_groupRadio);
            int type = 0;
            if(groupRadioButton.isChecked()) {
                type = 1;
            }

            addExpense_createExpense(expenseName, amount, type);

        });

        ad = builder.show();

        Button positiveButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setBackgroundColor(Color.WHITE);
        positiveButton.setTextColor(Color.rgb(1, 87, 155));
        positiveButton.setGravity(Gravity.END);

        expensePartcipants = tripMembers.stream().map(SelectableUser::new).collect(Collectors.toList());
        for(SelectableUser selectableUser : expensePartcipants) {
            if(selectableUser.getAppUserId() == CredentialManager.getInstance().getCurrentUser().getAppUserId()) {
                selectableUser.setSelected(true);
            }
        }
        selectExpenseParticipantsAdapter = new AddUsersAdapter(expensePartcipants, popupView.getContext(), false);
        expenseParticipantsRecycleView = popupView.findViewById(R.id.addExpense_expenseParticipants);
        expenseParticipantsRecycleView.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(popupView.getContext());
        expenseParticipantsRecycleView.setLayoutManager(layoutManager);
        expenseParticipantsRecycleView.setAdapter(selectExpenseParticipantsAdapter);

        RadioGroup radioGroup = popupView.findViewById(R.id.addExpense_expenseType);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.addExpense_groupRadio) {
                    expenseParticipantsRecycleView.setVisibility(View.VISIBLE);
                } else {
                    expenseParticipantsRecycleView.setVisibility(View.INVISIBLE);
                    for(SelectableUser selectableUser : expensePartcipants) {
                        if(!selectableUser.getLogin().equals(CredentialManager.getInstance().getCurrentUser().getLogin())) {
                            selectableUser.setSelected(false);
                        }
                        selectExpenseParticipantsAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    private void initializeUserDialog() {
        MultiSpinnerSearch multiSelectSpinnerWithSearch = findViewById(R.id.addMemberFloatingButton);
        multiSelectSpinnerWithSearch.setEnabled(false);
        multiSelectSpinnerWithSearch.setSearchHint("Type at least 3 characters...");
        multiSelectSpinnerWithSearch.setOnCancel(new Runnable() {
            @Override
            public void run() {

            }
        });

        Response.Listener<JSONArray> responseListener = response -> {
            if(response != null) {
                System.out.println("Selectable users fetched successful. -> " + response.length() + " users");
            }
            tripMembersCandidates = parseSelectableUsers(response);

            for(SelectableUser selectableUser : tripMembersCandidates) {
                if(tripMembers.contains(selectableUser)) {
                    selectableUser.setSelected(true);
                }
            }

            multiSelectSpinnerWithSearch.setItems(tripMembersCandidates, new MultiSpinnerListener() {
                @SuppressLint("NewApi")
                @Override
                public void onSelectionEnd() {
                    updateTripMembers();
                }
            });

            multiSelectSpinnerWithSearch.setEnabled(true);
        };

        Response.ErrorListener errorListener =  error -> {
            error.printStackTrace();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateTripMembers() {
        long tripId = Long.parseLong(getIntent().getStringExtra("tripId"));
        long[] participantsAppUserId = tripMembersCandidates.stream().filter(SelectableUser::isSelected).mapToLong(User::getAppUserId).toArray();
        if(participantsAppUserId == null) {
            participantsAppUserId = new long[]{};
        }

        JSONArray postData = new JSONArray();
        for(int i = 0; i < participantsAppUserId.length; i++) {
            postData.put(JSONObject.wrap(participantsAppUserId[i]));
        }
        System.out.println(postData.toString());

        Response.Listener<JSONArray> responseListener = response -> {
            //ad.cancel();
            ToastHelper.getInstance().getSuccessfulMessageToast(this, "Trip members updated", Toast.LENGTH_SHORT).show();
            reloadCurrentTripDetails();
        };

        Response.ErrorListener errorListener = error -> {
            System.out.println(error.getMessage());
            error.printStackTrace();
            ToastHelper.getInstance().getErrorMessageToast(this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        };

        JsonArrayRequest jsonObjectRequest = HttpUtils.getInstance().getCustomJsonArrayRequest(
                Request.Method.PUT,
                Constants.API_URL + "custom/update-trip-participants/" + tripId,
                postData,
                responseListener,
                errorListener,
                CredentialManager.getInstance().getCurrentToken()
        );

        RequestService.getInstance().addRequest(jsonObjectRequest);

    }

    @SuppressLint("NewApi")
    public void addExpense_updateExpense(Long expenseId, String expenseName, double amount, int type) {

        long[] participantsAppUserId = expensePartcipants.stream().filter(p->p.isSelected()).mapToLong(User::getAppUserId).toArray();
        if(participantsAppUserId == null) {
            participantsAppUserId = new long[]{};
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", expenseId);
            postData.put("description", expenseName);
            postData.put("amount", amount);
            postData.put("expenseType", type == 0 ? "INDIVIDUAL" : "GROUP");
            postData.put("participantsAppUserId", JSONObject.wrap(participantsAppUserId));
            System.out.println(postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            ToastHelper.getInstance().getErrorMessageToast(this, "Invalid values", Toast.LENGTH_SHORT).show();
        }

        Response.Listener<JSONObject> responseListener = response -> {
            ad.cancel();
            ToastHelper.getInstance().getSuccessfulMessageToast(this, "Expense updated", Toast.LENGTH_SHORT).show();
            reloadCurrentTripDetails();
        };

        Response.ErrorListener errorListener = error -> {
            System.out.println(error.getMessage());
            error.printStackTrace();
            ToastHelper.getInstance().getErrorMessageToast(this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        };

        JsonObjectRequest jsonObjectRequest = HttpUtils.getInstance().getCustomJsonObjectRequest(
                Request.Method.PUT,
                Constants.API_URL + "custom/update-expense/" + expenseId,
                postData,
                responseListener,
                errorListener,
                CredentialManager.getInstance().getCurrentToken()
        );

        RequestService.getInstance().addRequest(jsonObjectRequest);
    }

    @SuppressLint("NewApi")
    public void addExpense_createExpense(String expenseName, double amount, int type) {
        long createdBy = CredentialManager.getInstance().getCurrentUser().getAppUserId();
        long tripId = Long.parseLong(getIntent().getStringExtra("tripId"));

        long[] participantsAppUserId = expensePartcipants.stream().filter(p->p.isSelected()).mapToLong(User::getAppUserId).toArray();
        if(participantsAppUserId == null) {
            participantsAppUserId = new long[]{};
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("description", expenseName);
            postData.put("createdBy", createdBy);
            postData.put("tripId", tripId);
            postData.put("amount", amount);
            postData.put("expenseType", type == 0 ? "INDIVIDUAL" : "GROUP");
            postData.put("participantsAppUserId", JSONObject.wrap(participantsAppUserId));
            System.out.println(postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            ToastHelper.getInstance().getErrorMessageToast(this, "Invalid values", Toast.LENGTH_SHORT).show();
        }

        Response.Listener<JSONObject> responseListener = response -> {
            ad.cancel();
            ToastHelper.getInstance().getSuccessfulMessageToast(this, "Expense created", Toast.LENGTH_SHORT).show();
            reloadCurrentTripDetails();
        };

        Response.ErrorListener errorListener = error -> {
            System.out.println(error.getMessage());
            error.printStackTrace();
            ToastHelper.getInstance().getErrorMessageToast(this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        };

        JsonObjectRequest jsonObjectRequest = HttpUtils.getInstance().getCustomJsonObjectRequest(
                Request.Method.POST,
                Constants.API_URL + "custom/create-expense",
                postData,
                responseListener,
                errorListener,
                CredentialManager.getInstance().getCurrentToken()
        );

        RequestService.getInstance().addRequest(jsonObjectRequest);
    }

    public void reloadCurrentTripDetails() {
        Intent intent = new Intent(this, TripDetails.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        String tripId = getIntent().getStringExtra("tripId");
        intent.putExtra("tripId", tripId.toString());
        this.startActivity(intent);
    }
}
