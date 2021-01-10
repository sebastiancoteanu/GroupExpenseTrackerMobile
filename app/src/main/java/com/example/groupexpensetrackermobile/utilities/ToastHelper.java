package com.example.groupexpensetrackermobile.utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastHelper {

    private static final ToastHelper INSTANCE = new ToastHelper();

    private ToastHelper() {
    }

    public static ToastHelper getInstance() {
        return INSTANCE;
    }

    public Toast getErrorMessageToast(Context appContext, String toastMessage, int toastLength) {

        Toast toast = Toast.makeText(appContext, toastMessage, toastLength);
        TextView message = toast.getView().findViewById(android.R.id.message);

        message.setTextColor(Color.rgb(183, 28, 28));

        View view = toast.getView();
        view.setBackgroundColor(Color.WHITE);

        return toast;

    }

    public Toast getSuccessfulMessageToast(Context appContext, String toastMessage, int toastLength) {

        Toast toast = Toast.makeText(appContext, toastMessage, toastLength);
        TextView message = toast.getView().findViewById(android.R.id.message);

        message.setTextColor(Color.rgb(1, 87, 155));

        View view = toast.getView();
        view.setBackgroundColor(Color.WHITE);

        return toast;

    }

}
