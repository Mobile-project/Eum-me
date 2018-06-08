package jwh.com.eumme.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jhw.Eumme.ver.R;

import java.util.concurrent.ConcurrentHashMap;

import jwh.com.eumme.Model.memoItem;
import jwh.com.eumme.Presenter.PlayViewPagerAdapter;


public class PlayingFragment extends Fragment {
    private String modifiedTxt;
    private static int CurrentPosition;
    private static ConcurrentHashMap<Integer, memoItem> txt;
    EditText editText;
    private static final String tag = "test";

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (PlayViewPagerAdapter.mode) {

            try {
                if (isVisibleToUser) {
                } else {
                    if (PlayViewPagerAdapter.mode) {
                        modifiedTxt = editText.getText().toString();
                        if (FlagSingleton.getInstance().getFlag()) {
                            //오른쪽으로 넘겻으면 저장하자
                            memoItem newItem = new memoItem(modifiedTxt,txt.get(CurrentPosition-2).getMemoTime(),CurrentPosition-2);
                            Log.d(tag,"저장하자 오른쪽으로 넘겻자나");
                            if (RecordingSingleton.getInstance().check(CurrentPosition - 2)) {
                                Log.d(tag,"값이 있는 곳이네 그럼 덮어쓰자 ");
                                RecordingSingleton.getInstance().reset(CurrentPosition - 2, modifiedTxt);
                            }
                            else{
                                Log.d(tag,"값이 없는 곳이네 그럼 새로 추가하자");
                                RecordingSingleton.getInstance().addToArray(CurrentPosition-2,newItem);
                                Log.d(tag,"메모내용 "+RecordingSingleton.getInstance().getMemo(CurrentPosition-2));
                                Log.d(tag,"메모 생성 시간 "+RecordingSingleton.getInstance().getTime(CurrentPosition-2));
                                Log.d(tag,"메모 인덱스 "+RecordingSingleton.getInstance().getIndex(CurrentPosition-2));

                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }*/

    public static PlayingFragment create(int position, ConcurrentHashMap<Integer, memoItem> list) {
        PlayingFragment fragment = new PlayingFragment();
        CurrentPosition = position;
        txt = list;

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_play, container, false);
        editText = view.findViewById(R.id.memo_area1);

        try {
            if (CurrentPosition == 1 && !PlayViewPagerAdapter.check) {
                PlayViewPagerAdapter.check = true;
                editText.setText(txt.get(CurrentPosition - 1).getMemo());
            } else {
                editText.setText(txt.get(CurrentPosition).getMemo());
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return view;

    }


}
