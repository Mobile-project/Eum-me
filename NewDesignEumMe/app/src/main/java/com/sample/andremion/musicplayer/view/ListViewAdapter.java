package com.sample.andremion.musicplayer.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.RecordingMataData;
import com.sample.andremion.musicplayer.activities.MainActivity;
import com.sample.andremion.musicplayer.activities.PlayActivity;

import java.io.File;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private File file;
    String tag = "myListView";


    RecordingMataData metaData;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position; // 0번 부터 시작
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
//        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.btn_play) ;
        ImageButton playButton = (ImageButton) convertView.findViewById(R.id.btn_play);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.text_view_file_name) ;
        TextView playTimeTextView = (TextView) convertView.findViewById(R.id.text_view_play_time) ;
        TextView dateTextView = (TextView) convertView.findViewById(R.id.text_view_date);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 각 항목의 플레이 버튼 눌렀을떄
                Log.d(tag, "play button click : " + pos);
                Toast.makeText(context, "play button click : " + pos,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, PlayActivity.class);
                Bundle bundle = new Bundle();
                Log.d(tag, listViewItemList.get(pos).getFileName());
                bundle.putString("fileName", listViewItemList.get(pos).getFileName());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });



        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
//        iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getFileName());
        playTimeTextView.setText(listViewItem.getPlayTime());
        dateTextView.setText(listViewItem.getCreatedTime());
//        playTimeTextView.setText(listViewItem.getDesc()); // 순서대로 숫자할당


        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = listViewItem.getFileName(); // 현재 누른 파일 이름

                Toast.makeText(context, "file name click : " + fileName, Toast.LENGTH_SHORT).show();


                //
//                Log.d(tag, "test : " + ((MainActivity)MainActivity.mContext).getA());
                //

                // 컨텍스트를 통해 액티비티에 접근
                // dbHelper.getResult로 메모목록 가져옴
                metaData = ((MainActivity)MainActivity.mContext).dbHelper.getResult(fileName);
                Log.d(tag, "metadata test : " + metaData.getCreatedTime());
                int len = metaData.getMemoItem().size();
                Log.d(tag, "len : " + len);
                for(int i=0;i<len;i++){
                    Log.d(tag, metaData.getMemoItem().get(i).getFileName() + " " +
                    metaData.getMemoItem().get(i).getMemo() + " " +
                    metaData.getMemoItem().get(i).getMemoIndex() + " " +
                    metaData.getPlayTime() + " " +
                    metaData.getCreatedTime());
                }
            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable icon, String title, String playtime, String createdtime) {
        ListViewItem item = new ListViewItem();
        item.setIcon(icon);
        item.setFileName(title);
        Log.d(tag, "playtime : "+playtime);
        item.setPlayTime(String.valueOf(playtime));
        item.setCreatedTime(createdtime);
        Log.d(tag, "addItem : playtime : " + playtime);
        Log.d(tag, "addItem : cratedTime : " + ((MainActivity)MainActivity.mContext).dbHelper.getCreatedTime(title));
        listViewItemList.add(item);
    }



}