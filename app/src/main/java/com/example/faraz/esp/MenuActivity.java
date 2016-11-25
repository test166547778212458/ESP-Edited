package com.example.faraz.esp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MenuActivity extends AppCompatActivity{
    private static final String TAG = MenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
    }

    public void SendCoords(View view) {
        startActivity(new Intent(MenuActivity.this, SOSActivity.class));
        //Toast.makeText(MenuActivity.this, "Sending Coords Code HERE", Toast.LENGTH_LONG).show();
    }

    public void startCall(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:900" ));
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

    public void exit(View view) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}

