package com.example.groupexpensetrackermobile.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.entities.User;

import java.util.List;

public class SelectedUserHolder extends RecyclerView.ViewHolder {

    public SelectedUserHolder(@NonNull View itemView) {
        super(itemView);

        firstNameLastName = itemView.findViewById(R.id.tvFirstNameLastName);
        username = itemView.findViewById(R.id.tvUserName);
        removeItemButton = itemView.findViewById(R.id.btnRemoveButton);
    }

    private TextView firstNameLastName;
    private TextView username;
    private ImageButton removeItemButton;

    public void setFirstNameLastName(String value) {
        firstNameLastName.setText(value);
    }

    public void setUserName(String value) {
        username.setText(value);
    }

    public ImageButton getRemoveItemButton() {
        return removeItemButton;
    }

}
