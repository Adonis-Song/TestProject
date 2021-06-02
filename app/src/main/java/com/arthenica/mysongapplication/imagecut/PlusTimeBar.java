package com.arthenica.mysongapplication.imagecut;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.util.Assertions;

public class PlusTimeBar extends DefaultTimeBar {
	private static final String TAG = "PlusTimeBar";
	private OnScrubListener mOriginListener;
	public PlusTimeBar(Context context) {
		super(context);
	}

	public PlusTimeBar(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public PlusTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PlusTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, @Nullable AttributeSet timebarAttrs) {
		super(context, attrs, defStyleAttr, timebarAttrs);
	}

	public PlusTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, @Nullable AttributeSet timebarAttrs, int defStyleRes) {
		super(context, attrs, defStyleAttr, timebarAttrs, defStyleRes);
	}

	@Override
	public void addListener(OnScrubListener listener) {
		Assertions.checkNotNull(listener);
		if (listener instanceof Player.EventListener) {
			mOriginListener = listener;
		} else {
			super.addListener(listener);
		}
	}
}
