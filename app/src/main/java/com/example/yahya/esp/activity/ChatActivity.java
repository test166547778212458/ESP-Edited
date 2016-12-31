package com.example.yahya.esp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.yahya.esp.R;
import com.example.yahya.esp.db.DatabaseHandler;
import com.example.yahya.esp.db.Message;
import com.example.yahya.esp.db.config;
import com.example.yahya.esp.util.NotificationUtils;

import java.util.List;


public class ChatActivity extends AppCompatActivity{
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private DatabaseHandler databaseHandler;

    private ScrollView sv;
    private LinearLayout container;
    private LayoutInflater inflater;

    private TextView message;
    private TextView date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);
        Log.e("Activity status","onCreate");
        databaseHandler = new DatabaseHandler(this);

        sv = (ScrollView) findViewById(R.id.ScrollView);
        container = (LinearLayout) findViewById(R.id.AllMessagesContainer);
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        buildLayout();

        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(View.FOCUS_DOWN);
            }
        });

        message = (TextView) findViewById(R.id.content);
        date = (TextView) findViewById(R.id.date);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered

                } else if (intent.getAction().equals(config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String _message = intent.getStringExtra("message");
                    String _date = intent.getStringExtra("date");

                    LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.chatlayout, null);
                    TextView content = (TextView) ll.findViewById(R.id.content);
                    TextView date = (TextView) ll.findViewById(R.id.date);
                    content.setText(_message);
                    date.setText(_date);
                    container.addView(ll);

                    sv.post(new Runnable() {
                        @Override
                        public void run() {
                            sv.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Activity status","onResume");
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        Log.e("Activity status","onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        finish();
        super.onPause();
    }

    private void buildLayout(){
        int size = databaseHandler.getMessagesCount();
        if (size == 0) {
            container.getChildAt(0).setVisibility(View.GONE);
            return;
        }

        container.setVisibility(View.VISIBLE);

        List<Message> messageList = databaseHandler.getAllMessages();

        for (int i = 0 ; i < size ; i++){
            LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.chatlayout, null);
            TextView content = (TextView) ll.findViewById(R.id.content);
            TextView date = (TextView) ll.findViewById(R.id.date);
            content.setText(messageList.get(i).getMessage());
            date.setText(messageList.get(i).getDate());
            container.addView(ll);
        }
        container.getChildAt(0).setVisibility(View.GONE);
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
