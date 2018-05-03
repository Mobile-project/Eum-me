package org.androidtown.eum_me;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by tamama on 2018. 4. 30..
 */

public class RecordActivity extends AppCompatActivity{

    ImageButton btn_pause;
    ImageButton btn_book_mark;
    LinearLayout memoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_main);

        btn_book_mark=(ImageButton)findViewById(R.id.btn_book_mark);
        btn_book_mark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),"book", Toast.LENGTH_LONG).show();

            }
        });

        btn_pause=findViewById(R.id.btn_pause);
        btn_pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),"pause", Toast.LENGTH_LONG).show();

            }
        });

        memoFragment=(LinearLayout)findViewById(R.id.layout_memo_fragment);
        memoFragment.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float distance=0;
                float pressedX=0;
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        pressedX=motionEvent.getX();
                       // Toast.makeText(getApplicationContext(),"x좌표 받기",Toast.LENGTH_LONG).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        distance=pressedX-motionEvent.getX();
                        //Toast.makeText(getApplicationContext(),"거리계산",Toast.LENGTH_LONG).show();
                        break;
                }

                if(Math.abs(distance)<100){
                    return false;
                }

                if(distance>0){
                    Intent intent = new Intent(getApplicationContext(),memoFragment.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slid_out_right);
                    Toast.makeText(getApplicationContext(),"드래그로 화면 전환 됨",Toast.LENGTH_LONG).show();
                }
                finish();
                return true;
            }
        });
    }
}
