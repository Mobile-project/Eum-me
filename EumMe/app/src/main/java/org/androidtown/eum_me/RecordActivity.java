package org.androidtown.eum_me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by tamama on 2018. 4. 30..
 */

public class RecordActivity extends AppCompatActivity{
    ImageButton btn_book_mark;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_main);

        btn_book_mark=(ImageButton)findViewById(R.id.btn_book_mark);
        btn_book_mark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),"book", Toast.LENGTH_LONG).show();

            }
        });

    }



}
