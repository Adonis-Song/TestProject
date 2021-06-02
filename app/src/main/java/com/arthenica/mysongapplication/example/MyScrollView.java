package com.arthenica.mysongapplication.example;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	private static final String TAG = "zhuo MyScrollView";
	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		Log.d(TAG, "onMeasure: width111 = " + width);
		if (getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
			if ((width != 0 || getMeasuredWidth() != 0)) {
				if (isFillViewport() && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
					if (width == 0) {
						width = getMeasuredWidth();
					}
					widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
					Log.d(TAG, "onMeasure: width222 = " + width);
					setMeasuredDimension(width, getMeasuredHeight());
				}
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
