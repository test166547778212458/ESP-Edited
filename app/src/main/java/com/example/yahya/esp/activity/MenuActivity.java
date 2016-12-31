package com.example.yahya.esp.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.yahya.esp.R;


public class MenuActivity extends AppCompatActivity{
    private static final String TAG = MenuActivity.class.getSimpleName();

    Context context;
    BroadcastReceiver updateUIReciver;
    IntentFilter filter;

    private AlertDialog.Builder inExit_adb;
    private AlertDialog inExit_ad;

    private AlertDialog.Builder noInternet_adb;
    private AlertDialog noInternet_ad;

    private AlertDialog.Builder info_adb;
    private AlertDialog info_ad;

    Boolean ConnState = false;

    ImageView iv;
    ImageButton record_icon;
    ImageButton msg_icon;
    ImageButton sos_icon;
    ImageButton chat_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        onExit_dialog();
        onInfo_dialog();
        noInternetDialog();

        iv = (ImageView) findViewById(R.id.conn_state);
        msg_icon = (ImageButton) findViewById(R.id.msg_icon);
        record_icon = (ImageButton) findViewById(R.id.record_icon);
        sos_icon = (ImageButton) findViewById(R.id.sos_icon);
        chat_icon = (ImageButton) findViewById(R.id.chat_icon);

        if(isNetworkAvailable()){
            ConnState = true;
            iv.setImageResource(R.drawable.yes);
            msg_icon.setImageResource(R.drawable.msg_icon);
            record_icon.setImageResource(R.drawable.record_icon);
            sos_icon.setImageResource(R.drawable.sos_icon);
        }else{
            ConnState = false;
            iv.setImageResource(R.drawable.no);
            msg_icon.setImageResource(R.drawable.msg2);
            record_icon.setImageResource(R.drawable.record2);
            sos_icon.setImageResource(R.drawable.sos2);
        }

        context = this;

        filter = new IntentFilter();
        filter.addAction("service.to.activity.transfer");
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                changeView(intent.getIntExtra("state",-1));
            }

        };
//        registerReceiver(updateUIReciver, filter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(updateUIReciver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(updateUIReciver, filter);
        super.onResume();
    }

    private void onExit_dialog(){
        inExit_adb = new AlertDialog.Builder(this);
        inExit_adb.setMessage("Exit");

        //you can use db.setView(R.layout.layoutname) but it requires 21 api and above,
        //this app minimum api is 18
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.onexit_dialog, null);

        inExit_adb.setView(v);

        inExit_adb.setCancelable(false);
        inExit_adb.setPositiveButton("YES",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                exit();
            } });
        inExit_adb.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });

        inExit_ad = inExit_adb.create();
    }

    private void noInternetDialog(){
        noInternet_adb = new AlertDialog.Builder(this);
        noInternet_adb.setMessage("Speech to Text Request");

        //you can use db.setView(R.layout.layoutname) but it requires 21 api and above,
        //this app minimum api is 18
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.nointernet_dialog, null);

        noInternet_adb.setView(v);

        noInternet_adb.setCancelable(false);
        noInternet_adb.setPositiveButton("OK", null);

        noInternet_ad = noInternet_adb.create();
    }

    private void onInfo_dialog(){
        info_adb = new AlertDialog.Builder(this);
        info_adb.setMessage("About us");

        //you can use db.setView(R.layout.layoutname) but it requires 21 api and above,
        //this app minimum api is 18
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.info_dialog, null);

        info_adb.setView(v);

        info_adb.setCancelable(false);
        info_adb.setPositiveButton("OK",null);

        info_ad = info_adb.create();
    }

    public void SendCoords(View view) {
        if(!isNetworkAvailable()){
            //Toast.makeText(this,"You need Internet connection to Send.",Toast.LENGTH_SHORT).show();
            noInternet_ad.show();
            return;
        }
        startActivity(new Intent(MenuActivity.this, SOSActivity.class));
        //Toast.makeText(MenuActivity.this, "Sending Coords Code HERE", Toast.LENGTH_LONG).show();
    }

    public void startCall(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:911"));
        startActivity(intent);
    }

    public void chatLayout(View view) {
        startActivity(new Intent(this,ChatActivity.class));
    }

    public void goToMsgScrn(View view) {
        if(!isNetworkAvailable()){
            //Toast.makeText(this,"You need Internet connection to Send.",Toast.LENGTH_SHORT).show();
            noInternet_ad.show();
            return;
        }
        startActivity(new Intent(MenuActivity.this, SendMsgActivity.class));
    }

    public void goToRecScrn(View view) {
        if(!isNetworkAvailable()){
            //Toast.makeText(this,"You need Internet connection to Send.",Toast.LENGTH_SHORT).show();
            noInternet_ad.show();
            return;
        }
        startActivity(new Intent(MenuActivity.this, SendVoicActivity.class));
        //Toast.makeText(MenuActivity.this, "Record Message Code HERE", Toast.LENGTH_LONG).show();
    }

    public void goToAidScrn(View view) {
        startActivity(new Intent(MenuActivity.this, firstaidActivity.class));
        //Toast.makeText(MenuActivity.this, "Aid Tips Code HERE", Toast.LENGTH_LONG).show();
    }

    public void giveFeedback(View view){
        if(!isNetworkAvailable()){
            //Toast.makeText(this,"You need Internet connection to Send.",Toast.LENGTH_SHORT).show();
            noInternet_ad.show();
            return;
        }
        startActivity(new Intent(MenuActivity.this, feedbackActivity.class));
    }

    private void changeView(int state){
        if(state == 1 && !ConnState){
            ConnState = true;
            iv.setImageResource(R.drawable.yes);
            msg_icon.setImageResource(R.drawable.msg_icon);
            record_icon.setImageResource(R.drawable.record_icon);
            sos_icon.setImageResource(R.drawable.sos_icon);
        }else  if(state == 2 && ConnState){
            ConnState = false;
            iv.setImageResource(R.drawable.no);
            msg_icon.setImageResource(R.drawable.msg2);
            record_icon.setImageResource(R.drawable.record2);
            sos_icon.setImageResource(R.drawable.sos2);
        }else{}
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void info(View view){
        info_ad.show();
    }

    public void showExitDialog(View view){
        inExit_ad.show();
    }

    public void exit() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    public void onBackPressed() {
        inExit_ad.show();
    }

}

