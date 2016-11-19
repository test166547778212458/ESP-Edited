package Location;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.faraz.esp.PinActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationFinder extends Application implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    private static final String TAG = LocationFinder.class.getSimpleName();
    private static LocationFinder Instance;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Activity activity;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

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
                activity.requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                return;
            }
        }
        try{
            //Exception may happen.
            //Google Api Client not connected.
            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
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
    public void onLocationChanged(Location location) {

    }

    public void ConnectGoogleApiClient(){
        googleApiClient.connect();
    }

    public void DisconnectGoogleApiClient(){
        googleApiClient.connect();
    }

    public boolean isGoogleApiClientConnected(){
        return googleApiClient.isConnected();
    }

}
