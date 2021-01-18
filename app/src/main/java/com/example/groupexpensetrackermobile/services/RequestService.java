package com.example.groupexpensetrackermobile.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestService {

    private static final RequestService INSTANCE = new RequestService();
    private Context context;
    private RequestQueue queue;

    private RequestService() {

    };

    public void setContext(Context context) {
        if(context != null) {
            throw new RuntimeException("Context already initialized.");
        }
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public static RequestService getInstance() {
        return INSTANCE;
    }

    public void addRequest(Request request) {
        if(queue == null) {
            throw new RuntimeException("Request queue is null. It should have been initialized.");
        }

        queue.add(request);
    }
}
