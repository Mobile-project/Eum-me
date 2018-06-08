package jwh.com.eumme.Presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.concurrent.ConcurrentHashMap;

import jwh.com.eumme.Model.memoItem;
import jwh.com.eumme.View.PlayingFragment;

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
