package com.sample.andremion.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{

    SQLiteDatabase db;

    private String tag = "DBHelper";
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(tag, "before CREATE TABLE");
        db.execSQL("CREATE TABLE RECORDINGINFO (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, playtime INTEGER);");
        Log.d(tag, "after CREATE TABLE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void insert(String name, int playTime){
        Log.d(tag, "name : " + name + " playTime : " + playTime);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO RECORDINGINFO VALUES(null, '" + name + "', " + playTime + ");");
        Log.d(tag, "after insert");
        db.close();

    }


    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM RECORDINGINFO", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getInt(2)
                    + "time "
                    + "\n";
        }

        return result;
    }

}
