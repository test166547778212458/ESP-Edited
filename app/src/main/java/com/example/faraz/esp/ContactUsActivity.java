package com.example.faraz.esp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
    }

    public void back(View view) {
        finish();
    }
}


