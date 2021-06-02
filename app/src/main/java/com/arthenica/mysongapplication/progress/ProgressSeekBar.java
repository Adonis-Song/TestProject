package com.arthenica.mysongapplication.progress;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import rx.functions.Func1;

/**
 * 显示各种样式的progressbar
 */
public class ProgressSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {
    private Func1<Integer, String> mStyleFun;
    public ProgressSeekBar(Context context) {
        super(context);
        init(context);
    }

    public ProgressSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setThumb(new SeekBarThumbDrawable(context));
    }

    @Override
    public void invalidate() {
        super.invalidate();
        Drawable thumb = getThumb();
        if (thumb instanceof SeekBarThumbDrawable) {
            String text = mStyleFun != null ? mStyleFun.call(getProgress()) : String.valueOf(getProgress());
            ((SeekBarThumbDrawable) thumb).setText(text);
        }
    }

    public void setStyle(Func1<Integer, String> style) {
        mStyleFun = style;
    }

    public void setTextStyle(int size, int color) {
        Drawable thumb = getThumb();
        if (thumb instanceof SeekBarThumbDrawable) {
            SeekBarThumbDrawable drawable =  ((SeekBarThumbDrawable) thumb);
            drawable.setTextSize(size);
            drawable.setTextColor(color);
        }
    }

    public void setCircleRadius(int radius) {
        Drawable thumb = getThumb();
        if (thumb instanceof SeekBarThumbDrawable) {
            SeekBarThumbDrawable drawable =  ((SeekBarThumbDrawable) thumb);
            drawable.setCircleRadius(radius);
        }
    }
}
