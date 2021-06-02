package com.arthenica.mysongapplication.example;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class MyView extends View {
	private static final String TAG = "MyView zhuo";
	public MyView(Context context) {
		super(context);
	}

	public MyView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
/*		Log.d(TAG, "onMeasure: " + getMeasuredWidth());
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
			if (getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
				int width = MeasureSpec.getSize(widthMeasureSpec);
				if(width == 0) {
					setMeasuredDimension(getMeasuredWidth(), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));

				} else {
//					setMeasuredDimension(width, );
				}
			}
		}*/
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d(TAG, "MyScrollView onMeasure: width = " + getMeasuredWidth());

	}
}
