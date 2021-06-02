package com.arthenica.mysongapplication.consumeScrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.core.view.NestedScrollingParent;
import androidx.core.widget.NestedScrollView;

public class ConsumerMyScroller extends HorizontalScrollView {

	private int mScrollStartPosition;

	private static final String TAG = "ConsumerViewMyScroller";
	public ConsumerMyScroller(Context context) {
		super(context);
		init();
	}



	public ConsumerMyScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ConsumerMyScroller(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ConsumerMyScroller(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		mScrollStartPosition = 0;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean intercept = super.onInterceptTouchEvent(ev);
		Log.d(TAG, "onInterceptTouchEvent: intercept = " + intercept);
		return intercept;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean touch = super.onTouchEvent(ev);
		Log.d(TAG, "onTouchEvent: touch = " + touch);
		return touch;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		mScrollStartPosition = l;
	}

	public int getScrollStartPosition() {
		return mScrollStartPosition;
	}
}
