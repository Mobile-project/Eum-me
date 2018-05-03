package org.androidtown.eum_me;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tamama on 2018. 4. 30..
 */

public class RecordActivity extends AppCompatActivity{

    ImageButton btn_stop;
    ImageButton btn_book_mark;
    LinearLayout memoFragment;

    private String folderName = "/ZEum_me";
    private String initialFileName="/audio";
    private static String preFileName;
    private String currentTime;

    TextView NewFileNameTextView;

    private String NewFileName="";

    MediaRecorder mediaRecorder; //녹음을 도와주는 객체
    MediaPlayer mediaPlayer; //재생을 위한 객체

    String path = "";

    boolean isRecording = false; //녹음중인지 아닌지

    int fileNameCount=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_main);

        startRecording();           // start recording.

        btn_book_mark=(ImageButton)findViewById(R.id.btn_book_mark);
        btn_stop =findViewById(R.id.btn_stop);

        NewFileNameTextView =findViewById(R.id.textTest);
        NewFileNameTextView.setText("steaarasdasd");
        // book mark button
        btn_book_mark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),"book", Toast.LENGTH_LONG).show();
            }
        });

        /// 녹음 중지 버튼
        btn_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               stopRecording();
            }
        });


        ///////// ///////// ///////// ///////// ///////// ///////// ///////// ///////// ///////// /////////

        memoFragment=(LinearLayout)findViewById(R.id.layout_memo_fragment);

        memoFragment.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float distance=0;
                float pressedX=0;
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        pressedX=motionEvent.getX();
                       // Toast.makeText(getApplicationContext(),"x좌표 받기",Toast.LENGTH_LONG).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        distance=pressedX-motionEvent.getX();
                        //Toast.makeText(getApplicationContext(),"거리계산",Toast.LENGTH_LONG).show();
                        break;
                }

                if(Math.abs(distance)<100){
                    return false;
                }

                if(distance>0){
                    Intent intent = new Intent(getApplicationContext(), org.androidtown.eum_me.fragment.memoFragment.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slid_out_right);
                    Toast.makeText(getApplicationContext(),"드래그로 화면 전환 됨",Toast.LENGTH_LONG).show();
                }
                finish();
                return true;
            }
        });

        ///////// ///////// ///////// ///////// ///////// ///////// ///////// ///////// ///////// /////////



    }

    public void stopRecording(){
//        Toast.makeText(getApplicationContext(),"pause", Toast.LENGTH_LONG).show();
        if(isRecording){
            mediaRecorder.stop();
            mediaRecorder = null;
            isRecording=false;

            Toast.makeText(getApplicationContext(), "녹음중지", Toast.LENGTH_LONG).show();

        } else{
//            mediaPlayer.stop();
//            mediaPlayer=null;
//            Toast.makeText(getApplicationContext(), "재생중지", Toast.LENGTH_LONG).show();
        }

        FileNameDialog customDialog = new FileNameDialog(RecordActivity.this);

        // 커스텀 다이얼로그를 호출한다.
        // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
        customDialog.callFunction(NewFileNameTextView);

        NewFileName = NewFileNameTextView.getText().toString();

        Toast.makeText(RecordActivity.this, "new name : " + NewFileName, Toast.LENGTH_LONG);
//        Log.d("newname", "newname : " + this.getNewFileName());
    }

    public void startRecording(){
//        btn_record.setEnabled(false); //두번 누를 수 있으니까
//        btn_play.setEnabled(false);
//        btn_stop.setEnabled(true);
        recordingInit();            // default setting for recording.
        isRecording = true;

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //마이크로 녹음하겠다
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); //저장파일 형식 녹음파일은 3gp로 저장
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); //인코딩 방식설정
        mediaRecorder.setOutputFile(path); //경로설정

        try {
            mediaRecorder.prepare(); //녹음을 준비함 : 지금까지의 옵션에서 문제가 발생했는지 검사함
            mediaRecorder.start();
            Toast.makeText(getApplicationContext(), "녹음시작", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void recordingInit(){
//        currentTime=System.currentTimeMillis();
        currentTime = getcurrentTime();                 // 현재 시간
        initialFileName+=currentTime;                   // 현재 시간을 파일명에
        initialFileName+=".3gp";                        // 확장자명 붙이기
        Log.d("filePre", initialFileName);
        this.setPreFileName(initialFileName);
        Log.d("filePre", preFileName);
//        Log.d("filecount", "count : " + initialFileName);
        //기기마다 경로가 다르기 때문에 외부저장소 경로를 가져온다 / 없으면 내부저장소 경로를 가져옴



        // 파일 경로 정함
        path = Environment.getExternalStorageDirectory().getAbsolutePath(); //녹음저장확장자 3gp
        path+=folderName;
        File file = new File(path);
        file.mkdirs(); //없으면 디렉토리 생성, 있으면 통과
        path+=initialFileName;

        //권한승인여부 확인후 메시지 띄워줌(둘 중 하나라도)
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(RecordActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }


        //마이크가 있는지 없는지 확인
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) { //현재 하드웨어 정보를 가져옴
//            btn_play.setEnabled(false); //활성화 / 비활성화
//            btn_stop.setEnabled(false);
//            btn_record.setEnabled(true);
        }else{
//            btn_play.setEnabled(false);
//            btn_stop.setEnabled(false);
//            btn_record.setEnabled(false);
        }
    }

    public void playRecording(){
//        btn_record.setEnabled(false);
//        btn_play.setEnabled(false);
//        btn_stop.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

            Thread thread = new Thread(new myThread());
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==1){
//                btn_record.setEnabled(true);
//                btn_play.setEnabled(true);
//                btn_stop.setEnabled(false);
            }
        }
    };

    public class myThread implements Runnable{

        @Override
        public void run() {
            while(true){
                if(!mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer=null;

                    Message msg = new Message();
                    msg.arg1=1;
                    handler.sendMessage(msg);
                    return;
                }
            }
        }
    }

    public String getcurrentTime(){
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return String.valueOf(sdf.format(day));
    }

    public void changeFileName(String preName, String newName){
        File filePre = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + folderName, preName);
        File fileNow = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + folderName, "/"+newName+".3gp");

        Log.d("filePre", "in func : " + preName);
        Log.d("filePre", "in func : " + newName);

        Log.d("filePre", "pre : " + Environment.getExternalStorageDirectory().getAbsolutePath() + folderName +preName);
        Log.d("filePre", "now : " + Environment.getExternalStorageDirectory().getAbsolutePath() + folderName +"/"+newName + ".3gp");
        if(filePre.renameTo(fileNow)){
//            Toast.makeText(this, "변경 성공", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(this, "변경 실패", Toast.LENGTH_SHORT).show();
        }

    }
    public void setNewFileName(String name){
        this.NewFileName = name;
    }

    public String getNewFileName(){
        return this.NewFileName;
    }

    public String getPreFileName(){
        return this.preFileName;
    }

    public void setPreFileName(String name){
        this.preFileName=name;
    }

}
