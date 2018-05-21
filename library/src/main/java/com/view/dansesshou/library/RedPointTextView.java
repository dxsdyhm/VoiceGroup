package com.view.dansesshou.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dxs on 2018/5/15.
 */

public class RedPointTextView extends View {
    private  int currentValue=0;
    private  boolean isRedShow=true;
    private Paint mPaintRed;
    private Paint mTextPaint;
    private Rect redRect;
    private Rect textRect;
    private float redRatio=0.67f;
    private int grap=5;
    public RedPointTextView(Context context) {
        this(context,null);
    }

    public RedPointTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RedPointTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private void initData(Context context) {
        redRect=new Rect();
        textRect=new Rect();
        grap=dip2px(context,grap);
        mPaintRed = new Paint();
        mPaintRed.setAntiAlias(true);
        mPaintRed.setColor(Color.parseColor("#ff0000"));

        mTextPaint=new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setTextSize(dip2px(context, 12));
        mTextPaint.setColor(Color.parseColor("#000000"));
    }

    public void isShowRed(boolean isRedShow){
        new Thread(){
            @Override
            public void run() {
                while (redRect.width()*redRatio/2>0){
                    int l=redRect.left;
                    int t=redRect.top;
                    int r=redRect.right;
                    int b=redRect.bottom;
                    redRect.set(l+=1,t+=1,r-=1,b-=1);
                    textRect.offset(2,0);
                    postInvalidateDelayed(100);
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
        drawRed(canvas);
    }

    private void drawRed(Canvas canvas){
        canvas.drawCircle(redRect.centerX(),redRect.centerY(),redRect.width()*redRatio/2,mPaintRed);
    }

    private void drawText(Canvas canvas){
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (textRect.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式
        String text=getFormatText(currentValue);
        canvas.drawText(text,textRect.right,baseLineY,mTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heighMode=MeasureSpec.getMode(heightMeasureSpec);
        int heighSize=MeasureSpec.getSize(heightMeasureSpec);
        int mesW,mesH;
        if(widthMode==MeasureSpec.EXACTLY){
            mesW=widthSize;
        }else{
            mesW=dip2px(getContext(),100);
        }

        if(heighMode==MeasureSpec.EXACTLY){
            mesH=heighSize;
        }else{
            mesH= dip2px(getContext(),20);
        }
        setMeasuredDimension(mesW,mesH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        redRect.set(w-h,0,w,h);
        textRect.set(0,0,w-h-grap,h);
    }

    private static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private String getFormatText(int value){
        return ""+value+"″";
    }
}
