package com.example.faraz.esp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


public class MenuActivity extends AppCompatActivity{
    private static final String TAG = MenuActivity.class.getSimpleName();

    private AlertDialog.Builder inExit_adb;
    private AlertDialog inExit_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        onExit_dialog();
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

    public void SendCoords(View view) {
        startActivity(new Intent(MenuActivity.this, SOSActivity.class));
        //Toast.makeText(MenuActivity.this, "Sending Coords Code HERE", Toast.LENGTH_LONG).show();
    }

    public void startCall(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:900"));
        startActivity(intent);
    }

    public void goToMsgScrn(View view) {
        startActivity(new Intent(MenuActivity.this, SendMsgActivity.class));
    }

    public void goToRecScrn(View view) {
        startActivity(new Intent(MenuActivity.this, SendVoicActivity.class));
        //Toast.makeText(MenuActivity.this, "Record Message Code HERE", Toast.LENGTH_LONG).show();
    }

    public void goToContScrn(View view) {
        startActivity(new Intent(MenuActivity.this, ContactUsActivity.class));
    }

    public void goToAidScrn(View view) {
        //startActivity(new Intent(MenuActivity.this, MenuActivity.class));
        Toast.makeText(MenuActivity.this, "Aid Tips Code HERE", Toast.LENGTH_LONG).show();

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

    @Override
    protected void onStart() {
        super.onStart();

    }
}

