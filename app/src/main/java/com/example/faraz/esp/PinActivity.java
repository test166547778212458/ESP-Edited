package com.example.faraz.esp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class PinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Random rand = new Random();
        int pin = (int)rand.nextFloat();
        TextView pinText = (TextView) findViewById(R.id.textView5);
        String pinString = String.valueOf(pin);
        pinText.setText(pinString);
        setContentView(R.layout.pin_screen);
    }

    public void enter(View view) {
        EditText input = (EditText) findViewById(R.id.editText2);
        TextView pinView = (TextView) findViewById(R.id.textView5);

        String userInput = input.getText().toString();
        String pin = pinView.getText().toString();

        if(userInput == pin){
            startActivity(new Intent(PinActivity.this, MenuActivity.class));
        }else {
            Toast.makeText(PinActivity.this, "Wrong Pin", Toast.LENGTH_LONG).show();
        }

    }
}
