package org.androidtown.eum_me.audioRecorder;

import android.media.AudioFormat;

public class Constants {
    private static int sampleRateInHz = 8000;
    private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private static int BufferElements2Rec = 1024;
    private static int BytesPerElement = 2;

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
