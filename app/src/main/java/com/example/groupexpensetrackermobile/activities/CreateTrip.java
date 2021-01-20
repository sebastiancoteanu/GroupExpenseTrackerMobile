package com.example.groupexpensetrackermobile.activities;
import android.annotation.SuppressLint;
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
import com.example.groupexpensetrackermobile.customcomponents.MultiSpinnerSearch;
import com.example.groupexpensetrackermobile.entities.SelectableUser;
import com.example.groupexpensetrackermobile.entities.Trip;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.listeners.MultiSpinnerListener;
import com.example.groupexpensetrackermobile.utilities.ToastHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CreateTrip extends AppCompatActivity {

    private SelectedTripUserAdapter selectedUsersAdapter;
    private RecyclerView selectedUsersRecycleView;
    private List<SelectableUser> selectedUsers = new ArrayList<>();
    private List<SelectableUser> selectableUsers = new ArrayList<>();

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

        // Removed second parameter, position. Its not required now..
        // If you want to pass preselected items, you can do it while making listArray,
        // pass true in setSelected of any item that you want to preselect
        selectableUsers = createMockedListOfUsers();
        multiSelectSpinnerWithSearch.setItems(selectableUsers, new MultiSpinnerListener() {
            @SuppressLint("NewApi")
            @Override
            public void onSelectionEnd() {
                selectedUsers = selectableUsers.stream().filter(SelectableUser::isSelected).collect(Collectors.toList());
                selectedUsersAdapter.setUserList(selectedUsers);
                selectedUsersAdapter.notifyDataSetChanged();
            }
        });

        selectedUsers = new ArrayList<>();
        selectedUsersAdapter = new SelectedTripUserAdapter(selectedUsers, this);
        selectedUsersRecycleView = findViewById(R.id.selectedUsersRecycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        selectedUsersRecycleView.setLayoutManager(layoutManager);
        selectedUsersRecycleView.setAdapter(selectedUsersAdapter);
    }

    private void disableInputMethods() {
        //TextView titleTV = findViewById(R.id.tvTitle)
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<SelectableUser> createMockedListOfUsers() {
        List<SelectableUser> userList = new ArrayList<>();
        userList.add(new SelectableUser(1, 1, "popion", "Ion", "Popescu", "pop@ion.com", false));
        userList.add(new SelectableUser(2, 2, "alexpop", "Alex", "Popescu", "pop@alex.com", false));
        userList.add(new SelectableUser(3, 3, "popalex", "Alex", "Popa", "popa@alex.com", false));
        userList.add(new SelectableUser(4, 4, "mihaiandrei", "Andrei", "Mihai", "ama@mama.com", false));
        userList.add(new SelectableUser(5, 5, "vladdalv", "Vlad", "Coteanu", "vsc@ion.com", false));
        userList.add(new SelectableUser(6, 6, "sebibest", "Sebastian", "Coteanu", "svc@ion.com", false));
        userList.add(new SelectableUser(7, 7, "razvannnn", "Razvan", "Andreescu", "email@ion.com", false));
        userList.add(new SelectableUser(8, 8, "georgeeee", "George", "Mitica", "g@mitica.com", false));
        userList.add(new SelectableUser(9, 9, "vasileslav", "Vasile", "Vasilescu", "vv@vvv.com", false));
        return userList;
    }

    public void showUserSpinner(View v) {
        ToastHelper.getInstance().getSuccessfulMessageToast(this, "Show user spinner", Toast.LENGTH_SHORT).show();
    }
}