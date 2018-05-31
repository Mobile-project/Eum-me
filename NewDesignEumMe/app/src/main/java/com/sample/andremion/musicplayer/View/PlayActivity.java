package com.sample.andremion.musicplayer.View;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.andremion.musicplayer.Model.RecordingMataData;
import com.sample.andremion.musicplayer.Model.memoItem;
import com.sample.andremion.musicplayer.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayActivity extends AppCompatActivity {

    String tag = "myplayactivity";
    String path = Environment.getExternalStorageDirectory().toString() + "/ZEum_me";

    boolean isPlaying = false;
    private boolean buttonMode = false; //false= pause, true = play
    ImageButton pauseButton;
    TextView textViewFileName;
    EditText editText;

    private TextView mCurrentProgressTextView = null;       // 시간 표시하는 곳
    private SeekBar mediaSeekBar = null;
    MediaPlayer mediaPlayer;

    private Handler mediaHandler = new Handler();

    private String fileName = "";
    private String playTime = "";

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_player);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        fileName = bundle.getString("fileName");                // 파일이름 꺼냄
        playTime = bundle.getString("playTime");                // 플레이타임 꺼냄

        Log.d(tag, "file Fisrt : " + fileName + " playTime : " + playTime);
        textViewFileName = findViewById(R.id.file_name);
        textViewFileName.setText(fileName.toString());

        pauseButton = findViewById(R.id.btn_play);
        mediaSeekBar = findViewById(R.id.seek_bar);

        editText = findViewById(R.id.memo_area);

        Log.d(tag, "onCreate and fileNAme is  : " + fileName);
        path += "/" + fileName;
        Log.d(tag, "file path : " + path);


        // 재생하는 함수 실행
        onPlay(isPlaying);
        getMemo(fileName);
        pauseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(!buttonMode) {
                    pausePlaying();
                    buttonMode=true;
                    Toast.makeText(getApplicationContext(),"일시정지",Toast.LENGTH_SHORT).show();
                    Log.d(tag,"녹음 일시정지");
                }
                else if(buttonMode){
                    resumePlaying();
                    buttonMode=false;
                    Toast.makeText(getApplicationContext(),"다시 재생",Toast.LENGTH_SHORT).show();
                    Log.d(tag,"녹음 다시재생");
                }
            }
        });

    }

    //  재생 시작
    private void onPlay(boolean isPlaying) {
        if (!isPlaying) {
            //currently MediaPlayer is not playing audio
            if (mediaPlayer == null) {
                startPlaying(); //start from beginning
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }
        } else {
            //pause the MediaPlayer
            pausePlaying();
        }
    }


    private void startPlaying() {
        isPlaying = !isPlaying;
        mediaPlayer = new MediaPlayer();
        Log.d(tag, "start Playing");
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
//            mSeekBar.setMax(mMediaPlayer.getDuration());

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    Log.d(tag, "onPrepared");


                }
            });
        } catch (IOException e) {
            Log.e(tag, "prepare() failed");
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });

        updateSeekBar();

        //keep screen on while playing audio
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void stopPlaying() {
        Log.d(tag, "stop Playing");
        mediaHandler.removeCallbacks(mRunnable);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            mediaSeekBar.setProgress(mediaSeekBar.getMax());
            isPlaying = !isPlaying;

//        mCurrentProgressTextView.setText(mFileLengthTextView.getText()); // 플레이 타임
            mediaSeekBar.setProgress(mediaSeekBar.getMax());

            //allow the screen to turn off again once audio is finished playing
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }


    private void pausePlaying() {
        Log.d(tag, "pause Playing");
        mediaHandler.removeCallbacks(mRunnable);
        mediaPlayer.pause();
    }

    private void resumePlaying() {
        Log.d(tag, "resumePlaying");
        mediaHandler.removeCallbacks(mRunnable);
        mediaPlayer.start();
        updateSeekBar();
    }


    //updating mSeekBar
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {

                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                mediaSeekBar.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition) - TimeUnit.MINUTES.toSeconds(minutes);
//                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    @Override
    public void onBackPressed() {
        stopPlaying();
        Log.d(tag, "재생화면 종료");
        finish();
    }

    private void updateSeekBar() {
        mediaHandler.postDelayed(mRunnable, 1000);
    }

    private void getMemo(String fileName){
        RecordingMataData metaTemp = ((MainActivity)MainActivity.mContext).dbHelper.getResult(fileName);
        List<memoItem> itemList= metaTemp.getMemoItem();
        int len = itemList.size();
        Log.d(tag, "len : " + len);
        for(int i=0;i<len;i++){
            Log.d(tag, itemList.get(i).toString());
            editText.setText(itemList.get(i).toString() + "\n");
        }

    }
}
