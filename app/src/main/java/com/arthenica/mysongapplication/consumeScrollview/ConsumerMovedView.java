package com.arthenica.mysongapplication.consumeScrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public abstract class ConsumerMovedView extends View {

	protected IViewBoundChangeCallback callback;
	private boolean mOutOfParent = false;
	private boolean mLongPress = false;

	public ConsumerMovedView(Context context) {
		super(context);
	}

	public ConsumerMovedView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public ConsumerMovedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ConsumerMovedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	/**
	 * 设置View的长按状态
	 */
	abstract public void setStateLongPressed();

	public void setMoveOutOfParent(boolean enable) {
		mOutOfParent = enable;
	}

	public boolean getMoveOutOfParent() {
		return mOutOfParent;
	}

	public boolean isLongPress() {
		return mLongPress;
	}

	public void setLongPress(boolean currentIsLongPress) {
		this.mLongPress = currentIsLongPress;
	}

	public interface IViewBoundChangeCallback {

	}
}
