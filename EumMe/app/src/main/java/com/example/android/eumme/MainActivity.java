package com.example.android.eumme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.android.eumme.audioControl.recordMain;

public class MainActivity extends AppCompatActivity {

    ImageButton btn_record; // 녹음 버튼

    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        btn_record=findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),recordMain.class);
                startActivity(intent);
            }
        });

        backPressCloseHandler= new BackPressCloseHandler(this);
    }
    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int curId = item.getItemId();
        switch(curId){
            case R.id.menu_memo:
                break;
            case R.id.menu_record:
                break;
            default:break;

        }
        return super.onOptionsItemSelected(item);
    }*/
}
