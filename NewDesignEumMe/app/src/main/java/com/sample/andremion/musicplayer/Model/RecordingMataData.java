package com.sample.andremion.musicplayer.Model;

import java.util.List;

public class RecordingMataData {
    /*
        file_name       TEXT        파일 이름
        memo            TEXT        메모 내용
        play_time       INTEGER     몇초 짜리인지
        mamo_index      INTEGER     메모 번호
        created_time    TEXT        녹음파일 생성시간
         */
    List<memoItem> memoItem;
//    memoItem[] memoItem;
    int playTime;
    String createdTime;

    public RecordingMataData(List<com.sample.andremion.musicplayer.Model.memoItem> memoItem, int playTime, String createdTime) {
        this.memoItem = memoItem;
        this.playTime = playTime;
        this.createdTime = createdTime;
    }

    public List<com.sample.andremion.musicplayer.Model.memoItem> getMemoItem() {
        return memoItem;
    }

    public void setMemoItem(List<com.sample.andremion.musicplayer.Model.memoItem> memoItem) {
        this.memoItem = memoItem;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
