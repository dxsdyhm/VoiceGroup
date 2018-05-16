package com.view.dansesshou.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by dxs on 2018/5/16.
 */

public class IndicatorsViewGroup extends ViewGroup {
    private Path mPath;
    private Rect mBackRect;
    private Rect mFillBackRect;//填充区域//整体的一半
    private int ratios = 10;
    private Paint mPaint;
    private float tarRadio = 0.075f;//缺口比例（与宽）
    private int tarH = 0;//缺口深度（默认与缺口等长）
    private final float qurt2 = 1.4142f;
    private float progress;

    public IndicatorsViewGroup(Context context) {
        this(context, null);
    }

    public IndicatorsViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorsViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private void initData(Context context) {
        mPath = new Path();
        mBackRect = new Rect();
        mFillBackRect = new Rect();

        ratios = dip2px(context, ratios);

        setBackgroundColor(Color.TRANSPARENT);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#efefef"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        getDrawingRect(mBackRect);
        mFillBackRect.set(mBackRect.left, mBackRect.bottom / 2, mBackRect.right, mBackRect.bottom);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void setShowProgress(float progress) {
        this.progress = progress;
        mFillBackRect.set(mBackRect.left, (int) (mBackRect.bottom / 2 - progress * mBackRect.height() / 2), mBackRect.right, mBackRect.bottom);
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBackPath(canvas);
    }

    private float temp = 5;

    private void initBackPath(Canvas canvas) {
        float offset = mFillBackRect.width() * tarRadio / 2;
        float step = offset / 3;
        float tempProgress = (progress - 0.5f) * 2;
        mPath.rewind();
        mPath.moveTo(mFillBackRect.left, mFillBackRect.bottom);
        mPath.lineTo(mFillBackRect.left, mFillBackRect.top + ratios);
        mPath.quadTo(mFillBackRect.left, mFillBackRect.top, ratios, mFillBackRect.top);
        //1
        mPath.lineTo(mFillBackRect.centerX() - offset - step * qurt2, mFillBackRect.top);
        //2---
        mPath.quadTo(mFillBackRect.centerX() - offset, mFillBackRect.top, mFillBackRect.centerX() - step * 2, mFillBackRect.top + step * tempProgress);
        //3---
        mPath.lineTo(mFillBackRect.centerX() - step, mFillBackRect.top + step * 2 * tempProgress);
        //4---
        mPath.quadTo(mFillBackRect.centerX(), mFillBackRect.top + offset * tempProgress, mFillBackRect.centerX() + step, mFillBackRect.top + step * 2 * tempProgress);
        //5---
        mPath.lineTo(mFillBackRect.centerX() + step * 2, mFillBackRect.top + step * tempProgress);
        //6
        mPath.quadTo(mFillBackRect.centerX() + offset, mFillBackRect.top, mFillBackRect.centerX() + offset + step * qurt2, mFillBackRect.top);
        //7
        mPath.lineTo(mFillBackRect.right - ratios, mFillBackRect.top);
        mPath.quadTo(mFillBackRect.right, mFillBackRect.top, mFillBackRect.right, mFillBackRect.top + ratios);
        mPath.lineTo(mFillBackRect.right, mFillBackRect.bottom);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    private static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
