package com.sample.andremion.musicplayer.activities;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.view.ListViewAdapter;
import com.sample.andremion.musicplayer.view.ListViewItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListView extends AppCompatActivity{
    private File file;
    private List myList;
    private List myListDate;

    String tag = "myListView";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        myList = new ArrayList();
        myListDate = new ArrayList();
        String rootSD = Environment.getExternalStorageDirectory().toString();
        rootSD+="/ZEum_me";
        Log.d(tag,rootSD);
        Log.d(tag,"after rootSD");

        file = new File(rootSD);
        File list[] = file.listFiles();
        Log.d(tag,"after file list[] : " + list.length);


        // 파일 이름들 추가
        for(int i=0;i<list.length;i++){
            myList.add(list[i].getName());
            myListDate.add(list[i].lastModified());
        }
        Log.d(tag,"after myList add");


        android.widget.ListView listview ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (android.widget.ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        // 아이템 추가
        for(int i=0;i<myList.size();i++){
            Log.d(tag, "rootSD : " + rootSD);
            String filename = myList.get(i).toString();
            adapter.addItem(ContextCompat.getDrawable(this,R.drawable.btn_play),
                    filename,
                    getPlayTime(rootSD+"/"+filename),
                    getCreatedTime(Long.valueOf(myListDate.get(i).toString())));

        }
        // 첫 번째 아이템 추가.
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.input),
//                "Box", "Account Box Black 36dp") ;
//        // 두 번째 아이템 추가.
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.input),
//                "Circle", "Account Circle Black 36dp") ;
//        // 세 번째 아이템 추가.
//        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.input),
//                "Ind", "Assignment Ind Black 36dp") ;



        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;
                Log.d(tag, "item click listener");
                Toast.makeText(getApplicationContext(), "item num : " + position, Toast.LENGTH_SHORT).show();

                // TODO : use item data.
            }
        }) ;



    }


    // path에 있는 파일 길이 가져오기.
    // hh:mm:ss
    private String getPlayTime(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );
        long duration = timeInmillisec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);
        return hours + ":" + minutes + ":" + seconds;
    }


    private String getCreatedTime(long date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date).toString();

    }




}
