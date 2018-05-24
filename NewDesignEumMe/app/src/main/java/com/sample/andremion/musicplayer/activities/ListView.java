package com.sample.andremion.musicplayer.activities;

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
import java.util.ArrayList;
import java.util.List;

public class ListView extends AppCompatActivity{
    private File file;
    private List myList;

    String tag = "myListView";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        myList = new ArrayList();

        String rootSD = Environment.getExternalStorageDirectory().toString();
        rootSD+="/ZEum_me";
        Log.d(tag,rootSD);
        Log.d(tag,"after rootSD");

        file = new File(rootSD);
        File list[] = file.listFiles();
        Log.d(tag,"after file list[] : " + list.length);

        for(int i=0;i<list.length;i++){
            myList.add(list[i].getName());
        }
        Log.d(tag,"after myList add");


        android.widget.ListView listview ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (android.widget.ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        for(int i=0;i<myList.size();i++){
            adapter.addItem(ContextCompat.getDrawable(this,R.drawable.btn_play),
                    myList.get(i).toString(),String.valueOf((i+1)));

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



}
