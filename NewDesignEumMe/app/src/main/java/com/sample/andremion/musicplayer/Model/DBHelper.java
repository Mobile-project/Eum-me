package com.sample.andremion.musicplayer.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sample.andremion.musicplayer.Presenter.PlayViewPagerAdapter;
import com.sample.andremion.musicplayer.View.PlayActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by tamama on 2018. 5. 8..
 */

public class DBHelper {

    String tag = "mydbhelper";
    private static final String DATABASE_NAME = "RECORDINGMEMO.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        // DB를 새로 생성할 때 호출되는 함수
        // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //     _id              INTEGER     무시하는 프라이머리 키
        //     file_name        TEXT        녹음 파일 이름
        //     memo             TEXT        메모 내용
        //     memo_time        TEXT        메모한 시간
        //     memo_index       INTEGER     몇번째 메모인지
        @Override
        public void onCreate(SQLiteDatabase db) {

            // 새로운 테이블 생성
            //  String sql = "CREATE TABLE RECORDINGMEMO (_id INTEGER PRIMARY KEY AUTOINCREMENT, file_name TEXT, memo TEXT, memo_time INTEGER, memo_index INTEGER)";
            db.execSQL(DBInfo.CreateDB._CREATE);
            Log.d(tag,"디비 테이블 생성");
        }

        // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DBInfo.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public DBHelper(Context context) {
        this.mCtx = context;
    }

    public DBHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);

        return this;
    }

    public void close() {
        mDB.close();
    }

    //     _id              INTEGER     무시하는 프라이머리 키
    //     file_name        TEXT        녹음 파일 이름
    //     memo             TEXT        메모 내용
    //     memo_time        TEXT        메모한 시간
    //     memo_index       INTEGER     몇번째 메모인지
    public void insert(String file_name, String memo, String memo_time, int memo_index) {
        // 읽고 쓰기가 가능하게 DB 열기
        mDB = mDBHelper.getWritableDatabase();
        mDB.execSQL("INSERT INTO RECORDINGMEMO VALUES(null, '" + file_name + "', '" + memo + "', '" + memo_time + "', " + memo_index +")");
        Log.d(tag, "INSERT INTO RECORDINGMEMO VALUES(null, '" + file_name + "', '" + memo + "', " + memo_time + ", " + memo_index + ")");

    }

    // 이름 바꾸기
    // 테스트 필요
    public void reName(String prevName, String newName) {

        mDB = mDBHelper.getWritableDatabase();
        newName= newName+".mp4";
        String sql = "UPDATE "+DBInfo.CreateDB._TABLENAME +" SET "+DBInfo.CreateDB.FILE_NAME+" = '"+newName +"' WHERE "+DBInfo.CreateDB.FILE_NAME+" ='"+prevName+"';";
        Log.d("test",sql);
        mDB.execSQL(sql);
      //  mDB.rawQuery(sql,null);
        mDB.close();

    }

    // 파일 이름이 file_name 인 row 삭제
    public void delete(String file_name) {
        mDB = mDBHelper.getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
//        db.execSQL("DELETE FROM RECORDINGMEMO WHERE item='" + item + "';");
        mDB.execSQL(String.format("DELETE FROM %s WHERE %s = '%s'",DBInfo.CreateDB._TABLENAME,DBInfo.CreateDB.FILE_NAME,file_name));
    }


    //     _id              INTEGER     무시하는 프라이머리 키
    //     file_name        TEXT        녹음 파일 이름
    //     memo             TEXT        메모 내용
    //     memo_time        TEXT        메모한 시간
    //     memo_index       INTEGER     몇번째 메모인지

    // 파일 이름이 file_
    public RecordingMataData getResult(String fileName) {
        // 읽기가 가능하게 DB 열기
        mDB = mDBHelper.getReadableDatabase();
        String result = "";

        Log.d(tag, "get result : " + fileName);
        Log.d(tag, "SELECT * FROM RECORDINGMEMO WHERE file_name=\"" + fileName + "\"");

        Cursor cs = mDB.rawQuery("SELECT * FROM RECORDINGMEMO WHERE file_name=\"" + fileName + "\"", null);

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
        mDB.close();
        return new RecordingMataData(fileName, itemListTemp);                       // 메타데이터 객체를 리턴
    }

    // 메모만 반환하는 함수
    // 테스트 필요
    public ConcurrentHashMap<Integer, memoItem> selectMemo(String fileName) {

        ConcurrentHashMap<Integer, memoItem> memos = new ConcurrentHashMap<>();
        mDB = mDBHelper.getReadableDatabase();
        String sql = "SELECT * FROM "+DBInfo.CreateDB._TABLENAME+" WHERE "+DBInfo.CreateDB.FILE_NAME+" = '"+fileName+"';";

        Cursor cs = mDB.rawQuery(sql,null);

        while (cs.moveToNext()) {
            String memo = cs.getString(cs.getColumnIndex(DBInfo.CreateDB.MEMO));
            String memo_time  = cs.getString(cs.getColumnIndex(DBInfo.CreateDB.MEMO_TIME));
            int memo_index = cs.getInt(cs.getColumnIndex(DBInfo.CreateDB.MEMO_INDEX));
            memoItem term = new memoItem(memo,memo_time,memo_index);
            memos.put(memo_index, term);
        }
        return memos;
    }

}