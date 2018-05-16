package com.sample.andremion.musicplayer.audioControl;

import android.app.Service;
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


    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    boolean isRecording = false;
    int fileNameCount = 0;

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
        return super.onStartCommand(intent, flags, startId);
    }

    public String setPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path, Constants.getFolderName());

        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/audio" + Constants.getCurrentTime() + ".mp4");
    }

    public void recordInit() {
        isRecording = true;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(Constants.getAudioSource());
        mediaRecorder.setOutputFormat(Constants.getAudioFormat());
        mediaRecorder.setAudioEncoder(Constants.getAudioEncoder());
        mediaRecorder.setOutputFile(setPath());

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


}
