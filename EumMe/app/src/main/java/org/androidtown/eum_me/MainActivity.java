package org.androidtown.eum_me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.androidtown.eum_me.audioRecorder.recordMain;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_record; // 녹음 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         btn_record=findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),recordMain.class);
                 startActivity(intent);
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
                break;
            case R.id.menu_record:
                break;
            default:break;

        }
        return super.onOptionsItemSelected(item);
    }


}
