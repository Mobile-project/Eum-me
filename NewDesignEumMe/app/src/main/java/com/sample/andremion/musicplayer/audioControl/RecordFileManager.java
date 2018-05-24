package com.sample.andremion.musicplayer.audioControl;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Contract
 */
public class RecordFileManager implements Parcelable {
    private String mName; // file name
    private String mFilePath; //file path
    private String mMemo;   //file memo
    private int mId; //id in database
    private int mLength; // length of recording in seconds
    private long mTime; // date/time of the recording

    public RecordFileManager()
    {
    }

    public RecordFileManager(Parcel in) {
        mName = in.readString();
        mFilePath = in.readString();
        mId = in.readInt();
        mLength = in.readInt();
        mTime = in.readLong();
        mMemo = in.readString();
    }

    public RecordFileManager(int i, String string, String string1) {
    }

    public String getMemo() {
        return mMemo;
    }

    public void setMemo(String mMemo) { this.mMemo = mMemo; }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public static final Creator<RecordFileManager> CREATOR = new Creator<RecordFileManager>() {
        public RecordFileManager createFromParcel(Parcel in) {
            return new RecordFileManager(in);
        }

        public RecordFileManager[] newArray(int size) {
            return new RecordFileManager[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mLength);
        dest.writeLong(mTime);
        dest.writeString(mFilePath);
        dest.writeString(mName);
        dest.writeString(mMemo);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}