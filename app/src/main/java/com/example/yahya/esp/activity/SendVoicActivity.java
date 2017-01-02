package com.example.yahya.esp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yahya.esp.R;

import com.example.yahya.esp.db.config;
import com.example.yahya.esp.locationpkg.LocationFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SendVoicActivity extends AppCompatActivity {
    private static final String TAG = SendVoicActivity.class.getSimpleName();
    double longitude;
    double latitude;

    private final int SPEECH_RECOGNITION_CODE = 1;
    private EditText message;

    private ProgressDialog pDialog;
    private AlertDialog.Builder noInternet_adb;
    private AlertDialog.Builder noText_adb;
    private AlertDialog.Builder failed_adb;
    private AlertDialog.Builder succeed_adb;
    private AlertDialog noInternet_ad;
    private AlertDialog noText_ad;
    private AlertDialog failed_ad;
    private AlertDialog succeed_ad;

    String url;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_voice_message);

        sharedPreferences = getSharedPreferences(config.SHARED_PREF,0);

        url = getString(R.string.server)+getString(R.string.path_1);

        message = (EditText) findViewById(R.id.editText);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        noInternetDialog();
        noTextDialog();
        failedDialog();
        succeedDialog();
    }

    private void noInternetDialog(){
        noInternet_adb = new AlertDialog.Builder(this);
        noInternet_adb.setMessage("Speech to Text");

        //you can use db.setView(R.layout.layoutname) but it requires 21 api and above,
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
        noText_adb.setMessage("Speech to Text");

        //you can use db.setView(R.layout.layoutname) but it requires 21 api and above,
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
        failed_adb.setMessage("Speech to Text");

        //you can use db.setView(R.layout.layoutname) but it requires 21 api and above,
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
        succeed_adb.setMessage("Speech to Text");

        //you can use db.setView(R.layout.layoutname) but it requires 21 api and above,
        //this app minimum api is 18
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.succeed_dialog, null);

        succeed_adb.setView(v);

        succeed_adb.setCancelable(false);
        succeed_adb.setPositiveButton("OK", null);

        succeed_ad = succeed_adb.create();
    }

    public void speechtotextClick(View view){
        if(!isNetworkAvailable()){
            noInternet_ad.show();
            //Toast.makeText(this,"You need Internet connection to use this function.",Toast.LENGTH_SHORT).show();
            return;
        }
        //request component from speech recognition
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //required for the intent.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //check if the language of the device is arabic then make reqognizer arabic otherwise english.
        String dl = Locale.getDefault().toString().charAt(0)+""+Locale.getDefault().toString().charAt(1);
        if(dl.equals("ar")){
            System.out.println("Language is arabic");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar_SA");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"قل شيئا...");
        }else {
            System.out.println("Language is English");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");
        }
        try {
            //start recognizer.
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            if(dl.equals("ar")){
                Toast.makeText(getApplicationContext(),"نأسف, خاصية التعرف على الصوت غير مدعوم في جهازك.",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Sorry! Speech recognition is not supported in this device.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != intent) {
                    ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    message.setText(message.getText().toString()+text);
                }
                break;
            }
        }
    }

    public void back(View view) {
        finish();
    }


    public void send(View view){
        if(!isNetworkAvailable()){
            //Toast.makeText(this,"You need Internet connection to Send.",Toast.LENGTH_SHORT).show();
            noInternet_ad.show();
            return;
        }

        if(message.getText().toString().length() == 0){
            //Toast.makeText(this,"Please click on mic to convert your voice to text",Toast.LENGTH_SHORT).show();
            noText_ad.show();
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

        String regId = sharedPreferences.getString(config.tokenId,"");
        if(TextUtils.isEmpty(regId)){
            Toast.makeText(this,"You cannot communicate with the server",Toast.LENGTH_SHORT).show();
            return;
        }

        sendDataToServer(message.getText().toString(), regId, 1);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void sendDataToServer(final String msg, final String regId,final int op){
        showpDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                        message.setText("");
                        hidepDialog();
                        succeed_ad.show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                        hidepDialog();
                        failed_ad.show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("op", String.valueOf(op));
                params.put("lat", String.valueOf(latitude));
                params.put("lon", String.valueOf(longitude));
                params.put("msg", msg);
                params.put("regId", regId);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        LocationFinder.getInstance().getRequestQueueSingleton().addToRequestQueue(postRequest);
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

