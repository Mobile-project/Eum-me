package com.example.android.aaaa.audioControl;

import android.media.AudioFormat;
import android.media.MediaRecorder;

public class Constants {
    private static String folderName = "/ZEum_me";
    private static int audioSource = MediaRecorder.AudioSource.MIC;
    private static int sampleRateInHz = 8000;
    private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private static int BufferElements2Rec = 1024;
    private static int BytesPerElement = 2;

    public static String getFolderName() {
        return folderName;
    }

    public static int getAudioSource() {
        return audioSource;
    }

    public static int getSampleRateInHz(){
        return sampleRateInHz;
    }

    public static int getChannelConfig(){
        return channelConfig;
    }

    public static int getAudioFormat() {
        return audioFormat;
    }

    public static int getBufferElements2Rec() {
        return BufferElements2Rec;
    }

    public static int getBytesPerElement() {
        return BytesPerElement;
    }
}
