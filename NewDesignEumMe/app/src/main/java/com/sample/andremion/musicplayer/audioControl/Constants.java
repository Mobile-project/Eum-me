package com.sample.andremion.musicplayer.audioControl;

import android.media.AudioFormat;
import android.media.MediaDrmResetException;
import android.media.MediaRecorder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Constants {

    private static String folderName = "/ZEum_me";
    private static int audioSource = MediaRecorder.AudioSource.MIC;
    private static int audioFormat = MediaRecorder.OutputFormat.THREE_GPP;
    private static int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;

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
}
