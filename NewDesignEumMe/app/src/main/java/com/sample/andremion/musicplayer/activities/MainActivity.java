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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.andremion.musicplayer.DB.DBHelper;
import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.RecordingMataData;
import com.sample.andremion.musicplayer.audioControl.Constants;
import com.sample.andremion.musicplayer.audioControl.RecordeService;
import com.sample.andremion.musicplayer.memoControl.MemoFragement;
import com.sample.andremion.musicplayer.memoItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import com.sample.andremion.musicplayer.memoItem;


public class MainActivity extends AppCompatActivity {

    String tag = "mymainactivity";

    public static Context mContext;

    private TextView title;
    private TextView counter;

    private ImageView option;
    private ImageButton button;
    private Chronometer chronometer;
    private boolean check = false;


    private int startTime;
    private int endTime;
    private int memoCount;

    private ViewPager viewPager;
    int playTime;               // 몇초짜리인지
    String createdTime;         // 녹음파일 생성시간

    private BackPressCloseHandler backPressCloseHandler;

    List<memoItem> itemList = null;
    RecordingMataData metaData = null;
    public DBHelper dbHelper = null;

    public MainActivity(){
        mContext=this;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_list);

        backPressCloseHandler = new BackPressCloseHandler(this);

        option = findViewById(R.id.options);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        title = findViewById(R.id.name);
        // DBHelper 객체 생성
        dbHelper = new DBHelper(getApplicationContext(), "RECORDINGMEMO.db", null, 1);

        itemList = new ArrayList<>();       // 아이템들 넣을 어레이


        //권한 받아오기
       /* if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }*/

        //파일 개수 받아오기 다시 하기
        String rootSD = Environment.getExternalStorageDirectory().toString();
        File file = new File(rootSD + "/ZEum_me");
        File list[] = file.listFiles();
        Constants.setFileCount(list.length);
        counter = findViewById(R.id.counter);
        counter.setText(Constants.getFilecount() + " 개");


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
                    memoCount = 1;

                    // 시작시간
                    startTime = (int) System.currentTimeMillis();
                } else if (check) {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(getApplicationContext(), "녹음 중지", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(getApplicationContext(), RecordeService.class));
                    title.setText("Tab the timer to start recording");
                    check = false;

                    // 끝난 시간
                    endTime = (int) System.currentTimeMillis();
                    playTime = (endTime - startTime) / 1000;                     // 몇초짜리인지 계산. 초 단위
                    createdTime = Constants.getCurrentTime();           // 현재시간 yyyyMMdd_HHmm

                    metaData = new RecordingMataData(itemList, playTime, createdTime);


                    /*
    file_name       TEXT        파일 이름
    memo            TEXT        메모 내용
    play_time       INTEGER     몇초 짜리인지
    mamo_index      INTEGER     메모 번호
    created_time    TEXT        녹음파일 생성시간
     */

                    // 메모 갯수만큼 돌면서 디비에 인서트
                    List<memoItem> item = metaData.getMemoItem();
                    Log.d(tag, "before insert");
                    for (int i = 0; i < memoCount - 1; i++) {
                        String fileName = item.get(i).getFileName();
                        String memo = item.get(i).getMemo();
                        int playTime = metaData.getPlayTime();
                        int memoIndex = item.get(i).getMemoIndex();
                        String createdTime = metaData.getCreatedTime();
                        Log.d(tag, "file name : " + fileName + " memo : " + memo + " play time : " + playTime + " mamo index : " + memoIndex + " created time : " + createdTime);
                        dbHelper.insert(fileName, memo, playTime, memoIndex, createdTime);
                    }
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

        //viewPager생성하고 설정하기
        viewPager=findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //SCROLL_STATE_IDLE = 0
                //SCROLL_STATE_DRAGGING = 1
                //SCROLL_STATE_SETTING=2

            }

        });
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    // 오른쪽 스와이프 했을때 새로운 메모 내용 itemList에 넣는 함수.
    public void makeNewMemo(String memo, int memoIndex) {
//        private String fileName;                // 파일 이름
//        private int playTime;                   // 몇초짜리인지
//        private String memo;                    // 메모 내용
//        private int memoIndex;                  // 몇번쨰 메모인지
//        private String createdTime;             // 언제 만든 녹음파일인지,

        String fileNameTemp = "audio" + Constants.getCurrentTime() + ".mp4";
//        fileNameTemp = fileNameTemp.substring(fileNameTemp.indexOf("/audio"));
//        Log.d(tag, "fileNameTemp : " +fileNameTemp);
        String memoTemp = memo;
        int memoIndexTemp = memoIndex;

        /*
        file_name       TEXT        파일 이름
        memo            TEXT        메모 내용
        play_time       INTEGER     몇초 짜리인지
        mamo_index      INTEGER     메모 번호
        created_time    TEXT        녹음파일 생성시간
         */

        // 새로운 메모 객체 생성
        memoItem newItem = new memoItem(fileNameTemp, memoTemp, memoIndexTemp);

        Log.d(tag, "filename : " + fileNameTemp + " memo : " + memoTemp + " memo index : " + memoIndexTemp);

        // 새로운 메모 객체 itemList에 추가
        itemList.add(newItem);
        // 저장하기
    }

    //viewpagerAdapter 설정
    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        public MyViewPagerAdapter(android.support.v4.app.FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position>0||position<Integer.MAX_VALUE){
                Fragment fragment = new MemoFragement();
                return fragment;

            }
            else
                return null;
        }


        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
    }

    public void setDateAndCreatedTime(String date, String time){


    }
}

