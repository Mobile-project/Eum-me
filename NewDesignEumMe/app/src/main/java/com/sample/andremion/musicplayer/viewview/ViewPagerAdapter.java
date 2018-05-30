package com.sample.andremion.musicplayer.viewview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.sample.andremion.musicplayer.memoControl.MemoFragement;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(android.support.v4.app.FragmentManager fm)
    {
        super(fm);
    }
    String tag = "adapter";
    int count=1;
    @Override
    public Fragment getItem(int position) {
        if(position>0||position<Integer.MAX_VALUE){
            Fragment fragment = new MemoFragement();
            return fragment;

        }
        else
            return null;
    }
    @Override
    public int getCount() {
        Log.d(tag, "Getcount");
        return Integer.MAX_VALUE;
    }
}
