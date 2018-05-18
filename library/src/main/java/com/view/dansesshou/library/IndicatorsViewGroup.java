package com.view.dansesshou.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dxs on 2018/5/16.
 */

public class IndicatorsViewGroup extends ViewGroup {
    public final static int STATE_HIDDEN=0;
    public final static int STATE_SHOW=1;
    private Path mPath;
    private Path mPathArrow;
    private Rect mBackRect;
    private Rect mFillBackRect;//填充区域//整体的一半
    private int ratios = 10;
    private Paint mPaint;
    private float tarRadio = 0.075f;//缺口比例（与宽）
    private float arrowRadio = 0.04f;//箭头比例（与宽）
    private float closeRadio = 0.042f;//关闭叉的宽比例（与宽）
    private int tarH = 0;//缺口深度（默认与缺口等长）
    private final float qurt2 = 1.4142f;
    private float progress;
    private int arrowTop = 4;
    private ArrowsView arrowsView;
    private CloseView closeView;

    //如果要扩大点击区域，则可将此参数放入视图内部，这里点击区域是整个控件，暂时不需要移动
    private int closePadding = 20;

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
        mPathArrow = new Path();
        mBackRect = new Rect();
        mFillBackRect = new Rect();
        arrowsView = new ArrowsView(context);
//        closeView=new CloseView(context);

        ratios = dip2px(context, ratios);
        closePadding = dip2px(context, closePadding);
        arrowTop = dip2px(context, arrowTop);

        setBackgroundColor(Color.TRANSPARENT);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#efefef"));
        addView(arrowsView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        getDrawingRect(mBackRect);
        mFillBackRect.set(mBackRect.left, mBackRect.bottom / 2, mBackRect.right, mBackRect.bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int childW = MeasureSpec.makeMeasureSpec((int) (w * arrowRadio), MeasureSpec.EXACTLY);
        int child_w = MeasureSpec.makeMeasureSpec((int) (w * closeRadio), MeasureSpec.EXACTLY);
        getChildAt(0).measure(childW, heightMeasureSpec);
        View close = getChildAt(1);
        if (close != null) {
            close.measure(child_w, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = r - l;
        int h = b - t;
        View view = getChildAt(0);
        //0.21是缺口高与高的比例
        int offset = (int) (0.21 * h * progress);
        view.layout((int) (w - view.getMeasuredWidth()) / 2, h / 2 + arrowTop - offset, (int) ((w + view.getMeasuredWidth()) / 2), (int) (h / 2 + arrowTop + view.getMeasuredHeight() - offset));
        View close = getChildAt(1);
        if (close != null) {
            close.layout(w - closePadding - close.getMeasuredWidth(), closePadding, w - closePadding, closePadding + close.getMeasuredHeight());
        }
    }

    public void setShowProgress(float progress) {
        this.progress = progress;
        mFillBackRect.set(mBackRect.left, (int) (mBackRect.bottom / 2 - progress * mBackRect.height() / 2), mBackRect.right, mBackRect.bottom);
        arrowsView.setShowProgress(progress);
        requestLayout();
    }

    /**
     * 附带布局显示状态
     * @param state {@link #STATE_HIDDEN {@link #STATE_SHOW}}
     */
    public void showOrHidenSpecialView(int state) {
        if (state == STATE_SHOW) {
            if (closeView == null) {
                closeView = new CloseView(getContext());
            }
            addView(closeView);
        } else {
            //Android 内部已经优化
            if (closeView != null) {
                removeView(closeView);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBackPath(canvas);
    }

    private void initBackPath(Canvas canvas) {
        float offset = mFillBackRect.width() * tarRadio / 2;
        float step = offset / 3;
        float tempProgress = (progress - 0.5f) * 2;
        mPath.rewind();
        //+1解决精度问题
        mPath.moveTo(mFillBackRect.left, mFillBackRect.bottom+1);
        mPath.lineTo(mFillBackRect.left, mFillBackRect.top + ratios);
        mPath.quadTo(mFillBackRect.left, mFillBackRect.top, ratios, mFillBackRect.top);
        //1
        mPath.lineTo(mFillBackRect.centerX() - offset - step * qurt2, mFillBackRect.top);
        //2
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
        mPath.lineTo(mFillBackRect.right, mFillBackRect.bottom+1);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
