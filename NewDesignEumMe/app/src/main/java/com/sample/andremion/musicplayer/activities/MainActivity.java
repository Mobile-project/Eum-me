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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.andremion.musicplayer.DBHelper;
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

    private ImageButton btnList;
    private ImageButton btnDB;
    private EditText memoArea;

    private long startTime;
    private long endTime;
    private int playTime;

    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);

        /////////// 디비 만들기
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "RecordingInfo.db", null, 1);
        ///////////

        //
        mCoverView = findViewById(R.id.cover);
        mTitleView = findViewById(R.id.title);
        mTimeView = findViewById(R.id.time);
        mDurationView = findViewById(R.id.duration);
        mProgressView = findViewById(R.id.progress);
        mFabView = findViewById(R.id.fab);

        memoArea = findViewById(R.id.memo_area);

        startTime=0;
        endTime=0;
        playTime=0;

        fileName="";

        // Set the recycler adapter
       /* RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tracks);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(MusicContent.ITEMS));*/

       title=findViewById(R.id.NameText);

       btnPlay=findViewById(R.id.btn_play);
       btnPlay.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){

               Toast.makeText(getApplicationContext(),"녹음 시작",Toast.LENGTH_SHORT).show();
               startService(new Intent(getApplicationContext(),RecordeService.class));
               setTimer();
               fileName = Constants.getCurrentTime();
               title.setText(fileName);
               btnPlay.setEnabled(false);
               btnPause.setEnabled(true);
               Log.d("MainActivity","recordservice class ");
           }
       });

       btnPause = findViewById(R.id.btn_pause);
       btnPause.setEnabled(false);
       btnPause.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               stopService(new Intent(getApplicationContext(),RecordeService.class));
               stopTimer();
               title.setText("음메");
               btnPlay.setEnabled(true);
               btnPause.setEnabled(false);
               playTime = (int) (getEndTime() - getStartTime());
               Toast.makeText(getApplicationContext(),getEndTime() + " - " + getStartTime() +" : " + getSecond(playTime),Toast.LENGTH_SHORT).show();

               dbHelper.insert("audio" + fileName, getSecond(playTime));
           }
       });

       btnList = findViewById(R.id.btn_list);
       btnList.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(getApplicationContext(), "list", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getApplicationContext(), FileViewerActivity.class);
               startActivity(intent);

           }
       });

       btnDB = findViewById(R.id.btn_db);
       btnDB.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String temp = dbHelper.getResult();
               memoArea.setText(temp);
           }
       });


    }









    private void setTimer(){
        this.startTime=System.currentTimeMillis();
    }

    private void stopTimer(){
        this.endTime=System.currentTimeMillis();
    }

    public long getStartTime(){
        return this.startTime;
    }
    public long getEndTime(){
        return this.endTime;
    }

    public int getSecond(int sec){
        return sec/1000;
    }
}
