package com.view.dansesshou.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.view.dansesshou.library.RedPointTextView;
import com.view.dansesshou.library.VoiceView;

/**
 * Created by dxs on 2018/5/15.
 */

public class VoiceGroup extends ViewGroup {
    private VoiceView voiceView;
    private RedPointTextView redPointTextView;
    private int Grap = 12;
    private float ratio=3.8f;

    public VoiceGroup(Context context) {
        this(context, null);
    }

    public VoiceGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        Grap = dip2px(context, Grap);
        voiceView = new VoiceView(context);
        redPointTextView = new RedPointTextView(context);
        addView(redPointTextView, 0);
        addView(voiceView, 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = r - l;
        int h = b - t;
        View red = getChildAt(0);
        red.layout(0, 0, w, (int) (h * 0.23f));

        View voice = getChildAt(1);
        voice.layout(0, (int) (h * 0.5f), w, h);
    }

    //开始播放（附带红点消失）
    public void startPlaying(){
        setIsPlaying(true);
        setRedPointState(false);
    }

    //停止播放
    public void stopPlaying(){
        setIsPlaying(false);
    }

    public void setIsPlaying(boolean isPlaying){
        if(voiceView!=null){
            voiceView.startPlay(isPlaying);
        }
    }

    public void setRedPointState(boolean isShow){
        if(redPointTextView!=null){
            redPointTextView.isShowRed(isShow);
        }
    }

    private static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
