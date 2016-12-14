package Location;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.faraz.esp.PinActivity;
import com.example.faraz.esp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import connection.RequestQueueSingleton;

public class LocationFinder extends Application implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private static final String TAG = LocationFinder.class.getSimpleName();
    private static LocationFinder Instance;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Activity activity;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    Location location;

    private SharedPreferences sp;
    private SharedPreferences.Editor spE;
    private static final String filename = "ESP";
    private static final String LSL = "LSL";

    private SimpleDateFormat sdf;
    private DateFormat df;
    private Date d;

    private static final int PERMISSION_REQUEST_CODE = 112;

    public static LocationFinder getInstance(){
        return Instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000);
        locationRequest.setFastestInterval(15 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        sp = getSharedPreferences(filename, 0);
        spE = sp.edit();
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

    public void nextActivity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(activity, PinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                activity.finish();
            }
        }, 3000);
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
        d = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("d:M:H:m");

        String currentDate = df.format(d);
        String spDate = sp.getString(LSL,"");

        //if spDate is empty that mean never sent location to the server
        if(spDate.length() == 0){
            spE.putString(LSL,df.format(d)).commit();
            sendLocation();
            return;
        }

        String[] currentDateArray = currentDate.split(":");
        String[] spDateArray = spDate.split(":");

        //compare months
        if(Integer.valueOf(currentDateArray[1]) > Integer.valueOf(spDateArray[1]))
        {
            spE.putString(LSL,df.format(d)).commit();
            sendLocation();
        }
        //compare days
        else if(Integer.valueOf(currentDateArray[0]) > Integer.valueOf(spDateArray[0]))
        {
            spE.putString(LSL,df.format(d)).commit();
            sendLocation();
        }
        //compare hours
        else if(Integer.valueOf(currentDateArray[2]) > Integer.valueOf(spDateArray[2]))
        {
            spE.putString(LSL,df.format(d)).commit();
            sendLocation();
        }
        //compare minutes
        else if(Integer.valueOf(currentDateArray[3]) > (Integer.valueOf(spDateArray[3])+30)){
            spE.putString(LSL,df.format(d)).commit();
            sendLocation();
        }else{}
    }

    private void sendLocation(){

        String url = getString(R.string.server)+getString(R.string.path_2)+"&lat="+location.getLatitude()+
                "&lon="+location.getLongitude();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance().addToRequestQueue(stringRequest);
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
}
