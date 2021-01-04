package com.example.groupexpensetrackermobile;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View v) {
        EditText userNameEdit = findViewById(R.id.userName);
        EditText passwordEdit = findViewById(R.id.password);

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();

        try {
            postData.put("username", userNameEdit.getText().toString());
            postData.put("password", passwordEdit.getText().toString());
            postData.put("langKey", "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> responseListener = response -> System.out.println("succes -> " + response.toString());

        Response.ErrorListener errorListener = error -> error.printStackTrace();

        JsonObjectRequest jsonObjectRequest = HttpUtils.INSTANCE.getCustomJsonObjectRequest(
            Request.Method.POST,
            Constants.API_URL + "authenticate",
            postData,
            responseListener,
            errorListener
        );

        queue.add(jsonObjectRequest);
    }
}