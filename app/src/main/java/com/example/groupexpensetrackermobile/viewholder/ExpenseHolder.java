package com.example.groupexpensetrackermobile.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;

public class ExpenseHolder extends RecyclerView.ViewHolder {

    public ExpenseHolder(@NonNull View itemView) {
        super(itemView);

        descriptionTextView = itemView.findViewById(R.id.description);
        amountTextView = itemView.findViewById(R.id.amount);
    }

    private TextView descriptionTextView;
    private TextView amountTextView;


    private void setDescription(String value) {
        this.descriptionTextView.setText(value);
    }

    private void setAmount(Long value) {
        this.amountTextView.setText(value.toString().concat("$"));
    }

    public void setExpenseData(String description, Long amount) {
        setDescription(description);
        setAmount(amount);
    }

    public TextView getDescriptionTextView() {
        return descriptionTextView;
    }
}
