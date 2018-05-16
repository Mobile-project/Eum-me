package com.sample.andremion.musicplayer.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileViewerActivity extends ListActivity {
    private File file;
    private List myList;
    String tag = "FileViewerActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.file_viewer);
        Log.d(tag,"on Create@@@");


        myList = new ArrayList();

        String rootSD = Environment.getExternalStorageDirectory().toString();
        Log.d(tag,"after rootSD");

        file = new File(rootSD + "/ZEum_me");
        File list[] = file.listFiles();
        Log.d(tag,"after file list[]");


        for(int i=0;i<list.length;i++){
            myList.add(list[i].getName());
        }
        Log.d(tag,"after myList add");

        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,myList));

    }

    protected void onListItemClick(ListView l, View v,int position, long id){
        super.onListItemClick(l,v,position,id);

        File temp_file = new File(file, (String) myList.get(position));

        if(!temp_file.isFile()){
            file = new File(file, (String) myList.get(position));
            File list[] = file.listFiles();

            myList.clear();

            for(int i=0;i<list.length;i++){
                myList.add(list[i].getName());
            }
            Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_SHORT).show();
            setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,myList));
        }
    }
}
