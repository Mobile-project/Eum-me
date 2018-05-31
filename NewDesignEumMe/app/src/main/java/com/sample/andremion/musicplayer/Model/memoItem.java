package com.sample.andremion.musicplayer.Model;

public class memoItem {
    String tag = "mymemoitem";
//     * _id              INTEGER     무시하는 프라이머리 키
//     * file_name        TEXT        녹음 파일 이름
//     * memo             TEXT        메모 내용
//     * memo_time        TEXT        메모한 시간
//     * memo_index       INTEGER     몇번째 메모인지

    String memo;
    String memoTime;
    int memoIndex;


    // constructor
    public memoItem(String memo, String memoTime, int memoIndex) {
        this.memo = memo;
        this.memoTime = memoTime;
        this.memoIndex = memoIndex;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemoTime() {
        return memoTime;
    }

    public void setMemoTime(String memoTime) {
        this.memoTime = memoTime;
    }

    public int getMemoIndex() {
        return memoIndex;
    }

    public void setMemoIndex(int memoIndex) {
        this.memoIndex = memoIndex;
    }


}
