package com.example.faraz.esp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ContactUsActivity extends AppCompatActivity {
    private static final String TAG = IntroActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void gmail(View view){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"hajcc@haj.gov.sa"});
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            Log.d(TAG,ex.getMessage());
        }
    }

    public void facebook(View view){
        Uri uri = Uri.parse("https://www.facebook.com/MinistryOfHajj");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void twitter(View view){
        Uri uri = Uri.parse("https://twitter.com/HajMinistry");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}


