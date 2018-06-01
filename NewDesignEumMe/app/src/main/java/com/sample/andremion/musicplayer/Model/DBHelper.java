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


//     _id              INTEGER     무시하는 프라이머리 키
//     file_name        TEXT        녹음 파일 이름
//     memo             TEXT        메모 내용
//     memo_time        TEXT        메모한 시간
//     memo_index       INTEGER     몇번째 메모인지
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(tag, "onCreate gogo");
        // 새로운 테이블 생성
        String sql = "CREATE TABLE RECORDINGMEMO (_id INTEGER PRIMARY KEY AUTOINCREMENT, file_name TEXT, memo TEXT, memo_time INTEGER, memo_index INTEGER);";
//        db.execSQL("CREATE TABLE RECORDINGMEMO (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, price INTEGER, create_at TEXT);");
        Log.d(tag, sql);
        db.execSQL(sql);

    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //씨발
    }
    //     _id              INTEGER     무시하는 프라이머리 키
    //     file_name        TEXT        녹음 파일 이름
    //     memo             TEXT        메모 내용
    //     memo_time        TEXT        메모한 시간
    //     memo_index       INTEGER     몇번째 메모인지
    public void insert(String file_name, String memo, int memo_time, int memo_index) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
//        String sql = "INSERT INTO RECORDINGMEMO VALUES(null, 'file_name', 'memo', play_time, memo_index, 'created_time');";
//        db.execSQL("INSERT INTO RECORDINGMEMO VALUES(null, '" + item + "', " + price + ", '" + create_at + "');");
        db.execSQL("INSERT INTO RECORDINGMEMO VALUES(null, '" + file_name + "', '" + memo + "', " + memo_time + ", " + memo_index +")");

        Log.d(tag, "INSERT INTO RECORDINGMEMO VALUES(null, '" + file_name + "', '" + memo + "', " + memo_time + ", " + memo_index +")");

        db.close();
    }


    // 메모 수정?
    public void update(String file_name, int memo_index, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        String sql="UPDATE RECORDINGMEMO SET memo='"+ memo +"' WHERE file_name='" + file_name +"' AND memo_index= " + memo_index;
        db.execSQL(sql);
        Log.d(tag, sql);
        db.close();
    }


    // 이름 바꾸기
    // 테스트 필요
    public void reName(String prevName, String newName){
        SQLiteDatabase db = getWritableDatabase();
        Log.d(tag, "pre : " + prevName + " new : " + newName);
        db.execSQL("UPDATE RECORDINGMEMO SET file_name='" + newName + "' WHERE file_name='" + prevName + "');");

        Log.d(tag, "UPDATE RECORDINGMEMO SET file_name='" + newName + "' WHERE file_name='" + prevName + "');");

        db.close();
    }

    // 파일 이름이 file_name 인 row 삭제
    public void delete(String file_name) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
//        db.execSQL("DELETE FROM RECORDINGMEMO WHERE item='" + item + "';");
        db.execSQL("DELETE FROM RECORDINGMEMO WHERE file_name='" + file_name + "';");
        db.close();
    }


    //     _id              INTEGER     무시하는 프라이머리 키
    //     file_name        TEXT        녹음 파일 이름
    //     memo             TEXT        메모 내용
    //     memo_time        TEXT        메모한 시간
    //     memo_index       INTEGER     몇번째 메모인지

    // 파일 이름이 file_
    public RecordingMataData getResult(String fileName) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Log.d(tag, "get result : " + fileName);
        Log.d(tag, "SELECT * FROM RECORDINGMEMO WHERE file_name=\""+fileName+"\"");

        Cursor cs = db.rawQuery("SELECT * FROM RECORDINGMEMO WHERE file_name=\""+fileName+"\"",null);

        List<memoItem> itemListTemp = new ArrayList<>();      // 메모 아이템들

        while (cs.moveToNext()) {
            String t = cs.getString(0);         // 얘는 무시
            String filename = cs.getString(1);
            String memo = cs.getString(2);
            String memotime = cs.getString(3);
            int memoidx = cs.getInt(4);


            memoItem itemTemp = new memoItem(memo, memotime, memoidx);              // 새로운 메모 아이템 생성
            itemListTemp.add(itemTemp);                                             // 새로운 메모 아이템을 리스트에 추가
        }
        cs.close();
        db.close();
        return new RecordingMataData(fileName, itemListTemp);                       // 메타데이터 객체를 리턴
    }

    // 메모만 반환하는 함수
    // 테스트 필요
    public List<String> selectMemo(){
        List<String> memos=new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cs = db.rawQuery("SELECT memo FROM RECORDINGMEMO",null);
        while(cs.moveToNext()){
            memos.add(cs.getString(0));
            Log.d(tag, "in selectMemo : " + cs.getString(0));
        }
        return memos;
    }

}