package org.androidtown.eum_me.audioRecorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class recordPCM {

    //Audio record setting
    Thread recordingThread;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int sampleRateInHz = 44100;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
    private byte Data[] = new byte[bufferSizeInBytes];
    private AudioRecord audioRecorder = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);

    private boolean isRecording = false;

    //Save setting
    String filePath =null;
    File path;

    public void startRecording() {
        Log.d("MainActivity","Recodring");
        audioRecorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                String filepath = Environment.getExternalStorageDirectory().getPath();
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(filepath + "/record.pcm");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                while (isRecording) {
                    audioRecorder.read(Data, 0, Data.length);
                    try {
                        os.write(Data, 0, bufferSizeInBytes);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        recordingThread.start();
    }

    public void stopRecording() {
        if (null != audioRecorder) {
            isRecording = false;
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
            recordingThread = null;
        }
    }

    public void recordingInit(){
        currentTime =getcurrentTime();
    }

    public String getcurrentTime(){
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return String.valueOf(sdf.format(day));
    }

}
