package com.example.groupexpensetrackermobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.entities.Expense;
import com.example.groupexpensetrackermobile.viewholder.ExpenseHolder;

import java.util.List;

public class TripExpensesAdapter extends RecyclerView.Adapter<ExpenseHolder> {
    private List<Expense> expenses;
    private LayoutInflater layoutInflater;
    private final Context mContext;

    public TripExpensesAdapter(List<Expense> expenses, Context context) {
        this.expenses = expenses;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.add_users_card, parent, false);
        return new ExpenseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
        final Expense expense = expenses.get(position);
        holder.setExpenseData(expense.getDescription(), expense.getAmount());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}
