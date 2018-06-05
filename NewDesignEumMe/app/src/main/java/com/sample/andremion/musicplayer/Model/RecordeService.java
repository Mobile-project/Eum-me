package com.sample.andremion.musicplayer.Model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class RecordeService extends Service {
    public static String tag = "myrecordservice";
    Context sContext;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    boolean isRecording = false;
    int fileNameCount = 0;
    private String path;

    String currentTime;
    String newFileName;
    public RecordeService(){}



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startRecording();
        sContext = getApplicationContext();
        return super.onStartCommand(intent, flags, startId);
    }

    public String setFilePath() {
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path, Constants.getFolderName());

        if (!file.exists()) {
            file.mkdirs();
        }
        currentTime = Constants.getCurrentTime();
        return (file.getAbsolutePath() + "/audio" + currentTime + ".mp4");
    }

    public void recordInit() {
        isRecording = true;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(Constants.getAudioSource());
        mediaRecorder.setOutputFormat(Constants.getAudioFormat());
        mediaRecorder.setAudioEncoder(Constants.getAudioEncoder());
        mediaRecorder.setOutputFile(setFilePath());
        Constants.setPreFileName("audio"+currentTime+".mp4");

    }

    public void startRecording() {
        Log.d("MainActivity","녹음 시작");
        recordInit();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.d("MainActivity","녹음하는중");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder = null;
            isRecording = false;

        } else {
            mediaPlayer.stop();
            mediaPlayer = null;
        }


    }

    public String getPath() {
        return path;
    }






}
