package com.sample.andremion.musicplayer.Presenter;

import com.sample.andremion.musicplayer.Model.memoItem;

import java.util.ArrayList;

public class RecordingSingleton {
    ArrayList<memoItem>  memoItemList = null;
    private volatile static RecordingSingleton ourInstance = new RecordingSingleton();

    public static synchronized RecordingSingleton getInstance() {
        if(ourInstance ==null){
            ourInstance= new RecordingSingleton();
        }
        return ourInstance;
    }
    private RecordingSingleton(){
        memoItemList = new ArrayList<>();
    }

    public ArrayList<memoItem> getMemoItemList() {
        return memoItemList;
    }

    public void addToArray(memoItem newItem){
        memoItemList.add(newItem);
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

    public void setClear()  { memoItemList.clear();}


}
