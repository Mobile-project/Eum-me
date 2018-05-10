package com.example.android.eumme.audioControl;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.eumme.R;
import com.example.android.eumme.memoControl.onSwipeTouchListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class recordMain extends AppCompatActivity{
    private ImageButton btn_stop;
    private ImageButton btn_book_mark;

    private EditText memo_area;
    private TextView memo_name_text;

    private LinearLayout memo_container;

    private int memoCount=1; // 메모 인덱스



    //Audio record setting
    Thread recordingThread = null;
    private AudioRecord audioRecorder = null;
    private boolean isRecording = false;

    //Save setting
    private String folderName = "/ZEum_me";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("MainActivity", "RecordMain activity start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_main);

        memo_area = (EditText)findViewById(R.id.memo_area); // 메모하는곳
        memo_name_text=(TextView)findViewById(R.id.memo_name_text); // 메모제목
        memo_name_text.setText(memoCount++ + " th");

        memo_name_text.setOnTouchListener(new onSwipeTouchListener(this){
            public void onSwipeTop(){
            }
            public void onSwipeRight(){
                memo_name_text.setText(--memoCount + " th");

            }
            public void onSwipeLeft(){
                memo_name_text.setText(memoCount++ + " th");
                makeNewMemo();

            }
            public void onSwipeBottom(){
            }
        });


        btn_book_mark = (ImageButton) findViewById(R.id.btn_book_mark);
        btn_stop = findViewById(R.id.btn_stop);
        startRecording();

        // book mark button
        btn_book_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        /// 녹음 중지 버튼
        btn_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean check = false;
                stopRecording();
                Toast.makeText(getApplicationContext(),"녹음이 중지 되었습니다",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startRecording() {

        audioRecorder = new AudioRecord(Constants.getAudioSource(), Constants.getSampleRateInHz(),
                Constants.getChannelConfig(), Constants.getAudioFormat(),
                Constants.getBufferElements2Rec() * Constants.getBytesPerElement());
        audioRecorder.startRecording();
        isRecording = true;

        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];

        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00ff);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }

    private void writeAudioDataToFile() {

        //파일이름 설정
//        String currentTime = getCurrentTime();
//        initialFileName += currentTime;
//        initialFileName+= ".pcm";
//        this.setPreFileName(initialFileName);
        short sData[] = new short[Constants.getBufferElements2Rec()];

        //파일 저장 경로 설정
//        path = Environment.getExternalStorageDirectory().getAbsolutePath();
//        path += folderName;
//        file = new File(path);
//        file.mkdirs();
//        path += initialFileName;
        String fileName = getPCMFileName();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            audioRecorder.read(sData, 0, Constants.getBufferElements2Rec());
            try {
                byte bData[] = short2byte(sData);
                os.write(bData, 0, Constants.getBufferElements2Rec() * Constants.getBytesPerElement());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording()  {
        if (null != audioRecorder) {
            isRecording = false;
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
            recordingThread = null;
        }

        // FileNameChange customChange = new FileNameChange(recordMain.this);
        //customChange.callFunction(preFileName);
        File PCMFile;
        File WAVFile;

        convertWAV convertWAV = new convertWAV();
        PCMFile= new File(getPCMFileName());
        WAVFile = new File(getWAVFileName());
        try{
            Log.d("MainActivity","파일 확장자 변경 시도 ");
            convertWAV.rawToWave(PCMFile,WAVFile);
            Log.d("MainActivity","파일 확장자 변경 성공 ");
        }catch (IOException e){
            e.printStackTrace();
        }
        deletePCMFile();
    }

    public String getCurrentTime() {
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault());
        return String.valueOf(sdf.format(day));
    }

   /* public void changeFileName(String preName, String newName) {
        File beforeFileName;
        File afterFileName;

        beforeFileName=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+folderName,preName);
        afterFileName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + folderName, "/" + newName +".pcm");

        if(beforeFileName.renameTo(afterFileName)){
            Log.d("MainActivity","파일 이름 변경 성공");
        }else
        {
            Log.d("MainActivity","파일 이름 변경 실패 ");
        }

    }*/

    private String getWAVFileName(){
        String filePath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filePath,folderName);

        if(!file.exists()){
            file.mkdirs();
        }
        return (file.getAbsolutePath()+"/audio"+getCurrentTime()+".wav");
    }

    private String getPCMFileName(){
        String filePath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filePath,folderName);

        if(!file.exists()){
            file.mkdirs();
        }

        File PCMfile = new File(filePath,getCurrentTime()+".pcm");
        if(PCMfile.exists()){
            PCMfile.delete();
        }
        return (file.getAbsolutePath()+"/audio"+getCurrentTime()+".pcm");
    }

    private void deletePCMFile(){
        File file = new File(getPCMFileName());
        file.delete();
    }

/*
    public void setNewFileName(String name) {
        this.newFileName = name;
    }
*/

  /*  public String getNewFileName() {
        return this.newFileName;
    }

    public String getPreFileName() {
        return this.preFileName;
    }

    public void setPreFileName(String name) {
        this.preFileName = name;
    }*/

    public void makeNewMemo(){
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_out_left);
        memo_container.startAnimation(anim);
        memo_area.setText("");

        // 저장하기

    }
}
