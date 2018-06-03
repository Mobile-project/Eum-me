package com.sample.andremion.musicplayer.View;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.andremion.musicplayer.Model.RecordeService;
import com.sample.andremion.musicplayer.Model.RecordingMataData;
import com.sample.andremion.musicplayer.Model.memoItem;
import com.sample.andremion.musicplayer.Presenter.ListViewItem;
import com.sample.andremion.musicplayer.Presenter.PlayViewPagerAdapter;
import com.sample.andremion.musicplayer.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayActivity extends AppCompatActivity {

    String tag = "myplayactivity";
    String path = Environment.getExternalStorageDirectory().toString() + "/ZEum_me";
    int pos;//재생 멈춘 시점
    boolean isPlaying = false;
    private boolean buttonMode = false; //false= pause, true = play
    ImageButton pauseButton;
    TextView textViewFileName;
    TextView mFileLengthTextView;
    EditText editText;
    String rPlaytime;
    ViewPager viewpager;
    int realPlaytime;
    int seconds;
    int minutes;
    int hours;
    int duration;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_player);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        fileName = bundle.getString("fileName");// 파일이름 꺼냄
        playTime = bundle.getString("playTime");
        rPlaytime = playTime.replace(":", "");
        realPlaytime = Integer.parseInt(rPlaytime);
        seconds = realPlaytime % 100;
        minutes = (realPlaytime - seconds) % 100;
        hours = (realPlaytime / 10000);
        minutes = minutes + (hours * 60);
        duration = (minutes * 60) + seconds;
        //  playTime = bundle.getString(String.format("%02d:%02d", minutes,seconds));                // 플레이타임 꺼냄

        Log.d(tag, "file Fisrt : " + fileName + " playTime : " + playTime);
        textViewFileName = findViewById(R.id.file_name);
        textViewFileName.setText(fileName.toString());


        textViewFileName = findViewById(R.id.file_name);
        mFileLengthTextView = findViewById(R.id.file_length_text_view);
        mFileLengthTextView.setText(String.format("%02d:%02d", minutes, seconds));
        mCurrentProgressTextView = findViewById(R.id.current_progress_text_view);

        pauseButton = findViewById(R.id.btn_play);
        //editText = findViewById(R.id.memo_area);
        viewpager = findViewById(R.id.view_pager1);
        PlayViewPagerAdapter viewPagerAdapter = new PlayViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setCurrentItem(0);

        Log.d(tag, "onCreate and fileNAme is  : " + fileName);
        path += "/" + fileName;
        Log.d(tag, "file path : " + path);


        mediaSeekBar = findViewById(R.id.seek_bar);
        // mediaSeekBar.setMax(duration);
        ColorFilter filter = new LightingColorFilter
                (getResources().getColor(R.color.tab_strip), getResources().getColor(R.color.colorAccent1));
        mediaSeekBar.getProgressDrawable().setColorFilter(filter);
        mediaSeekBar.getThumb().setColorFilter(filter);
        mediaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                    mediaHandler.removeCallbacks(mRunnable);

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition())
                            - TimeUnit.MINUTES.toSeconds(minutes);
                    mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                    updateSeekBar();

                } else if (mediaPlayer == null && fromUser) {
                    prepareMediaPlayerFromPoint(progress);
                    updateSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    //프로그레스바 업데이트의 핸들러 메세지 제거
                    mediaHandler.removeCallbacks(mRunnable);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaHandler.removeCallbacks(mRunnable);
                    mediaPlayer.seekTo(seekBar.getProgress());

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition())
                            - TimeUnit.MINUTES.toSeconds(minutes);
                    mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));
                    updateSeekBar();
                }

            }
        });
        // 재생하는 함수 실행
        onPlay(isPlaying);
        //getMemo(fileName);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!buttonMode) {
                    pausePlaying();
                    buttonMode = true;
                    Toast.makeText(getApplicationContext(), "일시정지", Toast.LENGTH_SHORT).show();
                    Log.d(tag, "녹음 일시정지");
                } else if (buttonMode) {
                    resumePlaying();
                    buttonMode = false;
                    Toast.makeText(getApplicationContext(), "다시 재생", Toast.LENGTH_SHORT).show();
                    Log.d(tag, "녹음 다시재생");
                }
            }
        });
        mFileLengthTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    /**
     * by bigleeuk complete
     */
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

    /**
     * by bigleeuk complete
     */
    private void startPlaying() {
        isPlaying = !isPlaying;
        mediaPlayer = new MediaPlayer();
        Log.d(tag, "start Playing");
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaSeekBar.setMax(mediaPlayer.getDuration());
            //mediaSeekBar.incrementProgressBy(1);
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
    }

    /**
     * by bigleeuk complete
     */
    private void stopPlaying() {
        Log.d(tag, "stop Playing");
        mediaHandler.removeCallbacks(mRunnable);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        mediaSeekBar.setProgress(mediaSeekBar.getMax());
        isPlaying = !isPlaying;
        mCurrentProgressTextView.setText(mFileLengthTextView.getText()); // 플레이 타임
        mediaSeekBar.setProgress(mediaSeekBar.getMax());

    }

    /**
     * by bigleeuk complete
     */
    private void pausePlaying() {
        Log.d(tag, "pause Playing");
        mediaHandler.removeCallbacks(mRunnable);
        mediaPlayer.pause();
    }

    /**
     * by bigleeuk complete
     */
    private void resumePlaying() {
        Log.d(tag, "resumePlaying");
        mediaHandler.removeCallbacks(mRunnable);
        mediaPlayer.start();
        updateSeekBar();
    }

    /**
     * by bigleeuk complete
     */
    //updating mSeekBar
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {

                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                mediaSeekBar.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition) - TimeUnit.MINUTES.toSeconds(minutes);
                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    /**
     * by bigleeuk complete
     */
    public void prepareMediaPlayerFromPoint(int progress) {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaSeekBar.setMax(mediaPlayer.getDuration());
            // mediaSeekBar.incrementProgressBy();
            mediaPlayer.seekTo(progress);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });

        } catch (IOException e) {
            Log.e(tag, "prepare() failed");
        }
    }

    /**
     * by bigleeuk complete
     */
    @Override
    public void onBackPressed() {
        stopPlaying();
        Log.d(tag, "재생화면 종료");
        finish();

    }

    /**
     * by bigleeuk complete
     */
    private void updateSeekBar() {
        mediaHandler.postDelayed(mRunnable, 1000);
    }

   /* private void getMemo(String fileName) {
        RecordingMataData metaTemp = ((MainActivity) MainActivity.mContext).dbHelper.getResult(fileName);
        List<memoItem> itemList = metaTemp.getMemoItem();
        int len = itemList.size();
        Log.d(tag, "len : " + len);

        for (int i = 0; i < len; i++) {
            Log.d(tag, itemList.get(i).toString());
            editText.setText(itemList.get(i).toString() + "\n");
        }

    }*/
}