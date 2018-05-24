package com.sample.andremion.musicplayer.audioControl;

import android.media.MediaRecorder;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {

    private static String folderName = "/ZEum_me";
    private static int audioSource = MediaRecorder.AudioSource.MIC;
    private static int audioFormat = MediaRecorder.OutputFormat.THREE_GPP;
    private static int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;
    private  static int fileCount = 0;
    private static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ZEum_me";

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        Constants.filePath = filePath;
    }

    public static String getFolderName() {
        return folderName;
    }

    public static int getAudioSource() {
        return audioSource;
    }

    public static int getAudioFormat() {
        return audioFormat;
    }

    public static int getAudioEncoder() {
        return audioEncoder;
    }

    public static String getCurrentTime() {
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault());
        return String.valueOf(date.format(day));
    }


    public static void setFileCount(int count){
        fileCount=count;
    }
    public static int getFilecount(){
        return fileCount;
    }



}
