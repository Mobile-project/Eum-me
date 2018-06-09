package jwh.com.eumme.Presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import jwh.com.eumme.View.RecordingFragment;

public class RecordViewPagerAdapter extends FragmentStatePagerAdapter {
    public RecordViewPagerAdapter(android.support.v4.app.FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new RecordingFragment().create(position);
        return fragment;
    }
    @Override
    public int getCount() {
        return 30;
    }

}
