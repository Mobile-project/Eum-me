package com.sample.andremion.musicplayer.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sample.andremion.musicplayer.R;

import java.util.ArrayList;


public class PlayingFragment extends Fragment {
    private static int CurrentPosition;
    private static ArrayList<String> txt;
    EditText editText;

    public static PlayingFragment create(int position, ArrayList<String> list) {
        PlayingFragment fragment = new PlayingFragment();
        CurrentPosition = position;
        txt=list;
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_play, container, false);
        editText = view.findViewById(R.id.memo_area1);
//        try {
//            editText.setText(PlayingSingleton.getInstance().getTxt(CurrentPosition));
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        } catch (IndexOutOfBoundsException ae) {
//            ae.printStackTrace();
//
//        }
        Log.d("test","현재 프래그먼트의 포지션은 뭐지? "+CurrentPosition);
        try{
        if(CurrentPosition==1) {
            editText.setText(txt.get(CurrentPosition - 1));
        }
        else{
            editText.setText(txt.get(CurrentPosition));
        }}
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return view;
    }


}
