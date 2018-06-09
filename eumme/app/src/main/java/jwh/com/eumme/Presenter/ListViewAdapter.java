package jwh.com.eumme.Presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhw.Eumme.ver.R;

import java.io.File;
import java.util.ArrayList;

import jwh.com.eumme.Model.RecordingMataData;
import jwh.com.eumme.View.PlayActivity;

public class ListViewAdapter extends BaseAdapter {
    private File file;
    String tag = "myListView";
    String playtime;
    int duration;
    int seconds, minutes, hours;


    ImageView playButton;
    TextView titleTextView;
    TextView playTimeTextView;
    TextView dateTextView;
    RelativeLayout topContainer;
    ImageView uploadDownload;



    private Context mContext;
    RecordingMataData metaData;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }



    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
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
//        LinearLayout fileNameContainer = (LinearLayout) convertView.findViewById(R.id.file_name_container);
        playButton = (ImageView) convertView.findViewById(R.id.btn_play);
        titleTextView = (TextView) convertView.findViewById(R.id.text_view_file_name);
        playTimeTextView = (TextView) convertView.findViewById(R.id.text_view_play_time);
        dateTextView = (TextView) convertView.findViewById(R.id.text_view_date);
        topContainer = (RelativeLayout) convertView.findViewById(R.id.top_container);
        uploadDownload = (ImageView) convertView.findViewById(R.id.upload_download);



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 각 항목의 플레이 버튼 눌렀을떄
                Toast.makeText(context, "play button click : " + pos, Toast.LENGTH_SHORT).show();
                //List<String> memos =
                Intent intent = new Intent(context, PlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fileName", listViewItemList.get(pos).getFileName());
                bundle.putString("playTime", listViewItemList.get(pos).getPlayTime());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(tag, "play button click : " + pos);
//                Toast.makeText(context, "play button click : " + pos,Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(context, PlayActivity.class);
//                Bundle bundle = new Bundle();
//                Log.d(tag, listViewItemList.get(pos).getFileName());
//                bundle.putString("fileName", listViewItemList.get(pos).getFileName());
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        playButton.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getFileName());
        playTimeTextView.setText(listViewItem.getPlayTime());
        dateTextView.setText(listViewItem.getCreatedTime());

        ///// 체크버튼 표시 결정//////
        if (listViewItem.isUploaded() && listViewItem.isDownloaded()) { // TT
            uploadDownload.setImageResource(R.drawable.cloud_green);
        } else if(listViewItem.isUploaded() && !listViewItem.isDownloaded()){ // TF
            uploadDownload.setImageResource(R.drawable.download_green);
        } else if(!listViewItem.isUploaded() && listViewItem.isDownloaded()){ // FT
            uploadDownload.setImageResource(R.drawable.upload_green);
        } else{ // FF
            uploadDownload.setImageResource(R.drawable.cloud_black);
        }

        //        playtime = listViewItem.getPlayTime().replace(":", "");
//        duration = Integer.parseInt(playtime);
//        seconds = duration % 100;
//        minutes = (duration - seconds) % 100;
//        hours = (duration / 10000);
//        playTimeTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable icon, String title, String playTime, String createdTime, boolean uploadTF, boolean downloadTF) {
        ListViewItem item = new ListViewItem();
        item.setIcon(icon);
        item.setFileName(title);
        item.setPlayTime(String.valueOf(playTime));
        item.setCreatedTime(createdTime);
        item.setUploaded(uploadTF);
        item.setDownloaded(downloadTF);
//        Log.d(tag, "playtime : "+playTime);
//        Log.d(tag, "addItem : playtime : " + playTime);
//        Log.d(tag, "addItem : cratedTime : " + ((MainActivity)MainActivity.mContext).dbHelper.getCreatedTime(title));

        listViewItemList.add(item);
    }

    public void deleteItem(int position) {
        listViewItemList.remove(position);
    }

    public void modifyIsUploded(int position, boolean tf){
        listViewItemList.get(position).setUploaded(tf);
    }

    public void modifyIsDownloded(int position, boolean tf){
        listViewItemList.get(position).setDownloaded(tf);
    }

    public boolean ischecked(int idx){
        return this.listViewItemList.get(idx).isUploaded();
    }

    public boolean isContain(String name){
        return listViewItemList.contains(name);
    }

    public int getPosition(String name){
        return listViewItemList.indexOf(name);
    }

    public void nameChange(int idx, String newName){
        Log.d(tag, "pre name : " + listViewItemList.get(idx).getFileName());
        listViewItemList.get(idx).setFileName(newName);
        Log.d(tag, "new name : " + listViewItemList.get(idx).getFileName());
    }

    public String getPlaytime(int index){
        return String.valueOf(listViewItemList.get(index).getPlayTime());
    }


}