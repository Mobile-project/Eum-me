package com.sample.andremion.musicplayer.Presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sample.andremion.musicplayer.View.PlayingFragment;

import java.util.ArrayList;
import java.util.List;

public class PlayViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> list;
     public PlayViewPagerAdapter(android.support.v4.app.FragmentManager fm, ArrayList<String> stringList)
    {
        super(fm);
        list= stringList;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new PlayingFragment().create(position,list);
        return fragment;
    }
    @Override
    public int getCount() {
        return 100;
    }

}
