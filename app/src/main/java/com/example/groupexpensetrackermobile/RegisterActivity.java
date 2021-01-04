package com.example.groupexpensetrackermobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;

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
        EditText userNameEdit = findViewById(R.id.userName);
        EditText firstNameEdit = findViewById(R.id.firstName);
        EditText lastNameEdit = findViewById(R.id.lastName);
        EditText emailEdit = findViewById(R.id.email);
        EditText passwordEdit = findViewById(R.id.password);

        RequestQueue queue = Volley.newRequestQueue(this);

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
        }

        Response.Listener<JSONObject> responseListener = response -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        };

        Response.ErrorListener errorListener = error -> error.printStackTrace();

        JsonObjectRequest jsonObjectRequest = HttpUtils.INSTANCE.getCustomJsonObjectRequest(
            Request.Method.POST,
            Constants.API_URL + "register",
            postData,
            responseListener,
            errorListener
        );

        queue.add(jsonObjectRequest);
    }
}