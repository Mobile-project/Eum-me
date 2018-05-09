package com.example.izg08.proto.fileControl;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.izg08.proto.R;
import com.example.izg08.proto.audioControl.recordMain;

public class FileNameChange {
    private Context context;

    EditText message;
    Button okButton;
    Button cancelButton;
    recordMain record;

    //이름 변경
    private String newFileName;
    private String preFileName;

    public FileNameChange(Context context) {
        this.context = context;
        record= new recordMain();

    }
    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(String preName) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.file_name_change);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        message = (EditText) dlg.findViewById(R.id.mesgase);
        okButton = (Button) dlg.findViewById(R.id.okButton);
        cancelButton = (Button) dlg.findViewById(R.id.cancelButton);
        preFileName=preName;

        // 확인 버튼 클릭
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                newFileName=message.getText().toString();
                record.setNewFileName(newFileName);
                record.changeFileName(preFileName,newFileName);

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        // 취소 버튼 클릭
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}
