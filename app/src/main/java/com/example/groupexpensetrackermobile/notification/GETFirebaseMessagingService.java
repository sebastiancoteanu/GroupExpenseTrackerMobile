package com.example.groupexpensetrackermobile.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.groupexpensetrackermobile.MainApp;
import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.activities.LoginActivity;
import com.example.groupexpensetrackermobile.activities.MainActivity;
import com.example.groupexpensetrackermobile.config.CredentialManager;
import com.example.groupexpensetrackermobile.entities.User;
import com.example.groupexpensetrackermobile.services.RequestService;
import com.example.groupexpensetrackermobile.utilities.Constants;
import com.example.groupexpensetrackermobile.utilities.HttpUtils;
import com.example.groupexpensetrackermobile.utilities.ToastHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class GETFirebaseMessagingService extends FirebaseMessagingService {

    private static String FCM_TOKEN;

    private static int counter = 0;

    @Override
    public void onNewToken(String token) {
        System.out.println("Refreshed token: " + token);
        FCM_TOKEN = token;
        saveTokenForLoggedUser();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        } else {
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String description = data.get("description");
            showNotification(title, description);
        }
    }

    public void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        // Assign channel ID
        String channel_id = "title";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_money)
                .setContentText(message.substring(0, 40) + "...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message));


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel( notificationChannel);
        }

        notificationManager.notify(counter, builder.build());
        counter = counter + 1;
    }

    public static void saveTokenForLoggedUser() {

        User loggedUser = CredentialManager.getInstance().getCurrentUser();
        String token = CredentialManager.getInstance().getCurrentToken();
        if (FCM_TOKEN == null || loggedUser == null || token == null) {
            return;
        }

        System.out.println("Attempt to register token for logged user");

        JSONObject postData = new JSONObject();

        Response.Listener<JSONObject> responseListener = response -> {
            System.out.println("Successful registered token for logged user");
        };

        Response.ErrorListener errorListener = Throwable::printStackTrace;

        try {
            postData.put("token", FCM_TOKEN);
            postData.put("appUserId", loggedUser.getAppUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = HttpUtils.getInstance().getCustomJsonObjectRequest(
                Request.Method.POST,
                Constants.API_URL + "fcm-tokens",
                postData,
                responseListener,
                errorListener,
                CredentialManager.getInstance().getCurrentToken()
        );

        RequestService.getInstance().addRequest(jsonObjectRequest);
    }
}
