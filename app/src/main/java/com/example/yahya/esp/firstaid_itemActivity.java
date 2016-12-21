package com.example.yahya.esp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class firstaid_itemActivity extends AppCompatActivity{
    private static final String TAG = firstaid_itemActivity.class.getSimpleName();

    private int item_id;

    private TextView title;
    private TextView key;
    private TextView content;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstaid_item);
        item_id = getIntent().getIntExtra("item_id",-1);

        title = (TextView) findViewById(R.id.firstaid_title);
        key = (TextView) findViewById(R.id.firstaid_key);
        content = (TextView) findViewById(R.id.firstaid_content);
        image = (ImageView) findViewById(R.id.firstaid_image);

        //make content textview scrolable
        content.setMovementMethod(new ScrollingMovementMethod());


        showContent();
    }

    //"Bleeding","Broken Bone","Burns","Choking","Heart Attack","Seizures(epilepsy)"
    private void showContent(){
        switch(item_id){
            case 1:
                title.setText(R.string.firstaid_bleeding_title);
                key.setText(R.string.firstaid_bleeding_key);
                content.setText(R.string.firstaid_bleeding_content);
                image.setImageResource(R.drawable.bleeding);
                break;
            case 2:
                title.setText(R.string.firstaid_brokenbone_title);
                key.setText(R.string.firstaid_brokenbone_key);
                content.setText(R.string.firstaid_brokenbone_content);
                image.setImageResource(R.drawable.brokenbone);
                break;
            case 3:
                title.setText(R.string.firstaid_burns_title);
                key.setText(R.string.firstaid_burns_key);
                content.setText(R.string.firstaid_burns_content);
                image.setImageResource(R.drawable.burns);
                break;
            case 4:
                title.setText(R.string.firstaid_choking_title);
                key.setText(R.string.firstaid_choking_key);
                content.setText(R.string.firstaid_choking_content);
                image.setImageResource(R.drawable.choke);
                break;
            case 5:
                title.setText(R.string.firstaid_heartattack_title);
                key.setText(R.string.firstaid_heartattack_key);
                content.setText(R.string.firstaid_heartattack_content);
                image.setImageResource(R.drawable.hearattack);
                break;
            case 6:
                title.setText(R.string.firstaid_seizures_title);
                key.setText(R.string.firstaid_seizures_key);
                content.setText(R.string.firstaid_seizures_content);
                image.setImageResource(R.drawable.seizures);
                break;
            default:

        }
    }

    public void back(View view){ finish();}
}
