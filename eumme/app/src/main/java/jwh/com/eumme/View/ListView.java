package jwh.com.eumme.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jwh.com.eumme.Model.Constants;
import jwh.com.eumme.Model.DBHelper;
import jwh.com.eumme.Model.RecordingMataData;
import jwh.com.eumme.Model.memoItem;
import jwh.com.eumme.Presenter.ListViewAdapter;
import jwh.com.eumme.Presenter.ListViewItem;
import jwh.com.eumme.R;


///////////************//////////////////
// 메모 없는 녹음파일은 업로드해도 표시안댐//
// 그래서 메모없으면 ""으로 넣었다. //////
// 업로드한 파일, 다운로드한 파일들 가지고 있기////
//
///////////************//////////////////


public class ListView extends AppCompatActivity{
    private File file;
    private List myList;
    private List myListDate;
    String tag = "myListViewActivity";

//    public HashSet<String> uploadedList = new HashSet<String>();

    //////////////////////////////////////////////////////////////
    FirebaseStorage storage = FirebaseStorage.getInstance();

    // Create a storage reference from our app
    //참조를 만들려면 FirebaseStorage 싱글톤 인스턴스를
    // 사용하고 이 인스턴스의 getReference() 메소드를 호출합니다.
    StorageReference storageRef = storage.getReference();
    private Uri filePath;

    public ProgressDialog progressDialog;
    public int index=0;

    public ListViewAdapter adapter;
    public android.widget.ListView listview ;

    File list[];
    //////////////////////////////////////////////////////////////

    String newName="";
    public DBHelper dbHelper;

    ////////////////////////////////////////////////////////////////////////
    //////////////////////Reaf Time Database////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();     // 읽기 작업용?
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    //////////////////검색////////////////
    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        /////////////다운로드 테스트용////
//        downLoad("fu2.mp4");
        /////////////////////////////

//        Log.d(tag, "지금 내가 시발 당장 가지고 있는거 : " + uploadedList.size());

        myList = new ArrayList();
        myListDate = new ArrayList();
        String rootSD = Environment.getExternalStorageDirectory().toString();
        rootSD+="/ZEum_me";
        Log.d(tag,rootSD);
        Log.d(tag,"after rootSD");


        file = new File(rootSD);
        file.mkdir();
        list = file.listFiles();

        // 파일 이름들 추가
        for(int i=0;i<list.length;i++){
            myList.add(list[i].getName());
            myListDate.add(list[i].lastModified());
        }

        Log.d(tag,"after myList add");

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        dbHelper = new DBHelper(this);
        dbHelper.open(); // 디비오픈

        // 리스트뷰 참조 및 Adapter달기
        listview = (android.widget.ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
//        Log.d(tag, "지금 내가 시발 당장 가지고 있는거 141: " + uploadedList.size());

        // 아이템 추가
        for(int i=0;i<list.length;i++){
            String fileName = list[i].getName().toString();
            Log.d(tag, "정상additem : " + fileName);
            adapter.addItem(ContextCompat.getDrawable(this,R.drawable.folder),        // 플레이버튼
                    fileName,                                                                   // 녹음 파일 이름
                    Constants.getPlayTime(rootSD+"/"+fileName),                           // 녹음파일 재생시간
                    Constants.getCreatedTime(list[i]),                                           // 녹음파일 마지막 수정시간
                    false,                                                                    // 일단 false
                    true
            );
        }



        editSearch = findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editSearch.getText().toString();
                ((ListViewAdapter)listview.getAdapter()).getFilter().filter(text);
            }

        });


        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
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
                //클릭된 아이템의 위치를 이용하여 데이터인 문자열을 Toast로 출력
//                Toast.makeText(getApplicationContext(), myList.get(position).toString(),Toast.LENGTH_SHORT).show();
            }
        });

//        listviewFB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//                // get item
//                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;
//                Log.d(tag, "item click listener");
//                Toast.makeText(getApplicationContext(), "item num : " + position, Toast.LENGTH_SHORT).show();
//
//            }
//        }) ;

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "longlong : " + myList.get(position).toString(), Toast.LENGTH_SHORT).show();

                Log.d(tag, "item long click listener");
                return false;
            }
        });

        // 컨텍스트 메뉴 등록
        registerForContextMenu(listview);

        ////////////////////////////////////////////////////////////////////////
        //////////////////////Reaf Time Database////////////////////////////////
        ////////////////////////////////////////////////////////////////////////

        // message는 child의 이벤트를 수신합니다.
        databaseReference.child(Constants.getUserName()+":"+Constants.getUserUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Log.d(tag, "get key : " + dataSnapshot.getKey());               // Constants.getUserUid() 밑에 있는 애가 키값
//                Log.d(tag, "get key2 : " + dataSnapshot.getChildrenCount());
//                Log.d(tag, "get key3 : " + dataSnapshot.getValue());
                Log.d(tag, "leng : " + dataSnapshot.getChildrenCount());
                Log.d(tag, "dataSnapshot.getKey() : " + dataSnapshot.getKey()); // 파일 네임

//                Iterator iterator = uploadedList.iterator();
//                while (iterator.hasNext()) {
//                    Log.d(tag, "getUploadList() : " + iterator.next());
//                }


                for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                    String key = snapshot.getKey();
                    Log.d(tag, "key ; " + key);                 // 이건 애트리뷰트

                    for(DataSnapshot snapbaby : snapshot.getChildren()){
                        Log.d(tag, "snap baby : " + snapbaby.getValue());
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////

    }

    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////


    //Context 메뉴로 등록한 View(여기서는 ListView)가 처음 클릭되어 만들어질 때 호출되는 메소드
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //res폴더의 menu플더안에 xml로 MenuItem추가하기.
        //mainmenu.xml 파일을 java 객체로 인플레이트(inflate)해서 menu객체에 추가
        getMenuInflater().inflate(R.menu.recording_file_context_menu, menu);
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
        index= info.position; //AdapterView안에서 ContextMenu를 보여주는 항목의 위치
        String fileName = myList.get(index).toString();
        //예제에서는 선택된 ListView의 항목(String 문자열) data와 해당 메뉴이름을 출력함
        switch( item.getItemId() ){
            case R.id.changeName:
                Toast.makeText(this, myList.get(index)+" Change Name", Toast.LENGTH_SHORT).show();
                // 이름 새로 받아야함
//                ((MainActivity)MainActivity.mContext).dbHelper.noName(myList.get(index).toString(), newName);
                setNewName("");
                customDialog(index);    // 이름 받는 다이얼로그

//                if(!newName.equals("")){
////                    Log.d(tag, "이프문에 들어왔다.");
////                    nameChange(myList.get(index).toString(), newName);      // 이름 변경
////                    adapter.nameChange(index, newName+".mp4");
////                    adapter.notifyDataSetChanged();
//                } else{
//                    Log.d(tag, "엘스문이다.");
//
//                }

                // 파일 이름 바꾸는 함수
                break;
            case R.id.upload:
                if(adapter.ischecked(index)){
                    // 체크안되어있으면(업로드 안되어있으면 다운로드)
                    Log.d(tag, "체크안되어있으면(업로드 되어있으면 다운로드)");

                } else {


                    Toast.makeText(this, myList.get(index) + " Upload", Toast.LENGTH_SHORT).show();
                    upLoad(myList.get(index).toString());
                    String uid = Constants.getUserUid();

                    RecordingMataData meta = dbHelper.getResult(fileName);  // 디비에서 데이터 다뽑아옴
                    List<memoItem> itemList = meta.getMemoItemList();       // 디비에서온 데이터 담을 리스트

                    String fn = myList.get(index).toString();               // 파일이름
                    // .mp4에서 . 안되서 . 전까지만 이름으로 저장.
                    fn = fn.substring(0, fn.lastIndexOf('.'));          // 파일 이름 자름

                    Log.d(tag, "len : " + itemList.size());
                    Log.d(tag, "올라갈파일이름 : " + fn);
                    int len = itemList.size();
                    String memo = "";                // 메모내용
                    String memotime = "";             // 메모타임
                    String memoindex = "";            // 메모인덱스
                    String playtime = String.valueOf(adapter.getPlaytime(index));
                    String createdtime = String.valueOf(adapter.getCreatedTime(index));


                    Map<String, Object> taskMap = new HashMap<String, Object>();

                    List memolist = new ArrayList<>();
                    List memotimelist = new ArrayList<>();
                    List memoindexlist = new ArrayList<>();

                    if (len > 0) {  //  메모가 있다
                        for (int i = 0; i < len; i++) {

                            Log.d(tag, "test in for memo: " + itemList.get(i).getMemo());
                            Log.d(tag, "test in for memotime: " + itemList.get(i).getMemoTime());
                            Log.d(tag, "test in for memoindex: " + itemList.get(i).getMemoIndex());

                            memo = itemList.get(i).getMemo();
                            memotime = itemList.get(i).getMemoTime();
                            memoindex = String.valueOf(itemList.get(i).getMemoIndex());

                            memolist.add(memo);
                            memotimelist.add(memotime);
                            memoindexlist.add(memoindex);


                        }
                    } else { // 메모가 없다.

//                        databaseReference.child(uid).child(fn).child("memo").push().setValue(memo);
//                        databaseReference.child(uid).child(fn).child("memoTime").push().setValue(memotime);
//                        databaseReference.child(uid).child(fn).child("memoIndex").push().setValue(memoindex);
                        memolist.add("");
                        memotimelist.add("");
                        memoindexlist.add("");
                    }

                    taskMap.put("memo", memolist);
                    taskMap.put("memoTime", memotimelist);
                    taskMap.put("memoIndex", memoindexlist);

                    databaseReference.child(Constants.getUserName()).child(uid).child(fn).child("playTime").setValue(playtime);
                    databaseReference.child(Constants.getUserName()).child(uid).child(fn).child("createdTime").setValue(createdtime);
                    databaseReference.child(Constants.getUserName()).child(uid).child(fn).updateChildren(taskMap);

                }
                Log.d(tag, "after push");

                break;
            case R.id.delete:
                Toast.makeText(this, myList.get(index)+" Delete", Toast.LENGTH_SHORT).show();
                dbHelper.delete(myList.get(index).toString());
                File file = new File(Constants.getFilePath()+"/"+myList.get(index).toString());
                Log.d(tag, "delete : " + Constants.getFilePath()+"/"+myList.get(index).toString());
                ///////////////////////////////////////////////
                /////// 여기 안하면 리스트 제대로 업데이트 안댐//////////
                myList.remove(index);
                myListDate.remove(index);
                file.delete();
                ///////////////////////////////////////////////

                adapter.deleteItem(index);
                adapter.notifyDataSetChanged();

                /////////////////////////
                // 바로 갱신 안댐 //////////
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        //reload content
//
//                        myList.remove(index);
//                        adapter.notifyDataSetChanged();
//                        listviewFB.invalidateViews();
//                        listviewFB.refreshDrawableState();
//                    }
//                });
                /////////////////////////
                /////////////////////////


                break;
        }
        return true;
    };


//    //ListView의 아이템 하나가 클릭되는 것을 감지하는 Listener객체 생성 (Button의 OnClickListener와 같은 역할)
//    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {
//        //ListView의 아이템 중 하나가 클릭될 때 호출되는 메소드
//        //첫번째 파라미터 : 클릭된 아이템을 보여주고 있는 AdapterView 객체(여기서는 ListView객체)
//        //두번째 파라미터 : 클릭된 아이템 뷰
//        //세번째 파라미터 : 클릭된 아이템의 위치(ListView이 첫번째 아이템(가장위쪽)부터 차례대로 0,1,2,3.....)
//        //네번재 파리미터 : 클릭된 아이템의 아이디(특별한 설정이 없다면 세번째 파라이터인 position과 같은 값)
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            //클릭된 아이템의 위치를 이용하여 데이터인 문자열을 Toast로 출력
//            Toast.makeText(getApplicationContext(), myList.get(position).toString(),Toast.LENGTH_SHORT).show();
//
//        }
//
//    };





    public void nameChange(String preName, String newName){
        Log.d(tag, "in name change : " +Constants.getFilePath()+"/"+ preName);
        Log.d(tag, "in name change : " + Constants.getFilePath()+"/" +newName+".mp4");
        File filePre = new File(Constants.getFilePath()+"/", preName);
        File fileNow = new File(Constants.getFilePath()+"/", newName+".mp4");

        if(filePre.renameTo(fileNow)){
          //  Toast.makeText(getApplicationContext(), "변경 성공 : "+newName + ".mp4", Toast.LENGTH_SHORT).show();
            adapter.nameChange(index, newName+".mp4");
            adapter.notifyDataSetChanged();
        }else{
            //Toast.makeText(getApplicationContext(), "변경 실패", Toast.LENGTH_SHORT).show();
        }
        dbHelper.reName(myList.get(index).toString(), newName+".mp4");

    }



    public void customDialog(final int index){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final String[] ret = {""};
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Change File Name");
        alert.setMessage("Plz, input new file name");


        final EditText name = new EditText(this);
        alert.setView(name);

        alert.setNegativeButton("Cancle",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ret[0] = name.getText().toString();
                Log.d(tag, "셋 전 : " + ret[0]);
                setNewName(ret[0]);
                nameChange(myList.get(index).toString(), ret[0]);
            }
        });

        alert.show();

    }

    public void setNewName(String t){
        this.newName = t;
    }

    public String getNewName(String t){
        return this.newName;
    }




    //////////////////////////////////////////////////////////////
    ///////////////////////FIRE BASE UPLOAD///////////////////////
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
        StorageReference Ref = storageRef.child("users").child(Constants.getUserName()).child(Constants.getUserUid()).child("Recording/" + file.getLastPathSegment());// 파일 올라가는 위치
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







//    public List<String> findFileOnlyInFireBase(){
//        List<String> ret = new ArrayList<String>();
//
//        for(String iter : uploadedList){
//            // 업로드한 파일이 로컬에 없다면
//            if(!myList.contains(iter)){
//                Log.d(tag, "웹에만 있는애들 : " + iter);
//                ret.add(iter);
//            } else{
//                Log.d(tag, "둘다 있는애들 : " + iter);
//            }
//        }
//
//        return ret;
//    }




}