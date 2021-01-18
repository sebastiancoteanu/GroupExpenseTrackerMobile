package com.example.groupexpensetrackermobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.services.RequestService;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;
import com.example.groupexpensetrackermobile.utilities.ToastHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void goToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View v) {

        disableInputControls();

        EditText userNameEdit = findViewById(R.id.userName);
        EditText firstNameEdit = findViewById(R.id.firstName);
        EditText lastNameEdit = findViewById(R.id.lastName);
        EditText emailEdit = findViewById(R.id.email);
        EditText passwordEdit = findViewById(R.id.password);

        JSONObject postData = new JSONObject();

        try {
            postData.put("firstName", firstNameEdit.getText().toString());
            postData.put("lastName", lastNameEdit.getText().toString());
            postData.put("login", userNameEdit.getText().toString());
            postData.put("email", emailEdit.getText().toString());
            postData.put("password", passwordEdit.getText().toString());
            postData.put("langKey", "en");
        } catch (JSONException e) {
            e.printStackTrace();
            ToastHelper.getInstance().getErrorMessageToast(v.getContext(), "Invalid values", Toast.LENGTH_SHORT).show();
            enableInputControls();
        }

        Response.Listener<JSONObject> responseListener = response -> {
            enableInputControls();
            ToastHelper.getInstance().getSuccessfulMessageToast(v.getContext(), "Account created", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        };

        Response.ErrorListener errorListener = error -> {
            error.printStackTrace();
            enableInputControls();
            ToastHelper.getInstance().getErrorMessageToast(v.getContext(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        };

        JsonObjectRequest jsonObjectRequest = HttpUtils.getInstance().getCustomJsonObjectRequest(
            Request.Method.POST,
            Constants.API_URL + "register",
            postData,
            responseListener,
            errorListener,
            null
        );
        RequestService.getInstance().addRequest(jsonObjectRequest);
    }

    private void disableInputControls() {
        EditText userNameEdit = findViewById(R.id.userName);
        EditText firstNameEdit = findViewById(R.id.firstName);
        EditText lastNameEdit = findViewById(R.id.lastName);
        EditText emailEdit = findViewById(R.id.email);
        EditText passwordEdit = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.registerButton);

        userNameEdit.setEnabled(false);
        firstNameEdit.setEnabled(false);
        lastNameEdit.setEnabled(false);
        emailEdit.setEnabled(false);
        passwordEdit.setEnabled(false);
        registerButton.setEnabled(false);
    }

    private void enableInputControls() {
        EditText userNameEdit = findViewById(R.id.userName);
        EditText firstNameEdit = findViewById(R.id.firstName);
        EditText lastNameEdit = findViewById(R.id.lastName);
        EditText emailEdit = findViewById(R.id.email);
        EditText passwordEdit = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.registerButton);

        userNameEdit.setEnabled(true);
        firstNameEdit.setEnabled(true);
        lastNameEdit.setEnabled(true);
        emailEdit.setEnabled(true);
        passwordEdit.setEnabled(true);
        registerButton.setEnabled(true);
    }
}