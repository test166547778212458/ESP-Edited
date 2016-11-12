package com.example.faraz.esp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SendVoicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_voice_message);
    }

    public void back(View view) {
        finish();
    }
}

