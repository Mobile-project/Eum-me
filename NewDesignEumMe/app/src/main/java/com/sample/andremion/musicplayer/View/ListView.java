package com.sample.andremion.musicplayer.View;

import android.app.ProgressDialog;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sample.andremion.musicplayer.R;
import com.sample.andremion.musicplayer.Model.Constants;
import com.sample.andremion.musicplayer.Presenter.ListViewAdapter;
import com.sample.andremion.musicplayer.Presenter.ListViewItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListView extends AppCompatActivity{
    private File file;
    private List myList;
    private List myListDate;
    String tag = "myListViewActivity";

    //////////////////////////////////////////////////////////////
    FirebaseStorage storage = FirebaseStorage.getInstance();

    // Create a storage reference from our app
    //참조를 만들려면 FirebaseStorage 싱글톤 인스턴스를
    // 사용하고 이 인스턴스의 getReference() 메소드를 호출합니다.
    StorageReference storageRef = storage.getReference();
    private Uri filePath;

    public ProgressDialog progressDialog;

    //////////////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        myList = new ArrayList();
        myListDate = new ArrayList();
        String rootSD = Environment.getExternalStorageDirectory().toString();
        rootSD+="/ZEum_me";
        Log.d(tag,rootSD);
        Log.d(tag,"after rootSD");

        file = new File(rootSD);
        File list[] = file.listFiles();
        Log.d(tag,"after file list[] : " + list.length);


        // 파일 이름들 추가
        for(int i=0;i<list.length;i++){
            myList.add(list[i].getName());
            myListDate.add(list[i].lastModified());
        }
        Log.d(tag,"after myList add");


        android.widget.ListView listview ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (android.widget.ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        // 아이템 추가
        for(int i=0;i<myList.size();i++){
            Log.d(tag, "rootSD : " + rootSD);
            String filename = myList.get(i).toString();
            adapter.addItem(ContextCompat.getDrawable(this,R.drawable.btn_play),
                    filename,
                    getPlayTime(rootSD+"/"+filename),
                    getCreatedTime(Long.valueOf(myListDate.get(i).toString())));

        }

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;
                Log.d(tag, "item click listener");
                Toast.makeText(getApplicationContext(), "item num : " + position, Toast.LENGTH_SHORT).show();

                // TODO : use item data.
            }
        }) ;

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "longlong : " + myList.get(position).toString(), Toast.LENGTH_SHORT).show();
                Log.d(tag, "item long click listener");
                upLoad(myList.get(position).toString());
                return false;
            }
        });




    }


    // path에 있는 파일 길이 가져오기.
    // hh:mm:ss
    private String getPlayTime(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong( time );
        long duration = timeInmillisec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);
        return hours + ":" + minutes + ":" + seconds;
    }


    private String getCreatedTime(long date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date).toString();

    }


    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    public void upLoad(String fileName) {
        UploadTask uploadTask;
        filePath = Uri.parse(Constants.getFilePath() + "/" + fileName);                                 // 올라갈 파일 경로
        Log.d(tag, "filePath : " + filePath);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://eumme-c2ce7.appspot.com/");         // 올릴 저장소 주소
        //위에서 생성한 FirebaseStorage 를 참조하는 storage를 생성한다
        StorageReference storageRef = storage.getReference();                                           // 저장소에 대한 레퍼런스

        // 위의 저장소를 참조하는 images폴더안의 space.jpg 파일명으로 지정하여
        // 하위 위치를 가리키는 참조를 만든다
//        StorageReference spaceRef = storageRef.child("images/space.jpg");                               // 무엇?

        Uri file = Uri.fromFile(new File(Constants.getFilePath() + "/" + fileName));            // 올라갈 파일을 객체로 가져옴
        StorageReference Ref = storageRef.child("Recording/" + file.getLastPathSegment());            //
        uploadTask = Ref.putFile(file);                                                                 // 파일 올리는 태스크에 파일 장착

        // 상태바표시
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("uploading...");
        progressDialog.show();
        progressDialog.onStart();
        Log.d(tag, "after uploading...");


        // 파일 업로드의 성공/실패에 대한 콜백 받아 핸들링 하기 위해 아래와 같이 작성한다
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(tag, "on Failure");
                // Handle unsuccessful uploads
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(tag, "onSuccess");
                progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(tag, "onProgressss");
                //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
//                @SuppressWarnings("VisibleForTests")
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //dialog에 진행률을 퍼센트로 출력해 준다
                progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
            }
        });
    }
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////





    //Context 메뉴로 등록한 View(여기서는 ListView)가 처음 클릭되어 만들어질 때 호출되는 메소드
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        //res폴더의 menu플더안에 xml로 MenuItem추가하기.
        //mainmenu.xml 파일을 java 객체로 인플레이트(inflate)해서 menu객체에 추가
        getMenuInflater().inflate(R.menu.recording_file_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }


//    //Context 메뉴로 등록한 View(여기서는 ListView)가 클릭되었을 때 자동으로 호출되는 메소드
//    public boolean onContextItemSelected(MenuItem item) {
//        //AdapterContextMenuInfo
//        //AdapterView가 onCreateContextMenu할때의 추가적인 menu 정보를 관리하는 클래스
//        //ContextMenu로 등록된 AdapterView(여기서는 Listview)의 선택된 항목에 대한 정보를 관리하는 클래스
//        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        int index= info.position; //AdapterView안에서 ContextMenu를 보여즈는 항목의 위치
//        //선택된 ContextMenu의  아이템아이디를 구별하여 원하는 작업 수행
//        //예제에서는 선택된 ListView의 항목(String 문자열) data와 해당 메뉴이름을 출력함
//        switch( item.getItemId() ){
//            case R.id.modify:
//                Toast.makeText(this, mDatas.get(index)+" Modify", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.delete:
//                Toast.makeText(this, mDatas.get(index)+" Delete", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.info:
//                Toast.makeText(this, mDatas.get(index)+" Info", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    };


}
