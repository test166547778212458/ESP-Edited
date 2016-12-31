package com.example.yahya.esp.locationpkg;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yahya.esp.activity.PinActivity;
import com.example.yahya.esp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.yahya.esp.connection.RequestQueueSingleton;


public class LocationFinder extends Application implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private static final String TAG = LocationFinder.class.getSimpleName();
    private static LocationFinder Instance;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Activity activity;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    Location location;

    RequestQueueSingleton rqs;
    boolean mBounded;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private static final String filename = "ESP";
    private static final String lastStoredLocation = "LSL";

    final private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
    private Date currentdate;

    String url;

    private static final int PERMISSION_REQUEST_CODE = 112;

    public static LocationFinder getInstance(){
        return Instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;

        url = getString(R.string.server)+getString(R.string.path_2);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000);
        locationRequest.setFastestInterval(15 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        sharedPreferences = getSharedPreferences(filename, 0);
        sharedPreferencesEditor = sharedPreferences.edit();

        Intent mIntent = new Intent(this, RequestQueueSingleton.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            rqs = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            RequestQueueSingleton.LocalBinder mLocalBinder = (RequestQueueSingleton.LocalBinder)service;
            rqs = mLocalBinder.getServerInstance();
        }
    };

    public void gate(int entry){
        if(entry == 1){
            nextActivity();
        }else if (entry == 2){
            closeApp();
        } else if (entry == 3){
            askPermission();
        }
    }

    public void nextActivity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(activity, PinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, 1000);
    }

    public void askPermission(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getPermission();
            }
        }, 1000);
    }

    public void closeApp(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                activity.finish();
            }
        }, 1000);
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
                activity.requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
                return;
            }
        }
        try{
            //Exception may happen.
            //Google Api Client not connected.
            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Log.d(TAG, "fused location registered!");
            gate(1);
        }catch (IllegalStateException e){
            Log.d(TAG,e.getMessage());
            gate(3);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        googleApiClient.connect();
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onLocationChanged(Location mlocation) {
        location = new Location(mlocation);
        Log.d(TAG,"location on change");
        Log.d(TAG,"longitude :"+location.getLongitude()+" latitude : "+location.getLatitude());

        sendLocationToServer();
    }

    private void sendLocationToServer(){
        currentdate = Calendar.getInstance().getTime();

        String previousDateString = sharedPreferences.getString(lastStoredLocation,"");

        if(TextUtils.isEmpty(previousDateString)){
            String currentdateString = simpleDateFormat.format(currentdate);
            sharedPreferencesEditor.putString(lastStoredLocation,currentdateString).commit();
            sendLocation();
            return;
        }

        Date previousDate = null;

        try {
            previousDate = simpleDateFormat.parse(previousDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        long difference_in_ms = currentdate.getTime()-previousDate.getTime();
        long difference_in_s = difference_in_ms/1000;
        long difference_in_m = difference_in_s/60;

        if(Math.abs(difference_in_m) > 30){
            String currentdateString = simpleDateFormat.format(currentdate);
            sharedPreferencesEditor.putString(lastStoredLocation,currentdateString).commit();
            sendLocation();
        }
    }

    private void sendLocation(){
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("lat", String.valueOf(location.getLatitude()));
                params.put("lon", String.valueOf(location.getLongitude()));
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        rqs.addToRequestQueue(postRequest);
    }

    public GoogleApiClient getGoogleApiClient(){
        return googleApiClient;
    }

    public void ConnectGoogleApiClient(){
        googleApiClient.connect();
    }

    public void DisconnectGoogleApiClient(){
        googleApiClient.disconnect();
    }

    public boolean isGoogleApiClientConnected(){
        return googleApiClient.isConnected();
    }

    public Location getLocation(){
        return location;
    }

    public Context getApp(){
        return getApplicationContext();
    }

    public RequestQueueSingleton getRequestQueueSingleton(){return rqs;}
}
