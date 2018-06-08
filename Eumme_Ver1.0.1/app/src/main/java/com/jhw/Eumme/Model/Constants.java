package com.jhw.Eumme.Model;

import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {
    ////////지우면 안댐 //////////
    public static String preFileName;
    ////////////////
    private static String userName;
    private static String userEmail;
    private static String userUid;

    ////////////////
    private final static String folderName = "/ZEum_me";
    private final static int audioSource = MediaRecorder.AudioSource.MIC;
    private final static int audioFormat = MediaRecorder.OutputFormat.THREE_GPP;
    private final static int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;
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

    public static String getPreFileName() {
        return preFileName;
    }

    public static void setPreFileName(String preFileName) {
        Constants.preFileName = preFileName;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Constants.userName = userName;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        Constants.userEmail = userEmail;
    }

    public static String getUserUid() {
        return userUid;
    }

    public static void setUserUid(String userUid) {
        Constants.userUid = userUid;
    }

    public static String getCurrentTime() {
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return String.valueOf(date.format(day));
    }

    public static void setFileCount(int count){
        fileCount=count;
    }
    public static int getFileCount(){
        return fileCount;
    }

    // path에 있는 파일 길이 가져오기.
    // hh:mm:ss
    public static String getPlayTime(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );
        long duration = timeInmillisec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);
        return String.format("%02d:%02d:%02d",hours, minutes, seconds);
    }

    // yyyy/MM/dd 형식으로 date 변환
    public static String dateTypeConvert(long date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date).toString();
    }

    // 파라미터 : 파일
    // 파일의 마지막 수정일을 yyyy/MM/dd 형태로 바꿔서 리턴
    public static String getCreatedTime(File file){
        return dateTypeConvert(file.lastModified());
    }




}
