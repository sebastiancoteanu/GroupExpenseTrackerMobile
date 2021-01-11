package com.example.groupexpensetrackermobile.activities;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;
import com.example.groupexpensetrackermobile.utilities.ToastHelper;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout firstNameLayout, lastNameLayout, loginLayout, emailLayout, passwordLayout;
    EditText firstNameEdit, lastNameEdit, loginEdit, emailEdit, passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = findViewById(R.id.registerButton);

        firstNameLayout = (TextInputLayout)findViewById(R.id.firstNameLayout);
        firstNameEdit = (EditText) findViewById(R.id.firstName);

        lastNameLayout = (TextInputLayout)findViewById(R.id.lastNameLayout);
        lastNameEdit = (EditText) findViewById(R.id.lastName);

        loginLayout = (TextInputLayout)findViewById(R.id.userNameLayout);
        loginEdit = (EditText) findViewById(R.id.userName);

        emailLayout = (TextInputLayout)findViewById(R.id.emailLayout);
        emailEdit = (EditText) findViewById(R.id.email);

        passwordLayout = (TextInputLayout)findViewById(R.id.passwordLayout);
        passwordEdit = (EditText) findViewById(R.id.password);

        firstNameEdit.addTextChangedListener(new ValidationTextWatcher(firstNameEdit));
        lastNameEdit.addTextChangedListener(new ValidationTextWatcher(lastNameEdit));
        loginEdit.addTextChangedListener(new ValidationTextWatcher(loginEdit));
        emailEdit.addTextChangedListener(new ValidationTextWatcher(emailEdit));
        passwordEdit.addTextChangedListener(new ValidationTextWatcher(passwordEdit));

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!validateBaseInput(firstNameLayout, firstNameEdit) ||
                    !validateBaseInput(lastNameLayout, lastNameEdit) ||
                    !validateUsername() ||
                    !validateEmail() ||
                    !validatePassword()) {
                    return;
                }
                register(v);
            }
        });
    }

    public void goToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void register(View v) {
        disableInputControls();
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

//        queue.add(jsonObjectRequest);
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

    // VALIDATIONS

    private boolean validateBaseInput(TextInputLayout t, EditText e) {
        if (e.getText().toString().length() == 0) {
            t.setError(t.getHint().toString().concat(" is required"));
            requestFocus(e);
            return false;
        } else {
            t.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateUsername() {
        String username = loginEdit.getText().toString();
        if (username.length() == 0) {
            loginLayout.setError("Username is required");
            requestFocus(loginEdit);
        } else if (username.length() > 50) {
            requestFocus(loginEdit);
            loginLayout.setError("Your username cannot be longer than 50 characters");
        } else {
            loginLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = emailEdit.getText().toString();
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (email.length() == 0) {
            emailLayout.setError("Email is required");
            requestFocus(emailEdit);
        } else if (email.length() > 100) {
            emailLayout.setError("Your email cannot be longer than 100 characters");
            requestFocus(emailEdit);
        } else if(!matcher.matches()) {
            emailLayout.setError("Invalid email address");
            requestFocus(emailEdit);
        } else {
            emailLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        String password = passwordEdit.getText().toString();

        if (password.length() == 0) {
            passwordLayout.setError("Password is required");
            requestFocus(passwordEdit);
        } else if (password.length() < 4) {
            passwordLayout.setError("Your password is required to be at least 4 characters");
            requestFocus(passwordEdit);
        } else if(password.length() > 50) {
            passwordLayout.setError("Your password cannot be longer than 50 characters");
            requestFocus(passwordEdit);
        } else {
            passwordLayout.setErrorEnabled(false);
        }
        return true;
    }

    private class ValidationTextWatcher implements TextWatcher {
        private View view;
        private ValidationTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.firstName:
                    validateBaseInput(firstNameLayout, firstNameEdit);
                    break;
                case R.id.lastName:
                    validateBaseInput(lastNameLayout, lastNameEdit);
                    break;
                case R.id.userName:
                    validateUsername();
                    break;
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
    }

}