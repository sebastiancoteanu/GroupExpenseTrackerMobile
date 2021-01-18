package com.example.groupexpensetrackermobile.activities;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.adapters.SelectedTripUserAdapter;
import com.example.groupexpensetrackermobile.adapters.TripAdapter;
import com.example.groupexpensetrackermobile.entities.Trip;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.utilities.ToastHelper;

import java.util.ArrayList;
import java.util.List;


public class CreateTrip extends AppCompatActivity {

    private SelectedTripUserAdapter selectedUsersAdapter;
    private RecyclerView selectedUsersRecycleView;
    private List<User> selectedUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create trip");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectedUsers = createMockedListOfUsers();
        selectedUsersAdapter = new SelectedTripUserAdapter(selectedUsers, this);
        selectedUsersRecycleView = findViewById(R.id.selectedUsersRecycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        selectedUsersRecycleView.setLayoutManager(layoutManager);
        selectedUsersRecycleView.setAdapter(selectedUsersAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<User> createMockedListOfUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, 1, "popion", "Ion", "Popescu", "pop@ion.com"));
        userList.add(new User(2, 2, "alexpop", "Alex", "Popescu", "pop@alex.com"));
        userList.add(new User(3, 3, "popalex", "Alex", "Popa", "popa@alex.com"));
        userList.add(new User(4, 4, "mihaiandrei", "Andrei", "Mihai", "ama@mama.com"));
        userList.add(new User(5, 5, "vladdalv", "Vlad", "Coteanu", "vsc@ion.com"));
        userList.add(new User(6, 6, "sebibest", "Sebastian", "Coteanu", "svc@ion.com"));
        userList.add(new User(7, 7, "razvannnn", "Razvan", "Andreescu", "email@ion.com"));
        userList.add(new User(8, 8, "georgeeee", "George", "Mitica", "g@mitica.com"));
        userList.add(new User(9, 9, "vasileslav", "Vasile", "Vasilescu", "vv@vvv.com"));
        return userList;
    }

    public void showUserSpinner(View v) {
        ToastHelper.getInstance().getSuccessfulMessageToast(this, "Show user spinner", Toast.LENGTH_SHORT).show();
    }
}