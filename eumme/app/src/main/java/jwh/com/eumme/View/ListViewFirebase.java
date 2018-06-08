package jwh.com.eumme.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.jhw.Eumme.ver.R;

import java.util.HashSet;
import java.util.Iterator;

import jwh.com.eumme.Presenter.ListViewAdapter;

public class ListViewFirebase extends AppCompatActivity{
    String tag = "myListviewfirebase";

    public ListViewAdapter adapter = null;
    public android.widget.ListView listview = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        Intent intent = getIntent();
        HashSet<String> set = (HashSet<String>) intent.getSerializableExtra("set");
        Log.d(tag, "size: " + set.size());


        adapter = new ListViewAdapter();
        listview = findViewById(R.id.listview1);
        if(listview==null){
            Log.d(tag, "listview is null");
        }
        listview.setAdapter(adapter);

        Iterator<String> filename = set.iterator();
        while(filename.hasNext()){
            adapter.addItem(ContextCompat.getDrawable(this,R.drawable.cow),
                    filename.next(),
                    null,
                    null,
                    true,
                    false);
        }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //ListView의 아이템 중 하나가 클릭될 때 호출되는 메소드
            //첫번째 파라미터 : 클릭된 아이템을 보여주고 있는 AdapterView 객체(여기서는 ListView객체)
            //두번째 파라미터 : 클릭된 아이템 뷰
            //세번째 파라미터 : 클릭된 아이템의 위치(ListView이 첫번째 아이템(가장위쪽)부터 차례대로 0,1,2,3.....)
            //네번재 파리미터 : 클릭된 아이템의 아이디(특별한 설정이 없다면 세번째 파라이터인 position과 같은 값)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get item
                //ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;
                // TODO Auto-generated method stub
                //클릭된 아이템의 위치를 이용하여 데이터인 문자열을 Toast로 출력
//                Toast.makeText(getApplicationContext(), myList.get(position).toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
