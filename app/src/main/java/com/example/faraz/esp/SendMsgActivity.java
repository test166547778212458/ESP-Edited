package com.example.faraz.esp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import Location.LocationFinder;

public class SendMsgActivity extends AppCompatActivity {
    private static final String TAG = IntroActivity.class.getSimpleName();
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        longitude = LocationFinder.getInstance().getLocation().getLongitude();
//        latitude = LocationFinder.getInstance().getLocation().getLatitude();
//        Toast.makeText(this, "Longitude : "+String.valueOf(longitude)+" Latitude : "+String.valueOf(latitude),Toast.LENGTH_LONG).show();
    }
}

