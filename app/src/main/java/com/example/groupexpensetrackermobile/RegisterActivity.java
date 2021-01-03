package com.example.groupexpensetrackermobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
        EditText userNameEdit = (EditText)findViewById(R.id.userName);
        EditText firstNameEdit = (EditText)findViewById(R.id.firstName);
        EditText lastNameEdit = (EditText)findViewById(R.id.lastName);
        EditText emailEdit = (EditText)findViewById(R.id.email);
        EditText passwordEdit = (EditText)findViewById(R.id.password);

        // TODO: make it a singletone
        RequestQueue queue = Volley.newRequestQueue(this);

        // TODO: extract somewhere as a const
        String url ="https://group-expense-tracker.herokuapp.com/api/register";
        JSONObject postData = new JSONObject();

        try {
            postData.put("firstName", userNameEdit.getText().toString());
            postData.put("lastName", firstNameEdit.getText().toString());
            postData.put("login", lastNameEdit.getText().toString());
            postData.put("email", emailEdit.getText().toString());
            postData.put("password", passwordEdit.getText().toString());
            postData.put("langKey", "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(postData);

        // TODO: Clean this shit up
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    JSONObject result = null;

                    if (jsonString != null && jsonString.length() > 0)
                        result = new JSONObject(jsonString);

                    return Response.success(result,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}