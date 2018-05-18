package com.view.dansesshou.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

/**
 * Created by dxs on 2018/5/15.
 */

public class VoiceView extends View {
    private RectF VoiceViewCenterRecf;
    private float recRatio = 0.0656f;//突出尖口占空间比例

    private Paint mPaintBack;//背景画笔
    private Paint mPaintLine;//白色线条

    private Path transPath;//三角形

    private int radues = 8;//圆角半径
    private float lineHRatio = 0.3125f;//线高占控件高比例
    private float lineH = 0;//线高
    private int lineW = 2;//线宽
    private float lineGrap = 0.0328f;//间隙占控件宽比例
    private float centX;
    private float centY;//水平中间线
    private float grap;//间隙
    private float offset = 0;//第一条线偏移量
    private int lineCount = 7;//线的个数
    private boolean isPlaying = false;
    private static float[] PAREMS_DEFALT = {1.0f, 0.8f, 0.6f, 0.4f, 0.6f, 0.8f, 1.0f};
    private float[] parems = new float[lineCount];//

    public VoiceView(Context context) {
        this(context, null);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private void initData(Context context) {
        VoiceViewCenterRecf = new RectF();
        radues = dip2px(context, radues);
        lineW = dip2px(context, lineW);
        mPaintBack = new Paint();
        mPaintBack.setAntiAlias(true);
        mPaintBack.setColor(Color.parseColor("#ffbf7f"));

        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStrokeCap(Paint.Cap.ROUND);
        mPaintLine.setStrokeWidth(lineW);
        mPaintLine.setColor(Color.parseColor("#ffffff"));
    }

    private Path getTrasPath(int w) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(recRatio * w * 2, 0);
        path.lineTo(recRatio * w * 2, radues * 2);
        path.close();
        return path;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        transPath = getTrasPath(w);
        lineH = h * lineHRatio;
        VoiceViewCenterRecf.set(w * recRatio, 0, w, h);
        initLineData();
    }

    private void initLineData() {
        centX = VoiceViewCenterRecf.centerX();
        centY = VoiceViewCenterRecf.centerY();
        grap = lineGrap * VoiceViewCenterRecf.width();
        offset = VoiceViewCenterRecf.left + (VoiceViewCenterRecf.width() - (lineCount * lineW + (lineCount - 1) * grap)) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawTrans(canvas);
        drawRecf(canvas);
        if (isPlaying) {
            getInitParems();
            drawVoice(canvas, parems);
            postInvalidateDelayed(150);
        } else {
            drawVoice(canvas, PAREMS_DEFALT);
        }
    }

    //绘制实心三角形
    private void drawTrans(Canvas canvas) {
        if (transPath != null) {
            canvas.drawPath(transPath, mPaintBack);
        }
    }

    //绘制实心矩形
    private void drawRecf(Canvas canvas) {
        canvas.drawRoundRect(VoiceViewCenterRecf, radues, radues, mPaintBack);
    }

    //绘制音波动画
    private void drawVoice(Canvas canvas, float[] parems) {
        // canvas.translate(getWidth() * recRatio, 0);
        for (int i = 0; i < lineCount; i++) {
            float off_i = i * (grap + lineW);
            float stopY = centY + lineH / 2 - lineH * parems[i];
            canvas.drawLine(offset + off_i, centY + lineH / 2, offset + off_i, stopY, mPaintLine);
        }
    }

    public void startPlay(boolean isPlaying) {
        this.isPlaying = isPlaying;
        invalidate();
    }

    //本想自动计算初始状态，后面觉得不需要这么通用，则放弃
    private void getInitParems() {
        for (int i = 0; i < parems.length; i++) {
            parems[i] = (float) Math.random();
        }
    }

    private static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
