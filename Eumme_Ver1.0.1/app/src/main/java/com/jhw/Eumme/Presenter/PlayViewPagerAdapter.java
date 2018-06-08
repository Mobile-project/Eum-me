package com.jhw.Eumme.Presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jhw.Eumme.Model.memoItem;
import com.jhw.Eumme.View.PlayingFragment;

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
