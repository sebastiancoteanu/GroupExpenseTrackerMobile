package com.example.groupexpensetrackermobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.adapters.TripAdapter;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.entities.Trip;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TripAdapter listAdapter;
    private RecyclerView tripRecyclerView;
    private List<Trip> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isLogged = CredentialManager.getInstance().getCurrentUser() != null;

        if (!isLogged) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        initializeBottomNavigationBar();
        initializeTripRecycleView();
    }

    private void initializeTripRecycleView() {
        tripRecyclerView = findViewById(R.id.tripRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tripRecyclerView.setLayoutManager(layoutManager);
        listAdapter = new TripAdapter(tripList, this);
        tripRecyclerView.setAdapter(listAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tripList.add(new Trip(1, "Revelion 2020", "Grup de revelion 2020", LocalDate.now(), BigDecimal.valueOf(20.5)));
            tripList.add(new Trip(2, "Revelion 2021", "Grup de revelion 2020", LocalDate.now(), BigDecimal.valueOf(-10.23)));
            tripList.add(new Trip(2, "Revelion 2023", "Grup de revelion 2020", LocalDate.now(), BigDecimal.valueOf(-6.23)));
            tripList.add(new Trip(2, "Revelion 2024", "Grup de revelion 2020", LocalDate.now(), BigDecimal.valueOf(12.23)));
            tripList.add(new Trip(2, "Revelion 2025", "Grup de revelion 2020", LocalDate.now(), BigDecimal.valueOf(72.11)));
        }

        listAdapter.notifyDataSetChanged();
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
}