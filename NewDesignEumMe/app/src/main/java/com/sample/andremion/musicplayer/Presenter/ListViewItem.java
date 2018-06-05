package com.sample.andremion.musicplayer.Presenter;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ListViewItem {
    String tag = "mylistviewitem";
    private Drawable iconDrawable ;
    private boolean isUploaded;

    private String fileName;
    private String descStr ;
    private String createdTime;
    private String playTime;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        Log.d(tag, "createdtime : " + createdTime);
        this.createdTime = createdTime;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        Log.d(tag, "playtime : " + playTime);
        this.playTime = playTime;
    }

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setFileName(String title) {
        fileName = title ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getFileName() {
        return this.fileName;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }
}