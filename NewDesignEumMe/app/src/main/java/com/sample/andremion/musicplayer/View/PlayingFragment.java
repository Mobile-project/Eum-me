package com.sample.andremion.musicplayer.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.View.RecordingFragment;

import java.util.List;


public class PlayingFragment extends Fragment {
    private static int CurrentPosition;
    private static List<String> MemoList;

    public static RecordingFragment create(int position){
        RecordingFragment fragment = new RecordingFragment();
        CurrentPosition=position;
        // MemoList = list;
        return fragment;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_play, container, false);
        EditText editText = view.findViewById(R.id.memo_area1);
        return view;
    }


}
