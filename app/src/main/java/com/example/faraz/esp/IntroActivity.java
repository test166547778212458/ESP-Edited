package com.example.faraz.esp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import Location.LocationFinder;

public class IntroActivity extends Activity implements LocationListener {
    private static final String TAG = IntroActivity.class.getSimpleName();
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private static final int PERMISSION_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocationFinder.getInstance().getGoogleApiClient().connect();
        gate(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!LocationFinder.getInstance().getGoogleApiClient().isConnected()){
            LocationFinder.getInstance().getGoogleApiClient().connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationFinder.getInstance().getGoogleApiClient().disconnect();
    }

    public void gate(int entry){
        if(entry == 1){
            nextActivity();
        }else if (entry == 2){
            closeApp();
        } else if (entry == 3){
            askPermission();
        }
    }

    public void getPermission() {
        // if user device api is equal marshmello api or greater
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ask if user granted Location permission
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                //dialog pop up to user asking for permission
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                return;
            }
        }
        try{
            //Exception may happen.
            //Google Api Client not connected.
            fusedLocationProviderApi.requestLocationUpdates(LocationFinder.getInstance().getGoogleApiClient(),
                    LocationFinder.getInstance().getLocationRequest(), this);
            gate(1);
        }catch (IllegalStateException e){
            Log.d(TAG,e.getMessage());
            gate(3);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getPermission();
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    gate(2);
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void nextActivity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(IntroActivity.this, PinActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }

    public void askPermission(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getPermission();
            }
        }, 3000);
    }

    public void closeApp(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 3000);
    }




}
