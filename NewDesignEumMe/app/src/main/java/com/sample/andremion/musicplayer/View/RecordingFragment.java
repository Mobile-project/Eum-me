package com.sample.andremion.musicplayer.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sample.andremion.musicplayer.Presenter.FlagSingleton;
import com.sample.andremion.musicplayer.Presenter.RecordingSingleton;
import com.sample.andremion.musicplayer.Model.memoItem;
import com.sample.andremion.musicplayer.R;


public class RecordingFragment extends Fragment  {
    private static String txt;
    private EditText editText;
    String tag = "mymainactivity";
    private static int CurrentPosition;

     @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (isVisibleToUser) {
            } else {
                txt = editText.getText().toString();
                if (FlagSingleton.getInstance().getFlag()) {
                    Log.d(tag, "그럼 저장해~~ ");
                    memoItem newItem = new memoItem(txt, FlagSingleton.getInstance().getTime(), CurrentPosition - 2);
                    Log.d(tag, "저장 아이템 셋팅 완료");
                    RecordingSingleton.getInstance().addToArray(newItem);
                    Log.d(tag, "리스트 받고 삽입하기 ");

                    String memo = RecordingSingleton.getInstance().getMemo(CurrentPosition - 2);
                    int index = RecordingSingleton.getInstance().getIndex(CurrentPosition - 2);
                    String time = RecordingSingleton.getInstance().getTime(CurrentPosition - 2);
                    Log.d(tag, "저장할 내용은 " + txt);
                    Log.d(tag, "저장완료됐구 이렇게 들어있어 " + memo + " " + index + " " + time);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RecordingFragment create(int position) {

        RecordingFragment fragment = new RecordingFragment();
        CurrentPosition = position;
        return fragment;
        // Required empty public constructor
    }

    @Override
    public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_memo, container, false);
        editText = view.findViewById(R.id.memo_area);
        Log.d("aa","지금 녹음중인데 이건 현재 프래그먼트 포지션이야 "+CurrentPosition);
        return view;
    }
}
