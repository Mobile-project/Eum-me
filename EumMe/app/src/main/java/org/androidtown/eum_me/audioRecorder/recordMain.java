package org.androidtown.eum_me.audioRecorder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import org.androidtown.eum_me.R;

/**
 * Created by tamama on 2018. 4. 30..
 */

public class recordMain extends AppCompatActivity {

    private ImageButton btn_stop;
    private ImageButton btn_book_mark;
    private LinearLayout container;
    final static recordPCM recordPCM = new recordPCM();
     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
         Log.d("MainActivity","RecordMain activity start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_main);

         container = (LinearLayout) findViewById(R.id.layout_record);
         LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         inflater.inflate(R.layout.activity_wave_view, container, true);

         btn_book_mark = (ImageButton) findViewById(R.id.btn_book_mark);
         btn_stop = findViewById(R.id.btn_stop);
         Log.d("MainActivity","Button create");
        recordPCM.startRecording();

        // book mark button
        btn_book_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        /// 녹음 중지 버튼
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordPCM.stopRecording();
        }
        });
     }
}



