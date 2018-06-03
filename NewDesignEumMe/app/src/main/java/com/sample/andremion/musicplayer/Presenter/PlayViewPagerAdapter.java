package com.sample.andremion.musicplayer.Presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sample.andremion.musicplayer.View.PlayingFragment;

import java.util.List;

public class PlayViewPagerAdapter extends FragmentStatePagerAdapter {

    public PlayViewPagerAdapter(android.support.v4.app.FragmentManager fm)
    {
        super(fm);
       // this.list = list;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new PlayingFragment().create(position);
        return fragment;
    }
    @Override
    public int getCount() {
        return 100;
    }

}
