package com.sample.andremion.musicplayer.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sample.andremion.musicplayer.Presenter.FlagSingleton;
import com.sample.andremion.musicplayer.Presenter.MemoSingleton;
import com.sample.andremion.musicplayer.Model.memoItem;
import com.sample.andremion.musicplayer.R;


public class RecordingFragment extends Fragment  {
    private static String term;
    private EditText editText;
    String tag = "mymainactivity";
    private static int CurrentPosition;

     @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (isVisibleToUser) {
            } else {
                term = editText.getText().toString();
                if (FlagSingleton.getInstance().getFlag()) {
                    Log.d(tag, "그럼 저장해~~ ");
                    memoItem newItem = new memoItem(term, FlagSingleton.getInstance().getTime(), CurrentPosition - 2);
                    Log.d(tag, "저장 아이템 셋팅 완료");
                    MemoSingleton.getInstance().addToArray(CurrentPosition - 2, newItem);
                    Log.d(tag, "리스트 받고 삽입하기 ");

                    String memo = MemoSingleton.getInstance().getMemo(CurrentPosition - 2);
                    int index = MemoSingleton.getInstance().getIndex(CurrentPosition - 2);
                    String time = MemoSingleton.getInstance().getTime(CurrentPosition - 2);
                    Log.d(tag, "저장할 내용은 " + term);
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
        return view;
    }
}
