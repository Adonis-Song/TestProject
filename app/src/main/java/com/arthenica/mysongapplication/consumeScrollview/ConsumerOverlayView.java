package com.arthenica.mysongapplication.consumeScrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.arthenica.mysongapplication.R;

public class ConsumerOverlayView extends View {


	public interface CaptionOverlayWindowListener {
		public void onClick(MotionEvent event);

		public void onWindowAdjust(int left, int right);

		public void onWindowAdjustFinish(int left, int right);
	}

	private static final String TAG = ConsumerOverlayView.class.getSimpleName();
	private static final int AUTO_ADJUST_POSITION_BIAS_DP = 3;

	private ConsumerOverlayView.CaptionOverlayWindowListener mCaptionOverlayWindowListener;
	private Rect mSelectedItemPosition;
	private int mSelectedConsumerBeanLeftWall;
	private int mSelectedConsumerBeanRightWall;
	private Rect mWindowBoundingRect;
	private boolean isDownInsideLeftHandle;
	private boolean isDownInsideRightHandle;
	private boolean mWindowResizing;
	private int mHorizonalPadding;
	private float mLastMotionX;
	private int mAutoAdjustMinBias;
	private int MIN_WINDOW_SIZE;

	public ConsumerOverlayView(@NonNull Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init();
	}

	public ConsumerOverlayView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ConsumerOverlayView(@NonNull Context context) {
		super(context);
		init();
	}

	private void init() {
		MIN_WINDOW_SIZE = getResources().getDimensionPixelOffset(R.dimen.min_caption_window_size);
		mAutoAdjustMinBias = getResources().getDimensionPixelOffset(R.dimen.AUTO_ADJUST_POSITION_BIAS_DP);
		mHorizonalPadding = getResources().getDimensionPixelOffset(R.dimen.caption_time_adjust_handler_width);
		mWindowBoundingRect = new Rect(0,0,0,0);
		mSelectedItemPosition = new Rect(0, 0, 0, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		mWindowBoundingRect.set(0,0,0,0);
		if (mSelectedItemPosition != null) {
			//Log.d("CaptionBP", "[OverlayView][onDraw] "+mSelectedConsumerBean.getStartTime()+" - "+mSelectedConsumerBean.getEndTime());
//			int startPosition = (int) (Math.ceil((float) mTimelineWidth * ((float) (mSelectedConsumerBean.getStartTime()) / mDuration)));
//			int endPosition = (int) (Math.ceil((float) mTimelineWidth * ((float) (mSelectedConsumerBean.getEndTime()) / mDuration)));
			Drawable drawable = mWindowResizing
					? ResourcesCompat.getDrawable(getResources(), R.drawable.bg_caption_item_overlay_active, null)
					: ResourcesCompat.getDrawable(getResources(), R.drawable.bg_caption_item_overlay, null);
			int left = mSelectedItemPosition.left - mHorizonalPadding;
			int right = mSelectedItemPosition.right + mHorizonalPadding;
			if (drawable != null) {
				drawable.setBounds(left, mSelectedItemPosition.top, right, mSelectedItemPosition.bottom);
				drawable.draw(canvas);
			}

			mWindowBoundingRect.set(left, mSelectedItemPosition.top, right, mSelectedItemPosition.bottom);
			Log.d(TAG, "setSelectedConsumerBean: mWindowBoundingRect = " + mWindowBoundingRect);
		}
	}

	public void setSelectedConsumerBean(Rect position, int leftWall, int rightWall) {
		Log.d(TAG, "setSelectedConsumerBean: position = " + position);
		mSelectedItemPosition = position;
		mSelectedConsumerBeanLeftWall = leftWall;
		mSelectedConsumerBeanRightWall = rightWall;
		invalidate();
	}

	public void setCaptionOverlayWindowListener(ConsumerOverlayView.CaptionOverlayWindowListener listener) {
		mCaptionOverlayWindowListener = listener;
	}

	private boolean checkDownInsideHandle(MotionEvent event) {
		int handlerWidth = mHorizonalPadding;
		isDownInsideLeftHandle = false;
		isDownInsideRightHandle = false;
		if (event.getX() >= mWindowBoundingRect.left
				&& event.getX() <= mWindowBoundingRect.left + handlerWidth) {
			isDownInsideLeftHandle = true;
		} else if (event.getX() >= mWindowBoundingRect.right - handlerWidth
				&& event.getX() <= mWindowBoundingRect.right) {
			isDownInsideRightHandle = true;
		}
		return isDownInsideLeftHandle || isDownInsideRightHandle;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "onTouchEvent: action = " + event.getAction());
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionX = event.getX();
				if (mSelectedItemPosition != null) {
					// draw state_pressed background
					invalidate();
					// update insideHandle flag
					if (checkDownInsideHandle(event)) {
						getParent().requestDisallowInterceptTouchEvent(true);
					}
					return true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (isDownInsideLeftHandle || isDownInsideRightHandle) {
					mWindowResizing = true;
					processCaptionDurationResizing(event);
					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mWindowResizing) {
					mWindowResizing = false;
					processCaptionDurationResizing(event);
				} else {
					mCaptionOverlayWindowListener.onClick(event);
				}
				invalidate();
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
		}
		//所有传递到View的操作都需要消费
		return true;
	}

	/**
	 * 处理时间轴的调节
	 * @param event
	 */
	private void processCaptionDurationResizing(MotionEvent event) {
		int newLeft = mSelectedItemPosition.left;
		int newRight = mSelectedItemPosition.right;
		if (isDownInsideLeftHandle) {
			newLeft = (int) (event.getX() - mLastMotionX + mSelectedItemPosition.left);
/*			if (mCurrentAutoPosition > 0 && Math.abs(newLeft - mCurrentAutoPosition) <= mAutoAdjustMinBias) {
				newLeft = mCurrentAutoPosition - 1;//自动对齐光标之后能把字幕显示出来
			}*/
			if (newRight - newLeft < MIN_WINDOW_SIZE) {
				newLeft = newRight - MIN_WINDOW_SIZE;
			}
			newLeft = Math.max(0, newLeft);
			newLeft = Math.max(mSelectedConsumerBeanLeftWall, newLeft);
		} else if (isDownInsideRightHandle) {
			newRight = (int) (event.getX() - mLastMotionX + mSelectedItemPosition.right);
/*			if (mCurrentAutoPosition > 0 && Math.abs(newRight - mCurrentAutoPosition) <= mAutoAdjustMinBias) {
				newRight = mCurrentAutoPosition + 1;//自动对齐光标之后能把字幕显示出来
			}*/
			if (newRight - newLeft < MIN_WINDOW_SIZE) {
				newRight = newLeft + MIN_WINDOW_SIZE;
			}
			newRight = Math.min(newRight, getWidth());
			newRight = Math.min(newRight, mSelectedConsumerBeanRightWall);
		}
		// send event to fragment
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			mCaptionOverlayWindowListener.onWindowAdjust(newLeft, newRight);
			mSelectedItemPosition.left = newLeft;
			mSelectedItemPosition.right = newRight;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			mCaptionOverlayWindowListener.onWindowAdjustFinish(newLeft, newRight);
			mSelectedItemPosition.left = newLeft;
			mSelectedItemPosition.right = newRight;
		}
		mLastMotionX = event.getX();
	}

}
