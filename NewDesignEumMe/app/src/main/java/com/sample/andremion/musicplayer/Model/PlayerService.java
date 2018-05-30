package com.sample.andremion.musicplayer.Model;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

public class PlayerService extends AppCompatActivity{

    private MediaPlayer mediaPlayer;
    private String fileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        fileName = intent.getExtras().getString("name");
        PlayerInit(fileName);
    }

    public void PlayerInit(String fileName){
        String FilePath= Environment.getExternalStorageDirectory().getAbsolutePath();
        FilePath +="/ZEum_me"+fileName;

        mediaPlayer=new MediaPlayer();
        try{
            mediaPlayer.setDataSource(FilePath);
            mediaPlayer.prepareAsync();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}