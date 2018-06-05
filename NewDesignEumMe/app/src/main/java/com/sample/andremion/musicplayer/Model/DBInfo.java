package com.sample.andremion.musicplayer.Model;

import android.provider.BaseColumns;

public final class DBInfo {
    //     _id              INTEGER     무시하는 프라이머리 키
    //     file_name        TEXT        녹음 파일 이름
    //     memo             TEXT        메모 내용
    //     memo_time        TEXT        메모한 시간
    //     memo_index       INTEGER     몇번째 메모인지

    public static final class CreateDB implements BaseColumns {
        public static final String FILE_NAME = "file_name";
        public static final String MEMO = "memo";
        public static final String MEMO_TIME = "memo_time";
        public static final String MEMO_INDEX = "memo_index";
        public static final String _TABLENAME = "RECORDINGMEMO";
        public static final String _CREATE = "" + "CREATE TABLE " + _TABLENAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + FILE_NAME + " TEXT, " + MEMO + " TEXT, " + MEMO_TIME + " TEXT, " + MEMO_INDEX + " INTEGER)";

    }
}
