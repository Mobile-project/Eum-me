package com.sample.andremion.musicplayer.View;

import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sample.andremion.musicplayer.Model.memoItem;
import com.sample.andremion.musicplayer.Presenter.PlayViewPagerAdapter;
import com.sample.andremion.musicplayer.R;

import java.util.ArrayList;


public class PlayingFragment extends Fragment {
    private static int CurrentPosition;
    private static ArrayList<memoItem> txt;
    EditText editText;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


    }

    public static PlayingFragment create(int position, ArrayList<memoItem> list) {
        PlayingFragment fragment = new PlayingFragment();
        CurrentPosition = position;
        txt = list;
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_play, container, false);
        editText = view.findViewById(R.id.memo_area1);

        try {
            if (CurrentPosition == 1 && !PlayViewPagerAdapter.check) {
                PlayViewPagerAdapter.check = true;
                editText.setText(txt.get(CurrentPosition - 1).getMemo());
            } else {
                editText.setText(txt.get(CurrentPosition).getMemo());
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return view;
    }


}
