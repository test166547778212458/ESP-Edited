package com.example.faraz.esp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import Location.LocationFinder;


public class PinActivity extends AppCompatActivity {
    private static final String TAG = IntroActivity.class.getSimpleName();
    double longitude;
    double latitude;
    EditText input;
    TextView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_screen);
        generatePin();
        input = (EditText) findViewById(R.id.editText2);
        pinView = (TextView) findViewById(R.id.textView5);
    }

    private void generatePin() {
        Random rand = new Random();
        int pin = rand.nextInt(1000);

        //int pin = (int) temp;
        TextView pinText = (TextView) findViewById(R.id.textView5);
        String pinString = String.valueOf(pin);
        pinText.setText(pinString);
    }

    public void enter(View view) {
        EditText input = (EditText) findViewById(R.id.editText2);
        TextView pinView = (TextView) findViewById(R.id.textView5);
        Log.v("input", input.getText().toString());
        Log.v("pinView", pinView.getText().toString());

        int x = Integer.valueOf(input.getText().toString());
        int y = Integer.valueOf(pinView.getText().toString());

        if (x == y) {
            startActivity(new Intent(PinActivity.this, MenuActivity.class));
            finish();
        } else {
            Toast.makeText(PinActivity.this, "Wrong Pin", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
//        try{
//            longitude = LocationFinder.getInstance().getLocation().getLongitude();
//            latitude = LocationFinder.getInstance().getLocation().getLatitude();
//            Toast.makeText(this, "Longitude : "+String.valueOf(longitude)+" Latitude : "+String.valueOf(latitude),Toast.LENGTH_LONG).show();
//        }catch(NullPointerException e){
//            System.out.println(e.getMessage());
//        }
    }
}
