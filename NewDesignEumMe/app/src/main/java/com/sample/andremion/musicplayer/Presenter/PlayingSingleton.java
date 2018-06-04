package com.sample.andremion.musicplayer.Presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PlayingSingleton {
    ArrayList<String> loadedMemo = null;
    private volatile static PlayingSingleton ourInstance = new PlayingSingleton();

    public static synchronized PlayingSingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new PlayingSingleton();
        }
        return ourInstance;
    }

    private PlayingSingleton() {
        loadedMemo = new ArrayList<String>();
    }

    public ArrayList<String> getList(){
        return loadedMemo;
    }

    public void setList(ArrayList<String> list) {
        this.loadedMemo = list;
    }

    public String getMemo(int position){
        return loadedMemo.get(position);
    }

}
