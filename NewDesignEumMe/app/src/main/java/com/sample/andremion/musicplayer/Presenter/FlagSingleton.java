package com.sample.andremion.musicplayer.Presenter;

import static java.lang.Math.abs;

public class FlagSingleton {
    private boolean flag= false;
    private long time = 0;
    private volatile static  FlagSingleton ourInstance = new FlagSingleton();

    public static synchronized FlagSingleton getInstance() {
        if(ourInstance == null){
            ourInstance= new FlagSingleton();
        }
        return ourInstance;
    }

    private FlagSingleton() {
        flag = false;
        time = 0;
    }

    public void setTime(long createdTime){
        time=createdTime;
    }
    public boolean getFlag(){
        return flag;
    }

    public String getTime(){

    long duration = time / 1000;
    long hours = duration / 3600;
    long minutes = (duration - hours * 3600) / 60;
    long seconds = abs(duration - (hours * 3600 + minutes * 60));
    return hours + ":" + minutes + ":" + seconds;
    }
    public void changeFlag(int mode){
        if(mode==1) flag = true;
        else if(mode==2) flag = false;
    }
}
