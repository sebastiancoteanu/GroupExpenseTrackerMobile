package com.example.groupexpensetrackermobile.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.adapters.TripAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TripHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle;
    private TextView txtCreatedDate;
    private TextView txtBalance;

    public TripHolder(View itemView) {
        super(itemView);

        txtTitle = itemView.findViewById(R.id.tvTitle);
        txtCreatedDate = itemView.findViewById(R.id.tvDate);
        txtBalance = itemView.findViewById(R.id.tvBalance);
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setCreatedDate(LocalDate date) {
        txtCreatedDate.setText(date.toString());
    }

    public void setBalance(BigDecimal balance) {
        String sign = balance.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "-";
        if(sign == "-") {
            txtBalance.setTextColor(Color.rgb(183, 28, 28));
        } else {
            txtBalance.setTextColor(Color.rgb(0, 110, 26));
        }
        txtBalance.setText(balance.setScale(2).toString() + "$");
    }
}
