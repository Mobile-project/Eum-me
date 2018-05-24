package com.sample.andremion.musicplayer;

import android.util.Log;

public class memoItem {
    String tag = "mymemoitem";
    /*
        file_name       TEXT        파일 이름
        memo            TEXT        메모 내용
        play_time       INTEGER     몇초 짜리인지
        mamo_index      INTEGER     메모 번호
        created_time    TEXT        녹음파일 생성시간
         */
    private String fileName;                // 파일 이름
    private String memo;                    // 메모 내용
//    private int playTime;                   // 몇초짜리인지
    private int memoIndex;                  // 몇번쨰 메모인지
//    private String createdTime;              // 녹음파일 생성시간

    public memoItem(String fileName, String memo, int memoIndex) {
        Log.d(tag, "constructor : " + fileName + " " + memo + " " + memoIndex);
        this.fileName = fileName;
        this.memo = memo;
//        this.playTime=playTime;
        this.memoIndex = memoIndex;
//        this.createdTime=createdTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMemo() {
        return memo;
    }


    public void setMemo(String memo) {

        this.memo = memo;
    }


    public int getMemoIndex() {
        return memoIndex;
    }

    public void setMemoIndex(int memoIndex) {
        this.memoIndex = memoIndex;
    }

}
