package com.arthenica.mysongapplication.example;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MyViewGroup extends RelativeLayout {
	private static final String TAG = "Zhuo MyViewGroup";
	public MyViewGroup(Context context) {
		super(context);
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(TAG, "onMeasure: MyScrollView");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		Log.d(TAG, "onMeasure: width = " + getMeasuredWidth() + "  height =  " + getMeasuredHeight());
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
			final int size = getChildCount();
			for (int i = 0; i < size; i++) {
				final View view = getChildAt(i);
				LayoutParams lp  = (LayoutParams) view.getLayoutParams();
				if (lp.width == LayoutParams.MATCH_PARENT) {
					view.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.UNSPECIFIED),
							getChildMeasureSpec(heightMeasureSpec,
							getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height));
				}
			}
		}

	}


	@Override
	protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
		super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);

		Log.d(TAG, "measureChildWithMargins: ");
	}
}
