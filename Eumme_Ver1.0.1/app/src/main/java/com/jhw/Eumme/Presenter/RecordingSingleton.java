package com.jhw.Eumme.Presenter;

import com.jhw.Eumme.Model.memoItem;

import java.util.concurrent.ConcurrentHashMap;

public class RecordingSingleton {
    ConcurrentHashMap<Integer,memoItem> memoItemList = null;
    private volatile static RecordingSingleton ourInstance = new RecordingSingleton();

    public static synchronized RecordingSingleton getInstance() {
        if(ourInstance ==null){
            ourInstance= new RecordingSingleton();
        }
        return ourInstance;
    }
    private RecordingSingleton(){
        memoItemList = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Integer,memoItem> getMemoItemList() {
        return memoItemList;
    }

    public void addToArray(int index,memoItem newItem){
        memoItemList.put(index,newItem);
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

    public void setClear()  {

       memoItemList.clear();
    }

    public boolean check(int index){

        if(memoItemList.containsKey(index)){
            //있으면 true
            return true;
        }
        else
            //없으면 false
            return false;
    }

    public void reset(int index, String modifiedMemo){
        String term = memoItemList.get(index).getMemoTime();
        memoItem modifiedItem = new memoItem(modifiedMemo,term,index);
        memoItemList.put(index,modifiedItem);
    }

    public void resetForModify(int index, String modifiedMemo, ConcurrentHashMap<Integer,memoItem> list){
        String term = list.get(index).getMemoTime();
        memoItem modifiedItem = new memoItem(modifiedMemo,term,index);
        memoItemList.put(index,modifiedItem);
    }

}
