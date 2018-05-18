package com.view.dansesshou.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dxs on 2018/5/17.
 */

public class ArrowsView extends View {
    private Paint mPaintArrow;//箭头画笔
    private Path mPathArrow;
    private float arrowHRadio=0.533f;//箭头宽高比
    private float arrowGapRadio=0.222f;//间隙比例
    private float lineRadio=0.067f;//线条比例
    private float arrowGap=3.33f;
    private Rect bound;
    private int lineW=1;
    private float progress;

    public ArrowsView(Context context) {
        this(context,null);
    }

    public ArrowsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ArrowsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
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
            mesW=dip2px(getContext(),15);
        }
        mesH= (int) (mesW*(arrowHRadio+arrowGapRadio));
        setMeasuredDimension(mesW,mesH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetData(getContext());
        getDrawingRect(bound);
    }

    public void setShowProgress(float progress){
        this.progress=progress;
        invalidate();
    }

    private void initData(Context context) {
        mPathArrow=new Path();
        bound=new Rect();
        mPaintArrow=new Paint();
        mPaintArrow.setAntiAlias(true);
        mPaintArrow.setStyle(Paint.Style.STROKE);
        mPaintArrow.setStrokeCap(Paint.Cap.ROUND);
        mPaintArrow.setColor(Color.parseColor("#8a8a8a"));
        resetData(context);
    }

    private void resetData(Context context){
        arrowGap=getMeasuredWidth()==0?dip2px(context,arrowGap):getMeasuredWidth()*arrowGapRadio;
        mPaintArrow.setStrokeWidth(getMeasuredWidth()*lineRadio);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArrow(canvas);
    }

    private void drawArrow(Canvas canvas){
        float offset=(bound.height()-arrowGap-lineW)*progress;
//        mPaintArrow.setAlpha();
        mPathArrow.rewind();
        mPathArrow.moveTo(bound.left,bound.bottom-offset);
        mPathArrow.lineTo(bound.centerX(),bound.top+arrowGap+lineW/2+offset);
        mPathArrow.lineTo(bound.right,bound.bottom-offset);
        canvas.drawPath(mPathArrow,mPaintArrow);
        mPathArrow.offset(0,-arrowGap);
        canvas.drawPath(mPathArrow,mPaintArrow);
    }

    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
