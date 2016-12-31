package com.example.yahya.esp.firebaseService;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.yahya.esp.db.config;
import com.example.yahya.esp.db.DatabaseHandler;
import com.example.yahya.esp.db.Message;
import com.example.yahya.esp.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.example.yahya.esp.activity.*;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    DatabaseHandler databaseHandler;
    Message message = new Message();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(databaseHandler==null){
            databaseHandler = new DatabaseHandler(this);
        }

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null) {
            return;
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());


        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String msg = data.getString("message");
            String timestamp = data.getString("timestamp");
            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + msg);
            Log.e(TAG, "timestamp: " + timestamp);


            message.setMessage(msg);
            message.setDate(timestamp);

            databaseHandler.addToDB(message);
            printDB();

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", msg);
                pushNotification.putExtra("date", timestamp);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                resultIntent.putExtra("message", msg);
                resultIntent.putExtra("date", timestamp);

                showNotificationMessage(getApplicationContext(), title, msg, timestamp, resultIntent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String msg, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, msg, timeStamp, intent);
    }

    public void printDB() {
        List<Message> messages = databaseHandler.getAllMessages();
        Log.d("Database Records", "======================================================");
        for (Message msg : messages) {
            String log = "Id: " + msg.getId() + " ,Message: " + msg.getMessage() + " ,Date: " + msg.getDate();
            // Writing Contacts to log
            Log.d("message: ", log);
        }
        Log.d("Database Records", "======================================================");
    }
}