package com.arthenica.mysongapplication.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.arthenica.mysongapplication.DeviceInfoUtil;

public class CaptionMoveView extends View {
    private static final String TAG = "CaptionMoveView";
    private String mText;
    private Paint mNormalTextPaint;
    private Paint mHighlightTextPaint;
    private int mNormalBackgroundColor;
    private int mLongPressBackgroundColor;
    private int mSinglePressBackgroundColor;
    private int mTextPosLeft;
    private int mTextBaseLine;

    private int x1;
    private int x2;
    private int x3;
    private int x4;

    public CaptionMoveView(Context context) {
        super(context);
        init();
    }

    public CaptionMoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CaptionMoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CaptionMoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mNormalTextPaint = new Paint();
        mNormalTextPaint.setColor(Color.RED);
        mNormalTextPaint.setTextAlign(Paint.Align.LEFT);
        mHighlightTextPaint = new Paint();
        mHighlightTextPaint.setColor(Color.BLACK);
        mHighlightTextPaint.setTextAlign(Paint.Align.LEFT);
        mNormalBackgroundColor = Color.GREEN;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float height = getMeasuredHeight() / 2f;
        mNormalTextPaint.setTextSize(height);
        mHighlightTextPaint.setTextSize(height);
        Paint.FontMetricsInt fontMetrics = mNormalTextPaint.getFontMetricsInt();
        mTextPosLeft = DeviceInfoUtil.dpToPx(50);
        mTextBaseLine = getMeasuredHeight() / 2 - fontMetrics.ascent / 2;
        x1 = getMeasuredHeight() / 2 + fontMetrics.ascent / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mNormalBackgroundColor);
        canvas.drawLine(mTextPosLeft, mTextBaseLine, getWidth(), mTextBaseLine, mHighlightTextPaint);
        canvas.drawLine(mTextPosLeft, x1, getWidth(), x1, mHighlightTextPaint);
        canvas.drawText(mText, mTextPosLeft, mTextBaseLine, mNormalTextPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setBackgroundColor(int normalColorRes, int longPressColorRes, int singlePressColorRes) {
        mNormalBackgroundColor = ContextCompat.getColor(getContext(), normalColorRes);
        mLongPressBackgroundColor = ContextCompat.getColor(getContext(), longPressColorRes);
        mSinglePressBackgroundColor = ContextCompat.getColor(getContext(), singlePressColorRes);
    }


    public void setText(String text) {
        this.mText = text;
    }
}
