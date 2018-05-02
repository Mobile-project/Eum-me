package org.androidtown.eum_me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_record;
    ImageButton btn_book_mark;
    ImageButton btn_pause;
    recordFragment recordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordFragment=new recordFragment();
        btn_record=findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.record_fragment_containter,recordFragment).commit();
            }
        });
        btn_book_mark=(ImageButton)findViewById(R.id.btn_book_mark);
        btn_book_mark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),"book", Toast.LENGTH_LONG).show();

            }
        });

        btn_pause=findViewById(R.id.btn_pause);
        btn_pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),"pause", Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int curId = item.getItemId();
        switch(curId){
            case R.id.menu_memo:
                Toast.makeText(this,"memo", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_record:
                Toast.makeText(this,"record", Toast.LENGTH_LONG).show();
                break;
            default:break;

        }
        return super.onOptionsItemSelected(item);
    }


}
