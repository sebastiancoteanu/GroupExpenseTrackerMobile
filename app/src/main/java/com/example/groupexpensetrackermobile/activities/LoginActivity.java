package com.example.groupexpensetrackermobile.activities;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;
import com.example.groupexpensetrackermobile.utilities.ToastHelper;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    public void goToHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onLogin(View v) {

        disableInputControls();
        JSONObject postData = null;

        try {
            postData = getInputData();
        } catch (JSONException e) {
            e.printStackTrace();
            ToastHelper.getInstance().getErrorMessageToast(v.getContext(), "Invalid values", Toast.LENGTH_SHORT).show();
            enableInputControls();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        // Listeners for authenticating and fetching the jwt token
        // If this auth request is successful, then we should fetch the account details
        Response.Listener<JSONObject> responseListener = response -> {
            System.out.println("Login successful. -> " + response.toString());

            boolean status = CredentialManager.getInstance().storeToken(response);
            if(status) {
                JsonObjectRequest fetchAccountDetails = makeFetchAccountDetailsLoginRequest(v);
                queue.add(fetchAccountDetails);
            } else {
                enableInputControls();
                ToastHelper.getInstance().getErrorMessageToast(v.getContext(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
            }

        };

        Response.ErrorListener errorListener =  error -> {
            error.printStackTrace();
            enableInputControls();
            ToastHelper.getInstance().getErrorMessageToast(v.getContext(), "Wrong username or password. Please try again!", Toast.LENGTH_SHORT).show();
        };

        JsonObjectRequest jsonObjectRequest = HttpUtils.getInstance().getCustomJsonObjectRequest(
            Request.Method.POST,
            Constants.API_URL + "authenticate",
            postData,
            responseListener,
            errorListener,
     null
        );

        queue.add(jsonObjectRequest);
    }

    private JsonObjectRequest makeFetchAccountDetailsLoginRequest(View v) {
        Response.Listener<JSONObject> fetchAccountDetailsResponseListener = response -> {
            System.out.println("Account details successful fetched -> " + response.toString());
            boolean status = CredentialManager.getInstance().storeUser(response);
            enableInputControls();

            if(status) {
                goToHome(v);
                String firstName = CredentialManager.getInstance().getCurrentUser().getFirstName();
                ToastHelper.getInstance().getSuccessfulMessageToast(v.getContext(), "Welcome + " + firstName + "!", Toast.LENGTH_SHORT).show();
            } else {
                ToastHelper.getInstance().getErrorMessageToast(v.getContext(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
            }

        };

        Response.ErrorListener fetchAccountDetailsErrorListener =  error -> {
            error.printStackTrace();
            enableInputControls();
            ToastHelper.getInstance().getErrorMessageToast(v.getContext(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
        };

        return HttpUtils.getInstance().getCustomJsonObjectRequest(
                Request.Method.GET,
                Constants.API_URL + "account",
                null,
                fetchAccountDetailsResponseListener,
                fetchAccountDetailsErrorListener,
                CredentialManager.getInstance().getCurrentToken()
        );
    }

    private JSONObject getInputData() throws JSONException {

        EditText userNameEdit = findViewById(R.id.userName);
        EditText passwordEdit = findViewById(R.id.password);

        JSONObject postData = new JSONObject();
        postData.put("username", userNameEdit.getText().toString());
        postData.put("password", passwordEdit.getText().toString());
        postData.put("langKey", "en");

        return postData;
    }

    private void disableInputControls() {
        EditText userNameEdit = findViewById(R.id.userName);
        EditText passwordEdit = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.cirLoginButton);
        ProgressBar progressBar = findViewById(R.id.loginProgressBar);

        userNameEdit.setEnabled(false);
        passwordEdit.setEnabled(false);
        loginButton.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
    }

    private void enableInputControls() {
        EditText userNameEdit = findViewById(R.id.userName);
        EditText passwordEdit = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.cirLoginButton);
        ProgressBar progressBar = findViewById(R.id.loginProgressBar);

        userNameEdit.setEnabled(true);
        passwordEdit.setEnabled(true);
        loginButton.setEnabled(true);

        progressBar.setVisibility(View.GONE);
    }

}