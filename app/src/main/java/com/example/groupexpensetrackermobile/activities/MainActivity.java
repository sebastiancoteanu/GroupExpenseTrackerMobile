package com.example.groupexpensetrackermobile.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.adapters.TripAdapter;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.entities.Trip;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.notification.GETFirebaseMessagingService;
import com.example.groupexpensetrackermobile.services.RequestService;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TripAdapter listAdapter;
    private RecyclerView tripRecyclerView;
    private List<Trip> tripList = new ArrayList<>();

    private int currentPage;
    private int numberOfPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestService.getInstance().setContext(this);

        boolean isLogged = CredentialManager.getInstance().getCurrentUser() != null;

        if (!isLogged) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        // Save current registration token
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GETFirebaseMessagingService.saveTokenForLoggedUser();
            }
        });
        t.start();

        logUserStatus();
        initializeBottomNavigationBar();

        // Method that should show the spinner and block user input
        disableInputControls();
        initializeTripRecycleView();

    }

    private void logUserStatus() {
        User user = CredentialManager.getInstance().getCurrentUser();
        System.out.println("Current user is " + user.toString());
        System.out.println("Current token is " + CredentialManager.getInstance().getCurrentToken());
    }

    private void initializeTripRecycleView() {
        tripRecyclerView = findViewById(R.id.tripRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tripRecyclerView.setLayoutManager(layoutManager);

        Response.Listener<JSONArray> responseListener = response -> {
            if(response != null) {
                System.out.println("User trips fetched successful. -> " + response.length() + " trips");
            }
            tripList = parseUserTrips(response);
            numberOfPages = tripList.size() / TripAdapter.MAX_PAGE_SIZE + (tripList.size() % TripAdapter.MAX_PAGE_SIZE == 0 ? 0 : 1);
            System.out.println("Total number of pages is " + numberOfPages);
            listAdapter = new TripAdapter(tripList, this);
            tripRecyclerView.setAdapter(listAdapter);
            updateRecycleViewPage(1);
            enableInputControls();
        };

        Response.ErrorListener errorListener =  error -> {
            error.printStackTrace();
            enableInputControls();
        };

        JsonArrayRequest jsonArrayRequest = HttpUtils.getInstance().getCustomJsonArrayRequest(
                Request.Method.GET,
                Constants.API_URL + "custom/trips/participant/" + CredentialManager.getInstance().getCurrentUser().getAppUserId(),
                null,
                responseListener,
                errorListener,
                CredentialManager.getInstance().getCurrentToken()
        );

        RequestService.getInstance().addRequest(jsonArrayRequest);
    }

    public void onPreviousPageButtonClick(View v) {
        if(currentPage > 1) {
            updateRecycleViewPage(currentPage - 1);
        }
    }

    public void onNextPageButtonClick(View v) {
        if(currentPage < numberOfPages) {
            updateRecycleViewPage(currentPage + 1);
        }
    }

    private void updateRecycleViewPage(int newCurrentPage) {
        if(newCurrentPage >= 1 && newCurrentPage <= this.numberOfPages) {

            System.out.println("Updating recycle view. New page: " + newCurrentPage);

            List<Trip> pageSection = tripList.subList(
                    TripAdapter.MAX_PAGE_SIZE * (newCurrentPage - 1),
                    Math.min(tripList.size(), TripAdapter.MAX_PAGE_SIZE * newCurrentPage));
            listAdapter.setTripList(pageSection);
            listAdapter.notifyDataSetChanged();

            currentPage = newCurrentPage;
            TextView paginationInfo = findViewById(R.id.paginationInfo);
            paginationInfo.setText(currentPage + " / " + numberOfPages);

            View previousPage = findViewById(R.id.previousPage);
            View nextPage = findViewById(R.id.nextPage);
            if(currentPage == 1) {
                previousPage.setClickable(false);
            } else {
                previousPage.setClickable(true);
            }

            if(currentPage == numberOfPages) {
                nextPage.setClickable(false);
            } else {
                nextPage.setClickable(true);
            }
        }
    }

    private void initializeBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_profile:
                    if(bottomNavigationView.getSelectedItemId() != R.id.action_profile) {
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.action_main:
                    if(bottomNavigationView.getSelectedItemId() != R.id.action_main) {
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.action_reports:
                    if(bottomNavigationView.getSelectedItemId() != R.id.action_reports) {
                        Toast.makeText(MainActivity.this, "Reports", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        });

    }

    private List<Trip> parseUserTrips(JSONArray jsonArray) {
        List<Trip> trip = new ArrayList<>();

        if(jsonArray == null) {
            return trip;
        }

        try {
            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject == null) {
                    continue;
                }
                String createdDate = jsonObject.getString("createdDate");
                LocalDate date = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    date = LocalDate.parse(createdDate);
                }

                // balance is hardcoded for now
                trip.add(new Trip(
                        jsonObject.getLong("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        date,
                        BigDecimal.TEN));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return trip;
        }

        return trip;
    }

    private List<Trip> getMockedData() {
        List<Trip> localTripList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localTripList.add(new Trip(1, "Revelion 2020", "Grup de revelion 2020", LocalDate.now(), BigDecimal.valueOf(20.5)));
            localTripList.add(new Trip(2, "Revelion 2021", "Grup de revelion 2021", LocalDate.now(), BigDecimal.valueOf(-10.23)));
            localTripList.add(new Trip(2, "Revelion 2023", "Grup de revelion 2022", LocalDate.now(), BigDecimal.valueOf(-6.23)));
            localTripList.add(new Trip(2, "Revelion 2024", "Grup de revelion 2023", LocalDate.now(), BigDecimal.valueOf(12.23)));
            localTripList.add(new Trip(2, "Revelion 2025", "Grup de revelion 2024", LocalDate.now(), BigDecimal.valueOf(72.11)));
        }

        return localTripList;
    }

    private void disableInputControls() {
        View previousPage = findViewById(R.id.previousPage);
        View nextPage = findViewById(R.id.nextPage);
        ProgressBar progressBar = findViewById(R.id.mainProgressBar);

        previousPage.setEnabled(false);
        nextPage.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
    }

    private void enableInputControls() {
        View previousPage = findViewById(R.id.previousPage);
        View nextPage = findViewById(R.id.nextPage);
        ProgressBar progressBar = findViewById(R.id.mainProgressBar);

        previousPage.setEnabled(true);
        nextPage.setEnabled(true);

        progressBar.setVisibility(View.GONE);
    }

    public void goToCreateTrip(View v) {
        Intent intent = new Intent(this, CreateTrip.class);
        startActivity(intent);
    }
}