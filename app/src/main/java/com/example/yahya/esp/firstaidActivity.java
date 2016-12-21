package com.example.yahya.esp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class firstaidActivity extends AppCompatActivity {
    private static final String TAG = firstaidActivity.class.getSimpleName();
    ListView lv;
    String list[] = new String[] {"Bleeding","Broken Bone","Burns","Choking","Heart Attack","Seizures(epilepsy)"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstaid);

        lv = (ListView) findViewById(R.id.lv);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        //Toast.makeText(getApplicationContext(), "Item 1", Toast.LENGTH_SHORT).show();
                        startItemActivity(1);
                        break;
                    case 1:
                        //Toast.makeText(getApplicationContext(), "Item 2", Toast.LENGTH_SHORT).show();
                        startItemActivity(2);
                        break;
                    case 2:
                        //Toast.makeText(getApplicationContext(), "Item 3", Toast.LENGTH_SHORT).show();
                        startItemActivity(3);
                        break;
                    case 3:
                        //Toast.makeText(getApplicationContext(), "Item 4", Toast.LENGTH_SHORT).show();
                        startItemActivity(4);
                        break;
                    case 4:
                        //Toast.makeText(getApplicationContext(), "Item 5", Toast.LENGTH_SHORT).show();
                        startItemActivity(5);
                        break;
                    case 5:
                        //Toast.makeText(getApplicationContext(), "Item 5", Toast.LENGTH_SHORT).show();
                        startItemActivity(6);
                        break;
                    default:
                        //Toast.makeText(getApplicationContext(), "Default", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void startItemActivity(int i){
//        Intent intent = new Intent(this, firstaid_itemActivity.class);
//        intent.putExtra("item_id", i);
//        startActivity(intent);
        startActivity(new Intent(this, firstaid_itemActivity.class).putExtra("item_id", i));
    }

    public void back(View view){ finish();}
}
