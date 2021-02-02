package com.example.groupexpensetrackermobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.entities.Expense;
import com.example.groupexpensetrackermobile.listeners.OnItemClickListener;
import com.example.groupexpensetrackermobile.viewholder.ExpenseHolder;

import java.util.List;

public class TripExpensesAdapter extends RecyclerView.Adapter<ExpenseHolder> {
    private List<Expense> expenses;
    private LayoutInflater layoutInflater;
    private final Context mContext;
    private OnItemClickListener onItemClickListener;

    public TripExpensesAdapter(List<Expense> expenses, Context context, OnItemClickListener onItemClickListener) {
        this.expenses = expenses;
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.expense_item, parent, false);
        return new ExpenseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
        final Expense expense = expenses.get(position);
        holder.setExpenseData(expense.getDescription(), expense.getAmount());
        if(onItemClickListener != null) {
            holder.getDescriptionTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemLongClick(expense.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}
