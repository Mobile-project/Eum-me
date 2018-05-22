package com.sample.andremion.musicplayer.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.sample.andremion.musicplayer.activities.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       try{
           Thread.sleep(1000);
       }catch (InterruptedException e){
           e.printStackTrace();
       }

       startActivity( new Intent(this, MainActivity.class));
        finish();
    }
}
