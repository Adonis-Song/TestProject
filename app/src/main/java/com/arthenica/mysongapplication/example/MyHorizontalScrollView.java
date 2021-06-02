package com.arthenica.mysongapplication.example;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {
	private static final String TAG = "Zhuo MyHorizontal";
	public MyHorizontalScrollView(Context context) {
		super(context);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		Log.d(TAG, "onMeasure: width = " + getMeasuredWidth() + "  height =  " + getMeasuredHeight());

	}

/*	@Override
	protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
		if (isFillViewport()) {
			final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
					getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
							+ widthUsed, lp.width);
			final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
					getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin
							+ heightUsed, lp.height);

			final int usedTotal = getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin +
					widthUsed;
			final int parentSize = Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - usedTotal);
			childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - usedTotal), MeasureSpec.UNSPECIFIED);
			child.setMinimumWidth(parentSize);
			Log.d(TAG, "measureChildWithMargins: parentSize = " + parentSize);
			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
			return;
		}

		super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
	}*/
}
