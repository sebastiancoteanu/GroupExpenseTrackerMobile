package com.example.groupexpensetrackermobile.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;

public class TripMemberHolder extends RecyclerView.ViewHolder {
    private TextView firstNameLastName;
    private TextView username;
    private TextView wealth;

    public TripMemberHolder(@NonNull View itemView) {
        super(itemView);

        firstNameLastName = itemView.findViewById(R.id.firstNameLastName);
        username = itemView.findViewById(R.id.userName);
        wealth = itemView.findViewById(R.id.wealth);
    }

    public void setFirstNameLastName(String value) {
        this.firstNameLastName.setText(value);
    }

    public void setWealth(int value) {
        this.wealth.setText(String.valueOf(value).concat("$"));
    }

    public void setUsername(String value) {
        this.username.setText(value);
    }

}
