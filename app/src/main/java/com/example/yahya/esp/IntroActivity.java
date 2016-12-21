package com.example.yahya.esp;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import Location.LocationFinder;

public class IntroActivity extends Activity {
    private static final String TAG = IntroActivity.class.getSimpleName();
    double longitude;
    double latitude;
    private static final int PERMISSION_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocationFinder.getInstance().ConnectGoogleApiClient();
        LocationFinder.getInstance().setActivity(this);
        LocationFinder.getInstance().gate(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!LocationFinder.getInstance().isGoogleApiClientConnected()){
            LocationFinder.getInstance().ConnectGoogleApiClient();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationFinder.getInstance().DisconnectGoogleApiClient();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    LocationFinder.getInstance().getPermission();
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    LocationFinder.getInstance().gate(2);
                }
                break;
        }
    }
}
