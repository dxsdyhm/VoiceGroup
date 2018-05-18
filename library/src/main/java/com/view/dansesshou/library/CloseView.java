package com.view.dansesshou.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dxs on 2018/5/17.
 */

public class CloseView extends View {
    private Paint mPaint;
    private int lineW=2;
    private Rect rectBound;
    public CloseView(Context context) {
        this(context,null);
    }

    public CloseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CloseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mPaint != null) {
                            mPaint.setAlpha((int) (0.5 * 255));
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (mPaint != null) {
                            mPaint.setAlpha(255);
                        }
                        invalidate();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mPaint!=null){
            mPaint.setAlpha(255);
        }
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
            mesW=dip2px(getContext(),20);
        }

        if(heighMode==MeasureSpec.EXACTLY){
            mesH=heighSize;
        }else{
            mesH= dip2px(getContext(),20);
        }
        int real=Math.min(mesH,mesW);
        setMeasuredDimension(real,real);
    }

    private void initData(Context context) {
        rectBound=new Rect();

        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(dip2px(context,lineW));
        mPaint.setColor(Color.parseColor("#8a8a8a"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        getDrawingRect(rectBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(rectBound.left,rectBound.top,rectBound.right,rectBound.bottom,mPaint);
        canvas.drawLine(rectBound.left,rectBound.bottom,rectBound.right,rectBound.top,mPaint);
    }

    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
