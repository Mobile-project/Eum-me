package org.androidtown.eum_me.audioRecorder;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.eum_me.OnSwipeTouchListener;
import org.androidtown.eum_me.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tamama on 2018. 4. 30..
 */

public class recordMain extends AppCompatActivity {

    private ImageButton btn_stop;
    private ImageButton btn_book_mark;
    private LinearLayout container;
    private TextView newFileNameTextView;

    private EditText memo_area;
    private TextView memo_name_text;

    private LinearLayout memo_container;

    private int memoCount=1; // 메모 인덱스



    //Audio record setting
    Thread recordingThread = null;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int sampleRateInHz = 8000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int BufferElements2Rec = 1024;
    private int BytesPerElement = 2;
    private AudioRecord audioRecorder = null;
    private boolean isRecording = false;

    //Save setting
    private String folderName = "/ZEum_me";
    private String newFileName = null;
    private String initialFileName = "/audio";
    private static String preFileName;
    private String path = "";
    File file;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("MainActivity", "RecordMain activity start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_main);

        memo_area = (EditText)findViewById(R.id.memo_area); // 메모하는곳
        memo_name_text=(TextView)findViewById(R.id.memo_name_text); // 메모제목
        memo_name_text.setText(memoCount++ + " th");

        memo_name_text.setOnTouchListener(new OnSwipeTouchListener(this){
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


        memo_container = (LinearLayout)findViewById(R.id.memo_container);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_memo,memo_container,true);


        container = (LinearLayout) findViewById(R.id.layout_record);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_wave_view, container, true);

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
            String preFileName = null;

            @Override
            public void onClick(View v) {
                boolean check = false;
                stopRecording();
            }
        });

    }


    public void startRecording() {

        audioRecorder = new AudioRecord(audioSource, Constants.getSampleRateInHz(),
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
        String currentTime = getCurrentTime();
        initialFileName += currentTime;
        initialFileName+= ".pcm";
        this.setPreFileName(initialFileName);
        short sData[] = new short[Constants.getBufferElements2Rec()];

        //파일 저장 경로 설정
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path += folderName;
        file = new File(path);
        file.mkdirs();
        path += initialFileName;
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(path);
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

        FileNameChange customChange = new FileNameChange(recordMain.this);
        customChange.callFunction(preFileName);

    }

    public String getCurrentTime() {
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return String.valueOf(sdf.format(day));
    }


    public void convert(){
        File PCMFile;
        File WAVFile;

        convertWAV convertWAV = new convertWAV();
        PCMFile= new File(path);
        WAVFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+folderName);
        try{
            convertWAV.rawToWave(PCMFile,WAVFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void changeFileName(String preName, String newName) {
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

    }


    public void setNewFileName(String name) {
        this.newFileName = name;
    }

    public String getNewFileName() {
        return this.newFileName;
    }

    public String getPreFileName() {
        return this.preFileName;
    }

    public void setPreFileName(String name) {
        this.preFileName = name;
    }


    public void makeNewMemo(){
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_out_left);
        memo_container.startAnimation(anim);
        memo_area.setText("");

        // 저장하기

    }


}



