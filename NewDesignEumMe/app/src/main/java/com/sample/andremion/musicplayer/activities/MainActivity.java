/*
 * Copyright (c) 2016. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample.andremion.musicplayer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.audioControl.Constants;
import com.sample.andremion.musicplayer.audioControl.RecordeService;
import com.sample.andremion.musicplayer.memoControl.onSwipeTouchListener;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private TextView title;
    private TextView counter;

    private RelativeLayout play_list;
    private EditText memoArea;

    private ImageView option;
    private ImageButton button;
    private Chronometer chronometer;
    private boolean check = false;

    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_list);
        backPressCloseHandler = new BackPressCloseHandler(this);

        option = findViewById(R.id.options);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        title = findViewById(R.id.name);
        memoArea = findViewById(R.id.memo_area);

        // Set the recycler adapter
       /* RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tracks);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(MusicContent.ITEMS));*/

       //권한 받아오기
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        //파일 개수 받아오기 다시 하기
        String rootSD = Environment.getExternalStorageDirectory().toString();
        File file = new File(rootSD + "/ZEum_me");
        File list[] = file.listFiles();
        Constants.setFileCount(list.length);
        counter = findViewById(R.id.counter);
        counter.setText(Constants.getFilecount()+" 개");

        //메모장 넘기는 부분
        play_list = (RelativeLayout) findViewById(R.id.playlist);
        play_list.setOnTouchListener(new onSwipeTouchListener(this) {
            public void onSwipeTop() {
                Toast.makeText(getApplicationContext(), "swipetop", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(getApplicationContext(), "swipeRight", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(getApplicationContext(), "swipeLeft", Toast.LENGTH_SHORT).show();
                makeNewMemo();
            }
            public void onSwipeBottom() {
                Toast.makeText(getApplicationContext(), "swipeBottom", Toast.LENGTH_SHORT).show();
            }
        });

        button = (ImageButton) findViewById(R.id.btn_record);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    Toast.makeText(getApplicationContext(), "녹음 시작", Toast.LENGTH_SHORT).show();
                    startService(new Intent(getApplicationContext(), RecordeService.class));
                    title.setText(Constants.getCurrentTime());
                    check = true;
                } else if (check) {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(getApplicationContext(), "녹음 중지", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(getApplicationContext(), RecordeService.class));
                    title.setText("Tab the timer to start recording");
                    check = false;
                }
            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListView.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public void makeNewMemo() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_out_left);
        memoArea.startAnimation(anim);
        memoArea.setText("");

        // 저장하기
    }
}
