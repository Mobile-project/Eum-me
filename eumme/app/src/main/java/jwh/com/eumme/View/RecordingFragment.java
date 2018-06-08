package jwh.com.eumme.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jhw.Eumme.ver.R;

import jwh.com.eumme.Model.memoItem;
import jwh.com.eumme.Presenter.FlagSingleton;
import jwh.com.eumme.Presenter.RecordingSingleton;


public class RecordingFragment extends Fragment  {
    private  String txt="";
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
                if (FlagSingleton.getInstance().getFlag()) //Flag 오른쪽으로 넘어갔는지 왼쪽으로 넘어갔는지 알려주기
                {
                    memoItem newItem = new memoItem(txt, FlagSingleton.getInstance().getTime(), CurrentPosition - 2); //데이터 세팅
                    if (RecordingSingleton.getInstance().check(CurrentPosition-2)) {
                        //있으면 덮어쓰기
                        Log.d(tag,"사이즈"+RecordingSingleton.getInstance().getMemoItemList().size());
                        RecordingSingleton.getInstance().reset(CurrentPosition - 2,txt);
                    } else {
                        //없으면 그냥쓰기
                        RecordingSingleton.getInstance().addToArray(CurrentPosition - 2, newItem);

                    }
                }
            }
        } catch (NullPointerException e) {
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
