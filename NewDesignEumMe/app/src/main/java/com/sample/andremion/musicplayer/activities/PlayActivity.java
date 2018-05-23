package com.sample.andremion.musicplayer.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sample.andremion.musicplayer.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlayActivity extends AppCompatActivity {

    String tag = "myplayactivity";
    String path = Environment.getExternalStorageDirectory().toString() + "/ZEum_me";

    boolean isPlaying = false;

    ImageButton playButton;
    TextView textViewFileName;

    private TextView mCurrentProgressTextView = null;       // 시간 표시하는 곳
    private SeekBar mediaSeekBar = null;
    MediaPlayer mediaPlayer;

    private Handler mediaHandler = new Handler();

    private String fileName = "";

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


        fileName = bundle.getString("fileName");
        Log.d(tag, "file Fisrt : " + fileName);
        textViewFileName = findViewById(R.id.file_name);
        textViewFileName.setText(fileName.toString());

        playButton = findViewById(R.id.btn_play);
        mediaSeekBar = findViewById(R.id.seek_bar);

        Log.d(tag, "onCreate and fileNAme is  : " + fileName);
        path += "/" + fileName;
        Log.d(tag, "file path : " + path);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "play button clicked");

                // 재생하는 함수 실행
                onPlay(isPlaying);
                isPlaying = !isPlaying;
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
        playButton.setImageResource(R.drawable.btn_pause);
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
        playButton.setImageResource(R.drawable.btn_play);
        mediaHandler.removeCallbacks(mRunnable);
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


    private void pausePlaying() {
        Log.d(tag,"pause Playing");
        playButton.setImageResource(R.drawable.btn_play);
        mediaHandler.removeCallbacks(mRunnable);
        mediaPlayer.pause();
    }

    private void resumePlaying() {
        Log.d(tag, "resumePlaying");
        playButton.setImageResource(R.drawable.btn_pause);
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
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
//                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        mediaHandler.postDelayed(mRunnable, 1000);
    }
}
