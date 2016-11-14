package com.example.faraz.esp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SendVoicActivity extends AppCompatActivity {

    private final int SPEECH_RECOGNITION_CODE = 1;
    private EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_voice_message);

        message = (EditText) findViewById(R.id.editText);
    }

    public void speechtotextClick(View view){
        //request component from speech recognition
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //required for the intent.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //Optional


        //check if the language of the device is arabic then make reqognizer arabic otherwise english.
        String dl = Locale.getDefault().toString().charAt(0)+""+Locale.getDefault().toString().charAt(1);
        if(dl.equals("ar")){
            System.out.println("Language is arabic");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar_SA");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"قل شيئا...");
        }else{
            System.out.println("Language is English");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak something...");
        }


        try {
            //start recognizer.
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            if(dl.equals("ar")){
                Toast.makeText(getApplicationContext(),"نأسف, خاصية التعرف على الصوت غير مدعوم في جهازك.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Sorry! Speech recognition is not supported in this device.",Toast.LENGTH_SHORT).show();
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
}

