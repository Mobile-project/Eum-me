package com.sample.andremion.musicplayer.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sample.andremion.musicplayer.Model.Constants;
import com.sample.andremion.musicplayer.Model.DBHelper;
import com.sample.andremion.musicplayer.Model.RecordeService;
import com.sample.andremion.musicplayer.Model.memoItem;
import com.sample.andremion.musicplayer.Presenter.BackPressCloseHandler;
import com.sample.andremion.musicplayer.Presenter.FlagSingleton;
import com.sample.andremion.musicplayer.Presenter.PlayViewPagerAdapter;
import com.sample.andremion.musicplayer.Presenter.RecordViewPagerAdapter;
import com.sample.andremion.musicplayer.Presenter.RecordingSingleton;
import com.sample.andremion.musicplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //////////구글용
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    GoogleApiClient mGoogleApiClient;

    String mUsername;
    String mPhotoUrl;
    ////////////


    String tag = "mymainactivity";

    public static Context mContext;

    private TextView title;

    private ImageView option;
    private ImageButton buttonRecord;
    private Chronometer chronometer;
    private boolean isRecording = false;

    private long startTime;
    private long actionTime;
    private int endTime;

    private ViewPager viewPager;
    private int prePositon = 0;
    private RecordViewPagerAdapter recordViewPagerAdapter = null;

    int playTime = 0;               // 몇초짜리인지
    ConcurrentHashMap<Integer,memoItem> memoItemList;

    private BackPressCloseHandler backPressCloseHandler;

    // 메모정보들
    public DBHelper dbHelper;

    public MainActivity() {
        mContext = this;
    }

       /////////////////////////////////////
    public String fileName="";          // 기본이름
    public String newFileName="";       // 바꾼이름
    /////////////////////////////////////
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_list);

        option = findViewById(R.id.options);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        title = findViewById(R.id.name);
        buttonRecord = (ImageButton) findViewById(R.id.btn_record);
        viewPager = findViewById(R.id.view_pager);
        backPressCloseHandler = new BackPressCloseHandler(this);
        /////////구글용
        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this).setMessage("Signout ?").setPositiveButton("signout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFirebaseAuth.signOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);

                        // 이전 화면으로 가버림
                        // 로그아웃댐
                        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Toast.makeText(this, "로그인이 필요합니다", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else {
            Constants.setUserName(mFirebaseUser.getDisplayName());
            Constants.setUserEmail(mFirebaseUser.getEmail());
            Constants.setUserUid(mFirebaseUser.getUid());

            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }

            // TextView usernameTextView = (TextView) findViewById(R.id.username_textview);
            // usernameTextView.setText(mUsername);

           // Toast.makeText(this, mUsername + "님 환영합니다.", Toast.LENGTH_SHORT).show();

            // ImageView photoImageView = (ImageView) findViewById(R.id.photo_imageview);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API).build();

        // DBHelper 객체 생성
        dbHelper = new DBHelper(this);
        dbHelper.open();


        //권한 받아오기
        TedPermission.with(this).setPermissionListener(permissionlistener)
//                .setRationaleMessage("구글 로그인을 하기 위해서는 주소록 접근 권한이 필요해요")
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]").setPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).check();

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    isRecording = true; //녹음중이라는 표시
                    chronometer.setBase(SystemClock.elapsedRealtime()); //타이머 설정
                    chronometer.start();
                    Toast.makeText(getApplicationContext(), "녹음 시작", Toast.LENGTH_SHORT).show();
                    startService(new Intent(getApplicationContext(), RecordeService.class));//녹음 시작
                    title.setText(Constants.getCurrentTime()); //타이틀 현재시간으로 설정
                    startTime = System.currentTimeMillis(); // 메모가 언제 만들어지는지 알기 위한 시작시간

                } else if (isRecording) {
                    // 녹음 멈춤
                    isRecording = false;
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(getApplicationContext(), "녹음 중지", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(getApplicationContext(), RecordeService.class));
                    title.setText("Tab the Button to start recording");

                    // 끝난 시간
                    endTime = (int) System.currentTimeMillis();
                    playTime = (int) (endTime - startTime) / 1000;                     // 몇초짜리인지 계산. 초 단위

                    fileName = Constants.getPreFileName();          // 기본 이름.

                    customDialog();                           // 새 파일 이름 받아오기

                }
            }
        });

        //viewPager생성하고 설정하기
        recordViewPagerAdapter = new RecordViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(recordViewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prePositon < position) {
                    actionTime = System.currentTimeMillis();
                    long createdTime = startTime - actionTime;
                    FlagSingleton.getInstance().setTime(createdTime);
                    FlagSingleton.getInstance().changeFlag(1);
                    prePositon = position;
                } else if (prePositon > position) {
                    FlagSingleton.getInstance().changeFlag(2);
                    prePositon = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        // option (...) 눌렀을떄 이벤트
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isRecording) {
                    Intent intent = new Intent(getApplicationContext(), ListView.class);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "녹음을 중지시켜주세요", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 퍼미션 허가하는 함수
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
           // Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
          //  Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    ///// 구글용
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(tag, "onConnectionFailed:" + connectionResult);
    }
    ////////////////////////


    //////////////////////////////////////////////
    ////////////// 이름 변경 다이얼로그 ///////////////
    //////////////////////////////////////////////
    public void customDialog(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("New File Name");
//        alert.setMessage("Plz, input yourname");

        final EditText name = new EditText(this);
        alert.setView(name);
        alert.setCancelable(false);

        alert.setNegativeButton("Cancle",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                PlayViewPagerAdapter.check = false;
                addMemoItemListToDB(fileName);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();


            }
        });

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                PlayViewPagerAdapter.check = true;
                newFileName = name.getText().toString();
                Log.d(tag, "new name : " + newFileName);
                nameChange(Constants.getPreFileName(), newFileName);
                addMemoItemListToDB(newFileName+".mp4");
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

                finish();
            }
        });
        alert.show();
    }


    //////////////////////////////////////////////
    //////////////// 이름 변경 /////////////////////
    //////////////////////////////////////////////
    public void nameChange(String preName, String newName){
        Log.d(tag, "in name change preName : " +preName);
        Log.d(tag, "in name change newName : " +newName);
        File filePre = new File(Constants.getFilePath()+"/", preName);
        File fileNow = new File(Constants.getFilePath()+"/", newName+".mp4");
        if(filePre.exists()){
            Log.d(tag, "exists");
        } else{
            Log.d(tag, "no exists");
        }
        if(filePre.renameTo(fileNow)){
            Toast.makeText(getApplicationContext(), "변경 성공", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(), "변경 실패", Toast.LENGTH_SHORT).show();
        }


//        dbHelper.reName(preName, newName + ".mp4");
    }

    public void addMemoItemListToDB(String filename){
        // 메모 갯수만큼 돌면서 디비에 인서트
        memoItemList = RecordingSingleton.getInstance().getMemoItemList();
        if (memoItemList == null) {

        } else {
                for (int i = 0; i < memoItemList.size(); i++) {
                    String memo = memoItemList.get(i).getMemo();
                    int memoIndex = memoItemList.get(i).getMemoIndex();
                    String createdTime = memoItemList.get(i).getMemoTime();
                    Log.d(tag, "file name : " + filename + " memo : " + memo + " play time : " + playTime + " memo index : " + memoIndex + " created time : " + createdTime);
                    dbHelper.insert(filename, memo, createdTime, memoIndex);
                }
        }

    }
}
