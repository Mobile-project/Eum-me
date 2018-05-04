package org.androidtown.eum_me.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidtown.eum_me.R;

public class waveFragment extends Fragment  {

    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;
    private Equalizer mEqualizer;

    private LinearLayout mLinearLayout;
    private TextView mStatusTextView;
    private VisualizerView mVisualizerView;
    MediaRecorder mediaRecorder;
    private Bundle bundle;

    //녹음 데이터를 담는 객체 넘겨 받기
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaPlayer=(MediaPlayer)getArguments().getSerializable("key");
        mLinearLayout=getActivity().findViewById(R.id.layout_wave);
        mStatusTextView = getActivity().findViewById(R.id.statusText);

        setupVisualizerFxAndUI();

        mVisualizer.setEnabled(true);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
            }
        });

        mMediaPlayer.start();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wave, container, false);
    }

private void setupVisualizerFxAndUI(){

        mVisualizerView = getActivity().findViewById(R.id.visual_view);
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
                mVisualizerView.updateVisualizer(bytes);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) { }
        },Visualizer.getMaxCaptureRate()/2,true,false);
}

}

class VisualizerView extends View{
    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();

    private Paint mForePaint = new Paint();

    public VisualizerView(Context context){
        super(context);
        init();
    }

    private void init()
    {
        mBytes=null;
        mForePaint.setStrokeWidth(1f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.rgb(0,128,255));
    }

    public void updateVisualizer(byte[] bytes)
    {
        mBytes=bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(mBytes==null){
            return;
        }

        if(mPoints==null||mPoints.length<mBytes.length*4){
            mPoints= new float[mBytes.length*4];
        }

        mRect.set(0,0,getWidth(),getHeight());

        for(int i=0;i<mBytes.length-1;i++){
            mPoints[i*4]=mRect.width()*i/(mBytes.length-1);
            mPoints[i*4+1]=mRect.height()/2+((byte)(mBytes[i]+128))*(mRect.height()/2)/128;
            mPoints[i*4+2]=mRect.width()*(i+1)/(mBytes.length-1);
            mPoints[i*4+3]=mRect.height()/2+((byte)(mBytes[i+1]+128))*(mRect.height()/2)/128;
        }
        canvas.drawLines(mPoints,mForePaint);
    }
}
