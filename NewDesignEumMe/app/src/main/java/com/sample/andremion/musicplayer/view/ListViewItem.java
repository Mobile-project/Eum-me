package com.sample.andremion.musicplayer.view;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String fileName;
    private String descStr ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setFileName(String title) {
        fileName = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }


    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getFileName() {
        return this.fileName;
    }
    public String getDesc() {
        return this.descStr ;
    }
}