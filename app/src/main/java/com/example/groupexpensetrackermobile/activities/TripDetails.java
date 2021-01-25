package com.example.groupexpensetrackermobile.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.adapters.TripExpensesAdapter;
import com.example.groupexpensetrackermobile.adapters.TripMembersAdapter;
import com.example.groupexpensetrackermobile.entities.Expense;
import com.example.groupexpensetrackermobile.entities.ExpenseType;
import com.example.groupexpensetrackermobile.entities.User;

import java.util.ArrayList;
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

        tripMembers = createMockedListOfUsers();
        tripMembersAdapter = new TripMembersAdapter(tripMembers, this);
        tripMembersRecyclerView = findViewById(R.id.membersRecycler);
        LinearLayoutManager layoutManagerMembers = new LinearLayoutManager(this);
        tripMembersRecyclerView.setLayoutManager(layoutManagerMembers);
        tripMembersRecyclerView.setAdapter(tripMembersAdapter);

        tripExpenses = createMockedListOfExpenses();
        tripExpensesAdapter = new TripExpensesAdapter(tripExpenses, this);
        tripExpensesRecyclerView = findViewById(R.id.expensesRecycler);
        LinearLayoutManager layoutManagerExpenses = new LinearLayoutManager(this);
        tripExpensesRecyclerView.setLayoutManager(layoutManagerExpenses);
        tripExpensesRecyclerView.setAdapter(tripExpensesAdapter);
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
