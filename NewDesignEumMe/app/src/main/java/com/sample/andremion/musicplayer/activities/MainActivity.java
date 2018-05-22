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

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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


public class MainActivity extends PlayerActivity {

    private View mCoverView;
    private View mTitleView;
    private View mTimeView;
    private View mDurationView;
    private View mProgressView;
    private View mFabView;

    private TextView title;
    private ImageButton btnPlay;
    private ImageButton btnPause;
    private RelativeLayout play_list;
    private EditText memoArea;
    private BackPressCloseHandler backPressCloseHandler;
    private Chronometer chronometer;

    private ImageView option;

    private boolean check=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_list);

        mCoverView = findViewById(R.id.cover);
        mTitleView = findViewById(R.id.title);
        mTimeView = findViewById(R.id.time);
        mDurationView = findViewById(R.id.duration);
        mProgressView = findViewById(R.id.progress);
        mFabView = findViewById(R.id.fab);
        option = findViewById(R.id.options);


        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "list", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ListView.class);
                startActivity(intent);
            }
        });
        // Set the recycler adapter
       /* RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tracks);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(MusicContent.ITEMS));*/

       title=findViewById(R.id.NameText);

        chronometer=(Chronometer)findViewById(R.id.chronometer);
        chronometer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!check) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    Toast.makeText(getApplicationContext(),"녹음 시작",Toast.LENGTH_SHORT).show();
                    startService(new Intent(getApplicationContext(),RecordeService.class));
                    title.setText(Constants.getCurrentTime());
                    check=true;
                }
                else if(check){
                    chronometer.stop();
                    Toast.makeText(getApplicationContext(),"녹음 중지",Toast.LENGTH_SHORT).show();
                    stopService(new Intent(getApplicationContext(),RecordeService.class));
                    title.setText("Tab the timer to start recording");
                    check=false;
                }
            }
        });
        backPressCloseHandler= new BackPressCloseHandler(this);
    }
    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }

    public void makeNewMemo() {
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_out_left);
        memoArea.startAnimation(anim);
        memoArea.setText("");

        // 저장하기

    }
}
