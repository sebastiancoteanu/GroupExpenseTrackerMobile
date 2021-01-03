package com.example.groupexpensetrackermobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isLogged = false;

        if (!isLogged) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }

    }
}