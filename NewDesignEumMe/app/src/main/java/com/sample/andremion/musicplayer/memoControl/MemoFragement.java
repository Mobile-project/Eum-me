package com.sample.andremion.musicplayer.memoControl;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.audioControl.Constants;


public class MemoFragement extends Fragment {

    public MemoFragement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_memo, container, false);
    }

}
