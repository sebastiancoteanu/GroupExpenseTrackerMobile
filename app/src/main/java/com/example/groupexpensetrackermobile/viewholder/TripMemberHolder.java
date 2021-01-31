package com.example.groupexpensetrackermobile.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;

import java.math.BigDecimal;

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

    public void setWealth(BigDecimal balance) {
        if(balance == null) {
            wealth.setTextColor(Color.rgb(0, 110, 26));
            wealth.setText("0$");
        }
        String sign = balance.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "-";
        if(sign == "-") {
            wealth.setTextColor(Color.rgb(183, 28, 28));
        } else {
            wealth.setTextColor(Color.rgb(0, 110, 26));
        }
        wealth.setText(balance.setScale(2).toString() + "$");
    }

    public void setUsername(String value) {
        this.username.setText(value);
    }

}
