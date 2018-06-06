package com.sample.andremion.musicplayer.Presenter;

import com.sample.andremion.musicplayer.Model.memoItem;

import java.util.ArrayList;

public class MemoSingleton {
    ArrayList<memoItem>  memoItemList = null;
    private volatile static MemoSingleton ourInstance = new MemoSingleton();

    public static synchronized MemoSingleton getInstance() {
        if(ourInstance ==null){
            ourInstance= new MemoSingleton();
        }
        return ourInstance;
    }
    private MemoSingleton(){
        memoItemList = new ArrayList<memoItem>();
    }

    public ArrayList<memoItem> getMemoItemList() {
        return memoItemList;
    }

    public void addToArray(int position,memoItem newItem){
        memoItemList.add(position,newItem);
    }

    public String getMemo(int position){
        return memoItemList.get(position).getMemo();
    }

    public int getIndex(int position){
        return memoItemList.get(position).getMemoIndex();
    }
    public String getTime(int position){
        return memoItemList.get(position).getMemoTime();
    }

}
