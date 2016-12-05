package com.example.faraz.esp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import Location.LocationFinder;
import connection.RequestQueueSingleton;


public class SOSActivity extends Activity{
    private static final String TAG = SOSActivity.class.getSimpleName();
    double latitude;
    double longitude;

    private ProgressDialog pDialog;
    private AlertDialog.Builder noInternet_adb;
    private AlertDialog.Builder noText_adb;
    private AlertDialog.Builder failed_adb;
    private AlertDialog.Builder succeed_adb;
    private AlertDialog noInternet_ad;
    private AlertDialog noText_ad;
    private AlertDialog failed_ad;
    private AlertDialog succeed_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_layout);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Sending...");
        pDialog.setCancelable(false);

        noInternetDialog();
        noTextDialog();
        failedDialog();
        succeedDialog();
    }

    private void noInternetDialog(){
        noInternet_adb = new AlertDialog.Builder(this);
        noInternet_adb.setMessage("SOS Request");

        //you can use db.setView(R.layout.nointernet_dialog) but it requires 21 api and above,
        //this app minimum api is 18
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.nointernet_dialog, null);

        noInternet_adb.setView(v);

        noInternet_adb.setCancelable(false);
        noInternet_adb.setPositiveButton("OK", null);

        noInternet_ad = noInternet_adb.create();
    }

    private void noTextDialog(){
        noText_adb = new AlertDialog.Builder(this);
        noText_adb.setMessage("SOS Request");

        //you can use db.setView(R.layout.nointernet_dialog) but it requires 21 api and above,
        //this app minimum api is 18
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.notext_dialog, null);

        noText_adb.setView(v);

        noText_adb.setCancelable(false);
        noText_adb.setPositiveButton("OK", null);

        noText_ad = noText_adb.create();
    }

    private void failedDialog(){
        failed_adb = new AlertDialog.Builder(this);
        failed_adb.setMessage("SOS Request");

        //you can use db.setView(R.layout.nointernet_dialog) but it requires 21 api and above,
        //this app minimum api is 18
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.failed_dialog, null);

        failed_adb.setView(v);

        failed_adb.setCancelable(false);
        failed_adb.setPositiveButton("OK", null);

        failed_ad = failed_adb.create();
    }

    private void succeedDialog(){
        succeed_adb = new AlertDialog.Builder(this);
        succeed_adb.setMessage("SOS Request");

        //you can use db.setView(R.layout.nointernet_dialog) but it requires 21 api and above,
        //this app minimum api is 18
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.succeed_dialog, null);

        succeed_adb.setView(v);

        succeed_adb.setCancelable(false);
        succeed_adb.setPositiveButton("OK", null);

        succeed_ad = succeed_adb.create();
    }

    public void send(View view){
        if(!isNetworkAvailable()){
            //Toast.makeText(this,"You need Internet connection to Send.",Toast.LENGTH_SHORT).show();
            noInternet_ad.show();
            return;
        }

        try {
            longitude = LocationFinder.getInstance().getLocation().getLongitude();
            latitude = LocationFinder.getInstance().getLocation().getLatitude();
        }catch(NullPointerException e){
            Log.d(TAG, e.getMessage());
            Toast.makeText(this,"Cannot find your location. Please restart the application",Toast.LENGTH_SHORT).show();
            return;
        }

        sendDataToServer(2);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void sendDataToServer(final int op){
        showpDialog();

        String url = getString(R.string.server)+getString(R.string.path)+"?op="+op+"&lat="+latitude+"&lon="+longitude;

        Log.d("URL",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse",response);
                        hidepDialog();
                        succeed_ad.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidepDialog();
                failed_ad.show();
            }
        });

        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance().addToRequestQueue(stringRequest);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
