package com.example.groupexpensetrackermobile.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.services.LocalStorageService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        boolean isLogged = CredentialManager.getInstance().getCurrentUser() != null;

        if (!isLogged) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        completeProfileData();
        initializeBottomNavigationBar();
    }

    private void completeProfileData() {
        User currentUser = CredentialManager.getInstance().getCurrentUser();

        EditText userNameEdit = findViewById(R.id.profile_username);
        userNameEdit.setText(currentUser.getLogin());

        EditText firstNameEdit = findViewById(R.id.profile_firstName);
        firstNameEdit.setText(currentUser.getFirstName());

        EditText lastNameEdit = findViewById(R.id.profile_lastName);
        lastNameEdit.setText(currentUser.getLastName());

        EditText emailEdit = findViewById(R.id.profile_email);
        emailEdit.setText(currentUser.getEmail());

    }

    public void logoutUser(View v) {
        LocalStorageService.getInstance().clearStorage();
        CredentialManager.getInstance().removeUser();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void initializeBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_profile:
                    if(bottomNavigationView.getSelectedItemId() != R.id.action_profile) {
                        Intent intent = new Intent(this, ProfileActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.action_main:
                    if(bottomNavigationView.getSelectedItemId() != R.id.action_main) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.action_reports:
                    if(bottomNavigationView.getSelectedItemId() != R.id.action_reports) {
                        Toast.makeText(ProfileActivity.this, "Reports", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        });

    }

}
