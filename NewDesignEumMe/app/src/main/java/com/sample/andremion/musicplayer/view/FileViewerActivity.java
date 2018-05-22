package com.sample.andremion.musicplayer.view;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.audioControl.PlayerService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileViewerActivity extends ListActivity {
    private File file;
    private List myList;
    private PlayerService playerService;
    String tag = "FileViewerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.file_viewer);
        Log.d(tag, "in on Create");

        myList = new ArrayList();

        String rootSD = Environment.getExternalStorageDirectory().toString();
        Log.d(tag, "after rootSD");

        file = new File(rootSD + "/ZEum_me");
        File list[] = file.listFiles();
        Log.d(tag, "after file list[]");


        for (int i = 0; i < list.length; i++) {
            myList.add(list[i].getName());
            Log.d(tag, "name : " + list[i].getName());
        }
        Log.d(tag, "after myList add");

        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, myList));

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
//
//        Object o = this.getListAdapter().getItem(position);
//        String fileName = o.toString();
//        Intent intent = new Intent(getApplicationContext(),PlayerService.class);
//        intent.putExtra("name",fileName);
//        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fileview_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

