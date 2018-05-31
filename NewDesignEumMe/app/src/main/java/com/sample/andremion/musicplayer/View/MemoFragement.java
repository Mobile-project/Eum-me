package com.sample.andremion.musicplayer.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sample.andremion.musicplayer.R;


public class MemoFragement extends Fragment {
    private static String term;
    private EditText editText;
    String tag = "mymemofragment";
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (isVisibleToUser) {

            } else {
                term = editText.getText().toString();
//                Fragment fragment = new BasicFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("key",context);
//                fragment.setArguments(bundle);
                //   ((MainActivity)getActivity()).setString(context);
                // Log.d(tag, "Fragment ID ->" + rootView.getTag());
                // Log.d(tag,"프래그먼트 id"+String.valueOf(fragment.getId()));

                Log.d(tag, "메모내용저장 " + term);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static MemoFragement create(int position) {
        MemoFragement fragment = new MemoFragement();
        return fragment;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_memo, container, false);
    }


    public static String onFragmentSwipe() {
        return term;
    }
}
