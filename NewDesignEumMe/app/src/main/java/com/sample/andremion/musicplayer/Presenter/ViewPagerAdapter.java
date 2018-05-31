package com.sample.andremion.musicplayer.Presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.sample.andremion.musicplayer.View.MemoFragement;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(android.support.v4.app.FragmentManager fm)
    {
        super(fm);
    }
    String tag = "adapter";
    int count=1;
    @Override
    public Fragment getItem(int position) {

            Fragment fragment = new MemoFragement().create(position);
            return fragment;
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
