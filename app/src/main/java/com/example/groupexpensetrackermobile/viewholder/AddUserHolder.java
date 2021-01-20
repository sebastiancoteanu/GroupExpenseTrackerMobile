package com.example.groupexpensetrackermobile.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;

public class AddUserHolder extends RecyclerView.ViewHolder {

    public AddUserHolder(@NonNull View itemView) {
        super(itemView);

        firstNameLastName = itemView.findViewById(R.id.addUsersFirstNameLastName);
        username = itemView.findViewById(R.id.addUsersUserName);
        addUserCheckbox = itemView.findViewById(R.id.addUsersCheckbox);
    }

    private TextView firstNameLastName;
    private TextView username;
    private CheckBox addUserCheckbox;

    public void setFirstNameLastName(String value) {
        this.firstNameLastName.setText(value);
    }


    public void setUsername(String value) {
        this.username.setText(value);
    }

    public CheckBox getAddUserCheckbox() {
        return this.addUserCheckbox;
    }
}
