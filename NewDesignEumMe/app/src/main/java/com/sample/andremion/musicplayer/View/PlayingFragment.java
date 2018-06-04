package com.sample.andremion.musicplayer.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sample.andremion.musicplayer.Model.memoItem;
import com.sample.andremion.musicplayer.Presenter.FlagSingleton;
import com.sample.andremion.musicplayer.Presenter.PlayingSingleton;
import com.sample.andremion.musicplayer.Presenter.RecordingSingleton;
import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.View.RecordingFragment;

import java.util.List;


public class PlayingFragment extends Fragment {
    private static int CurrentPosition;
    EditText editText;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (isVisibleToUser) {
                editText.setText(PlayingSingleton.getInstance().getMemo(CurrentPosition-2));
                Log.d("myplayingfragment",PlayingSingleton.getInstance().getMemo(CurrentPosition-2));
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PlayingFragment create(int position){
        PlayingFragment fragment = new PlayingFragment();
        CurrentPosition=position;
        return fragment;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_play, container, false);
        editText = view.findViewById(R.id.memo_area1);
        return view;
    }


}
