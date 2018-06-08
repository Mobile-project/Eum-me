package jwh.com.eumme.View;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.jhw.Eumme.ver.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jwh.com.eumme.Model.Constants;
import jwh.com.eumme.Presenter.ListViewAdapter;

public class ListViewFirebase extends AppCompatActivity{
    String tag = "myListviewfirebase";

    public ListViewAdapter adapterFB = null;
    public android.widget.ListView listviewFB = null;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    public ProgressDialog progressDialog;


    List list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        Intent intent = getIntent();
        HashSet<String> set = (HashSet<String>) intent.getSerializableExtra("set");
        Log.d(tag, "size: " + set.size());

        list = new ArrayList();     // 업로드된 파일 이름 담는 배열


        adapterFB = new ListViewAdapter();
        listviewFB = findViewById(R.id.listview1);
        if(listviewFB ==null){
            Log.d(tag, "listviewFB is null");
        }
        listviewFB.setAdapter(adapterFB);

        Iterator<String> filename = set.iterator();
        while(filename.hasNext()){
            String t = filename.next();
            list.add(t);    // 업로드된 파일이름 리스트에 추가
            adapterFB.addItem(ContextCompat.getDrawable(this,R.drawable.cow),
                    t,
                    null,
                    null,
                    true,
                    false);
        }


        listviewFB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        registerForContextMenu(listviewFB);



    }

    //Context 메뉴로 등록한 View(여기서는 ListView)가 처음 클릭되어 만들어질 때 호출되는 메소드
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        //res폴더의 menu플더안에 xml로 MenuItem추가하기.
        //mainmenu.xml 파일을 java 객체로 인플레이트(inflate)해서 menu객체에 추가
        getMenuInflater().inflate(R.menu.recording_file_context_menu_fb, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    //    //Context 메뉴로 등록한 View(여기서는 ListView)가 클릭되었을 때 자동으로 호출되는 메소드
    /// 각 아이템 클릭했을떄 어떻게 처리할지
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterContextMenuInfo
        //AdapterView가 onCreateContextMenu할때의 추가적인 menu 정보를 관리하는 클래스
        //ContextMenu로 등록된 AdapterView(여기서는 Listview)의 선택된 항목에 대한 정보를 관리하는 클래스
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position; //AdapterView안에서 ContextMenu를 보여주는 항목의 위치
//        String fileName = myList.get(index).toString();
        //예제에서는 선택된 ListView의 항목(String 문자열) data와 해당 메뉴이름을 출력함
        switch( item.getItemId() ){
            case R.id.changeName:
                Toast.makeText(getApplicationContext(), "changename", Toast.LENGTH_SHORT).show();
                break;
            case R.id.download:
                Toast.makeText(getApplicationContext(), "download", Toast.LENGTH_SHORT).show();
                downLoad(list.get(index).toString(), index);
                break;
            case R.id.delete:
                Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    };







    //////////////////////////////////////////////////////////////
    ///////////////////////FIRE BASE DOWNLOAD/////////////////////
    //////////////////////////////////////////////////////////////


    public void downLoad(String fileName, int idx){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("uploading...");
        progressDialog.show();
        progressDialog.onStart();
        Log.d(tag, "다운받을 파일 : " + fileName);
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child("users/" + Constants.getUserUid() + "/Recording/" + fileName);
        Log.d(tag, "파베에 파일 위치 : " + "users/" + Constants.getUserUid() + "/Recording/" + fileName);
        File localFile=null;
        try{
            localFile = File.createTempFile("temp", ".mp4", Environment.getExternalStorageDirectory());

        } catch(IOException e){
            e.printStackTrace();
        }


        final File finalLocalFile = localFile;
        final String finalFileName = fileName;
        final int finalIdx = idx;
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Log.d(tag, "다운로드 완료");
                Log.d(tag, "파일 위치 : " + finalLocalFile.getPath());
                moveFile(finalLocalFile, finalFileName);
//                moveFile(finalLocalFile, finalFileName);
//                adapterFB.deleteItem(finalIdx);
                adapterFB.modifyIsDownloded(finalIdx, true);
///////////////////////////////////////////////////


                adapterFB.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(tag, "다운로드 실패");
                progressDialog.dismiss();
                // Handle any errors
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d(tag, "on progress");
                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                //dialog에 진행률을 퍼센트로 출력해 준다
                progressDialog.setMessage("Downloaded " + ((int)progress) + "% ...");
            }
        });
        localFile.delete();
    }

    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    public void moveFile(File from, String name) {
//        File file = new File("D:\\Test.java");
        File file = from;
        File file2 = new File(Constants.getFilePath()+"/"+name);//이동

        if(file.exists()) {
            Log.d(tag, "이동 완료");
            file.renameTo(file2);	//변경

        }
    }
}
