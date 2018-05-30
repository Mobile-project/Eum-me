package com.sample.andremion.musicplayer.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tamama on 2018. 5. 8..
 */

public class DBHelper extends SQLiteOpenHelper {

    String tag = "mydbhelper";

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(tag, "onCreate gogo");
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        String sql = "CREATE TABLE RECORDINGMEMO (_id INTEGER PRIMARY KEY AUTOINCREMENT, file_name TEXT, memo TEXT, play_time INTEGER, memo_index INTEGER, created_time TEXT);";
//        db.execSQL("CREATE TABLE RECORDINGMEMO (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, price INTEGER, create_at TEXT);");
        Log.d(tag, sql);
        db.execSQL(sql);

    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /*
    file_name       TEXT        파일 이름
    memo            TEXT        메모 내용
    play_time       INTEGER     몇초 짜리인지
    mamo_index      INTEGER     메모 번호
    created_time    TEXT        녹음파일 생성시간
     */
    public void insert(String file_name, String memo, int play_time, int memo_index, String created_time) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
//        String sql = "INSERT INTO RECORDINGMEMO VALUES(null, 'file_name', 'memo', play_time, memo_index, 'created_time');";
//        db.execSQL("INSERT INTO RECORDINGMEMO VALUES(null, '" + item + "', " + price + ", '" + create_at + "');");
        db.execSQL("INSERT INTO RECORDINGMEMO VALUES(null, '" + file_name + "', '" + memo + "', " + play_time + ", " + memo_index +", '" + created_time + "' )");
        Log.d(tag, "INSERT INTO RECORDINGMEMO VALUES(null, '" + file_name + "', '" + memo + "', " + play_time + ", " + memo_index +", '" + created_time + "' )");
        db.close();
    }

    public void update(String file_name, int memo_index, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
//        db.execSQL("UPDATE RECORDINGMEMO SET price=" + price + " WHERE item='" + item + "';");
        db.execSQL("UPDATE RECORDINGMEMO SET memo='"+ memo +"' WHERE file_name='" + file_name +"' AND memo_index= " + memo_index + ";");
        Log.d(tag, "UPDATE RECORDINGMEMO SET memo='"+ memo +"' WHERE file_name='" + file_name +"' AND memo_index= " + memo_index + ";");
        db.close();
    }

    public void delete(String file_name) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
//        db.execSQL("DELETE FROM RECORDINGMEMO WHERE item='" + item + "';");
        db.execSQL("DELETE FROM RECORDINGMEMO WHERE file_name='" + file_name + "';");
        db.close();
    }


    /*
    file_name       TEXT        파일 이름
    memo            TEXT        메모 내용
    play_time       INTEGER     몇초 짜리인지
    mamo_index      INTEGER     메모 번호
    created_time    TEXT        녹음파일 생성시간
     */


    public RecordingMataData getResult(String file_Name) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Log.d(tag, "get result : " + file_Name);
//        Cursor cursor = db.rawQuery("SELECT * FROM MONEYBOOK", null);
//        java.lang.String sql = "SELECT * FROM RECORDINGMEMO WHERE file_name = '"+file_Name+"'";
        Cursor cs = db.rawQuery("SELECT * FROM RECORDINGMEMO WHERE file_name=\""+file_Name+"\"",null);
        Log.d(tag, "SELECT * FROM RECORDINGMEMO WHERE file_name=\""+file_Name+"\"");

//        Cursor cs = db.rawQuery("SELECT * FROM RECORDINGMEMO", null);
//        Log.d(tag, "SELECT * FROM RECORDINGMEMO WHERE file_name='" + file_Name + "'");


        List<memoItem> itemListTemp=new ArrayList<>();
        int playTime=0;
        String createdTime="";

        while(cs.moveToNext()){
            String t = cs.getString(0);
            String fileName = cs.getString(1);
            String memo = cs.getString(2);
            playTime = cs.getInt(3);
            int memoIndex = cs.getInt(4);
            createdTime = cs.getString(5);
            Log.d(tag, "file name : " + fileName + " memo : " + memo + " play time : " + playTime + " mamo index : " + memoIndex + " created time : " + createdTime);

                memoItem itemTemp = new memoItem(fileName, memo, memoIndex);

                itemListTemp.add(itemTemp);


        }
        cs.close();

        RecordingMataData ret = new RecordingMataData(itemListTemp, playTime, createdTime);
        return ret;
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
//        Cursor cursor = db.rawQuery("SELECT * FROM RECORDINGMEMO", null);
//        while (cursor.moveToNext()) {
//            result += cursor.getString(0)
//                    + " : "
//                    + cursor.getString(1)
//                    + " | "
//                    + cursor.getInt(2)
//                    + "원 "
//                    + cursor.getString(3)
//                    + "\n";
//        }
//
//        return result;
    }

     /*
    file_name       TEXT        파일 이름
    memo            TEXT        메모 내용
    play_time       INTEGER     몇초 짜리인지
    mamo_index      INTEGER     메모 번호
    created_time    TEXT        녹음파일 생성시간
     */

    public String getCreatedTime(String fileName){
        String ret="";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM RECORDINGMEMO WHERE file_name=\""+fileName+"\"",null);
        while(cs.moveToNext()){
            String t = cs.getString(0);
            String fn = cs.getString(1);
            String m = cs.getString(2);
            int a = cs.getInt(3);
            int memoIndex = cs.getInt(4);
            ret = cs.getString(5);
        }
        Log.d(tag, "getCreatedTime : " + fileName + " and " + ret);

        db.close();

        return ret;

    }


     /*
    file_name       TEXT        파일 이름
    memo            TEXT        메모 내용
    play_time       INTEGER     몇초 짜리인지
    mamo_index      INTEGER     메모 번호
    created_time    TEXT        녹음파일 생성시간
     */

    public int getPlayTime(String fileName){
        int ret=0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM RECORDINGMEMO WHERE file_name=\""+fileName+"\"",null);
        while(cs.moveToNext()){
            String t = cs.getString(0);
            String fn = cs.getString(1);
            String m = cs.getString(2);
            ret = cs.getInt(3);
            int memoIndex = cs.getInt(4);
            String createdTime = cs.getString(5);
        }
        Log.d(tag, "getPlayTime : " + fileName + " and " + ret);

        db.close();

        return ret;
    }
    public void cleanDB(){

    }
}

