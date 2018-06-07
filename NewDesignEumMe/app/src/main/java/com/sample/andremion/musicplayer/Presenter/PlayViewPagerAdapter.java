package com.sample.andremion.musicplayer.Presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sample.andremion.musicplayer.Model.memoItem;
import com.sample.andremion.musicplayer.View.PlayActivity;
import com.sample.andremion.musicplayer.View.PlayingFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayViewPagerAdapter extends FragmentStatePagerAdapter {

    public static boolean check = false;
    public static boolean mode = false;
    ConcurrentHashMap<Integer, memoItem> list;

    public PlayViewPagerAdapter(android.support.v4.app.FragmentManager fm, ConcurrentHashMap<Integer, memoItem> stringList) {
        super(fm);
        list = stringList;

    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new PlayingFragment().create(position, list);
        return fragment;
    }

    @Override
    public int getCount() {
       /* if (!mode) {
            return list.size();
        } else if (mode) {
            return 100;
        }*/
        return list.size();
    }

}