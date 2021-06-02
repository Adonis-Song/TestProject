package com.arthenica.mysongapplication.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arthenica.mysongapplication.R;

public class SeekBarThumbDrawable extends Drawable {
    private Paint mBoundPaint;
    private Paint mTextPaint;
    private int mBoundRadius;
    private int mTextBottomPadding;
    private int mTextHeight;
    private int mTextWidth;
    private String mText;

    public SeekBarThumbDrawable(Context context) {
        mBoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int mBoundColor = context.getResources().getColor(R.color.colorBackgroundWhite, context.getTheme());
        mBoundPaint.setColor(mBoundColor);
        mBoundRadius = (int)context.getResources().getDimension(R.dimen.seek_bar_thumb_bound_radius);

        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextBottomPadding = (int)context.getResources().getDimension(R.dimen.seek_bar_thumb_text_bottom_padding);
        mTextPaint.setColor(context.getResources().getColor(R.color.colorTextWhite, context.getTheme()));
        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextSize((int) context.getResources().getDimension(R.dimen.seek_bar_thumb_text_size));
        Rect rect = new Rect();
        mTextPaint.getTextBounds("1", 0, 1, rect);
        mTextHeight = rect.height();

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawCircle(getBounds().left,
                getBounds().top,
                mBoundRadius,
                mBoundPaint);

        if (mText != null) {
            canvas.drawText(mText,
                    getBounds().left - mTextWidth / 2,
                    getBounds().top - mBoundRadius - mTextBottomPadding - mTextHeight,
                    mTextPaint);
        }

    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public void setText(String text) {
        this.mText = text;
        if (text != null) {
            mTextWidth = (int)mTextPaint.measureText(text);
        }
    }

    public void setCircleRadius(int radius) {
        mBoundRadius = radius;
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
    }

    public void setTextSize(int size) {
        mTextPaint.setTextSize(size);
    }

}
