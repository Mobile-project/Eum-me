package jwh.com.eumme.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jhw.Eumme.ver.R;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import jwh.com.eumme.Model.DBHelper;
import jwh.com.eumme.Model.memoItem;
import jwh.com.eumme.Presenter.PlayViewPagerAdapter;

public class PlayActivity extends AppCompatActivity {

    String tag = "myplayactivity";
    String path = Environment.getExternalStorageDirectory().toString() + "/ZEum_me";
    int Cseconds, Cminutes, realCtime;
    boolean isPlaying = false;
    private static int buttonMode = 1; // 1==pause 2==resume 3==replay
    ImageButton pauseButton;
    TextView textViewFileName;
    TextView mFileLengthTextView;
    int Preposition = 0;
    String rPlaytime, CTime;
    ViewPager viewpager;

    int[] createdTime = new int[100];
    private int mCurrentPosition;
    private TextView mCurrentProgressTextView = null;       // 시간 표시하는 곳
    private SeekBar mediaSeekBar = null;
    MediaPlayer mediaPlayer;
    public DBHelper dbHelper;
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
        buttonMode = 1;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.recording_player);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        fileName = bundle.getString("fileName");// 파일이름 꺼냄
        playTime = bundle.getString("playTime");
        rPlaytime = playTime.replace(":", "");

        long realPlaytime = Integer.parseInt(rPlaytime);
        long seconds = realPlaytime % 100;
        //minutes = (realPlaytime - seconds) % 100;
        long hours = (realPlaytime / 10000);
        long minutes = (realPlaytime - (hours * 10000) - seconds) / 100;
        minutes = (hours * 3600 + minutes);

        textViewFileName = findViewById(R.id.file_name);
        mFileLengthTextView = findViewById(R.id.file_length_text_view);
        mCurrentProgressTextView = findViewById(R.id.current_progress_text_view);

        textViewFileName.setText(fileName.toString());
        mFileLengthTextView.setText(playTime.toString());

        pauseButton = findViewById(R.id.btn_play);

        dbHelper = new DBHelper(this);
        dbHelper.open();
        ConcurrentHashMap<Integer, memoItem> stringList = dbHelper.selectMemo(fileName);

        /**
         * createdTime and index set
         * by bigeeuk
         */
        for (int i = 0; i < createdTime.length; i++) {
            if (i == 0) createdTime[i] = 1;
            else if (i == 1) createdTime[i] = 2;
            else createdTime[i] = 0;
        }
        /**
         * For Seekbar , Time Set
         * by bigleeuk
         */
        for (int i = 0; i < stringList.size(); i++) {
            CTime = stringList.get(i).getMemoTime();
            realCtime = Integer.parseInt(CTime);
            realCtime = -realCtime;
            CTime = Integer.toString(realCtime);
            Cseconds = (realCtime % 60);
            Cminutes = realCtime - Cseconds;
            createdTime[i] = realCtime;
        }

        viewpager = findViewById(R.id.view_pager1);
        PlayViewPagerAdapter viewPagerAdapter = new PlayViewPagerAdapter(getSupportFragmentManager(), stringList);
        viewpager.setAdapter(viewPagerAdapter);
        /**
         *  Viewpager and SeekBar connection
         *  changing screen and moving seekbar
         *  by bigleeuk
         */
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("position", "logPosition" + position);
                /**
                 * Apply SeekBar time , According to ViewPager
                 * by bigleeuk
                 */
                if (position == 0) {
                    mediaPlayer.seekTo(0);
                } else {
                    mediaPlayer.seekTo(createdTime[position - 1] * 1000);
                    Preposition = position;
                    Log.d("mill", "mills" + createdTime[position - 1] * 1000);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Log.d(tag, "onCreate and fileNAme is  : " + fileName);
        path += "/" + fileName;
        Log.d(tag, "file path : " + path);

        mediaSeekBar = findViewById(R.id.seek_bar);
        progressSeekbar();

        // 재생하는 함수 실행
        onPlay(isPlaying);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (buttonMode == 1) {
                    pauseButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.music_player_play));
                    Toast.makeText(getApplicationContext(), "일시정지", Toast.LENGTH_SHORT).show();
                    pausePlaying();
                    buttonMode = 2;
                    Log.d(tag, "버튼 모드 " + buttonMode);
                    //pause
                } else if (buttonMode == 2) {
                    pauseButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pause));
                    Toast.makeText(getApplicationContext(), "다시 재생", Toast.LENGTH_SHORT).show();
                    resumePlaying();
                    buttonMode = 1;
                    Log.d(tag, "버튼 모드 " + buttonMode);
                    //resume
                } else if (buttonMode == 3) {
                    //replay
                    //seekbar초기화
                    startPlaying();
                    Toast.makeText(getApplicationContext(), "리플레이", Toast.LENGTH_SHORT).show();
                    pauseButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pause));
                    Log.d(tag, "버튼 모드 " + buttonMode);
                    buttonMode = 1;
                }
            }
        });
        mFileLengthTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

        final Button btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setText("수정");

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notidialog();
                pausePlaying();
            }
        });
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
                buttonMode = 3;
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
        isPlaying = !isPlaying;
        mCurrentProgressTextView.setText(mFileLengthTextView.getText()); // 플레이 타임
        mediaSeekBar.setProgress(mediaSeekBar.getMax());
        pauseButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.music_player_play));

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

                mCurrentPosition = mediaPlayer.getCurrentPosition();
                mediaSeekBar.setProgress(mCurrentPosition);
                long hours = TimeUnit.MICROSECONDS.toHours(mCurrentPosition);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition) - TimeUnit.HOURS.toMinutes(hours);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition) - TimeUnit.MINUTES.toSeconds(minutes);
                mCurrentProgressTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

                updateSeekBar();
            }
        }
    };

    /**
     * by bigleeuk complete
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isPlaying) {
            stopPlaying();
        }

        PlayViewPagerAdapter.check = false;
        PlayViewPagerAdapter.mode = false;
    }

    /**
     * by bigleeuk complete
     */
    private void updateSeekBar() {
        mediaHandler.postDelayed(mRunnable, 1000);
    }

    public void progressSeekbar() {
        ColorFilter filter = new LightingColorFilter(getResources().getColor(R.color.tab_strip), getResources().getColor(R.color.colorAccent1));
        mediaSeekBar.getProgressDrawable().setColorFilter(filter);
        mediaSeekBar.getThumb().setColorFilter(filter);
        mediaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                    mediaHandler.removeCallbacks(mRunnable);
                    long hours = TimeUnit.MICROSECONDS.toHours(mCurrentPosition);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition) - TimeUnit.HOURS.toMinutes(hours);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition()) - TimeUnit.MINUTES.toSeconds(minutes);
                    mCurrentProgressTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

                    updateSeekBar();

                } else if (mediaPlayer == null && fromUser) {
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
                    long hours = TimeUnit.MICROSECONDS.toHours(mCurrentPosition);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition) - TimeUnit.HOURS.toMinutes(hours);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition()) - TimeUnit.MINUTES.toSeconds(minutes);
                    mCurrentProgressTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                    updateSeekBar();
                }

            }
        });
    }
    public void notidialog() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("알림");
        final TextView noti = new TextView(this);
        noti.setText("수정시 메모와 녹음의 동기화가 해제됩니다.");
        noti.setTextSize(20);
        noti.setGravity(Gravity.CENTER);
        alert.setView(noti);
        alert.setCancelable(false);
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                PlayViewPagerAdapter.check = false;
                resumePlaying();
            }
        });

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                PlayViewPagerAdapter.mode = true;
                PlayViewPagerAdapter.check = false;
                Bundle bundle1 = new Bundle();
                bundle1.putString("fileName", fileName);
                bundle1.putString("playTime", playTime);
                Intent in = new Intent(PlayActivity.this, ModifyActivity.class);
                in.putExtras(bundle1);
                startActivity(in);
                finish();
            }
        });
        alert.show();
    }

}