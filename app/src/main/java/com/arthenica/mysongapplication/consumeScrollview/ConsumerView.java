package com.arthenica.mysongapplication.consumeScrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.arthenica.mysongapplication.R;

public class ConsumerView extends ConsumerMovedView {
	float downX;
	float downY;
	private static final String TAG = "ConsumerView";
	public ConsumerView(Context context) {
		super(context);
		init();
	}

	public ConsumerView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ConsumerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ConsumerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	@Override
	public void setStateLongPressed() {

	}

	private void init() {
		setClickable(true);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(TAG, "onMeasure: ");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		Log.d(TAG, "onLayout: left = " + left + "  top = " + top + "  right = " + right + "  bottom = " + bottom);
//		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG, "onDraw: ");
		canvas.drawRGB(0x00, 0xFF, 0x00);
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "onTouchEvent: action = " + event.getAction());
/*		Log.d(TAG, "onTouchEvent: action = " + event.getAction());
		int biasX;
		int biasY;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = event.getRawX();
				downY = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				biasX = (int) (event.getRawX() - downX);
				biasY = (int) (event.getRawY() - downY);
//				layout((int) (getLeft() + biasX), (int) (getTop() + biasY) ,(int) (getRight() + biasX), (int) (getBottom() + biasY));
//				setTranslationX();
				scrollBy(biasX, biasY);
*//*				downX = event.getRawX();
				downY = event.getRawY();*//*
				Log.d(TAG, "onTouchEvent: biasX = " + biasX + "  bisY = " + biasY);

				break;
			case MotionEvent.ACTION_UP:
				biasX = (int) (event.getRawX() - downX);
				biasY = (int) (event.getRawY() - downY);
//				layout((int) (getLeft() + biasX), (int) (getTop() + biasY) ,(int) (getRight() + biasX), (int) (getBottom() + biasY));
*//*				downX = event.getRawX();
				downY = event.getRawY();*//*
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
				if (lp != null) {
					lp.leftMargin = lp.leftMargin + biasX;
					lp.topMargin = lp.topMargin + biasY;
				}
				Log.d(TAG, "onTouchEvent: biasX = " + biasX + "  bisY = " + biasY);

				getParent().requestLayout();
				break;
		}*/

		boolean is = super.onTouchEvent(event);
		Log.d(TAG, "onTouchEvent:  = " + is);
		return is;
//		return true;
	}

}
