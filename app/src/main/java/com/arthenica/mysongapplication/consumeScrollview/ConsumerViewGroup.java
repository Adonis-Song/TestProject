package com.arthenica.mysongapplication.consumeScrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.arthenica.mysongapplication.DeviceInfoUtil;
import com.arthenica.mysongapplication.R;

public class ConsumerViewGroup extends FrameLayout {

	private static final String TAG = "ConsumerViewGroup";
	private final int STATE_LONG_CLICK = 0x01;
	private final int STATE_CHOSEN_ITEM = 0x02;
	private final int STATE_ADJUST_ITEM = 0x04;
	private final int STATE_SELECTED = 0x08;
	private IHorizontalScroll mScrollCallback;

	private GestureDetector mGestureDetector;
	//当前操作的子View  操作包括长按和点击
	private ConsumerMovedView mCapturedView;
	//调整CaptureView的边界使用
	private ConsumerOverlayView mConsumerOverlayView;
	private int mStateFlag;
	private int mLastMotionX;
	private int mLastMotionY;
	private int mScrollIntervalSpace;
	private int mScrollBias;
	private int mDefaultHorizontalPadding;

	public ConsumerViewGroup(Context context) {
		super(context);
		init();
	}

	public ConsumerViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ConsumerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ConsumerViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		mStateFlag = 0;
		mDefaultHorizontalPadding = DeviceInfoUtil.getScreenWidthPx(getContext()) / 2;
		mScrollIntervalSpace = (int) getResources().getDimension(R.dimen.interval_space_to_scroll);
		mScrollBias = (int) getResources().getDimension(R.dimen.scroll_bias_horizontal);
//		setWillNotDraw(false);
		mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				Log.d(TAG, "onSingleTapUp: ");
				if ((mStateFlag & STATE_CHOSEN_ITEM) == 0) {
					ConsumerMovedView view = findBottomChildUnder((int) e.getX(), (int) e.getY());
					if (view != null) {
						setSingleChooseView(view);
//						invalidate();
					}
				}
				return false;
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				return false;
			}

			@Override
			public void onLongPress(MotionEvent ev) {
				Log.d(TAG, "onLongPress: ");
				ConsumerMovedView capturedView = findTopChildUnder((int) ev.getX(), (int) ev.getY());
				if (capturedView != null) {
					if ((mStateFlag & STATE_CHOSEN_ITEM) != 0) {
						clearConsumerOverLayView();
					}
					mLastMotionX = (int) ev.getX();
					mLastMotionY = (int) ev.getY();
					mCapturedView = capturedView;
					mStateFlag |= STATE_LONG_CLICK;
					mCapturedView.setLongPress(true);
					notifyStateToSelected();
				}
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				return false;
			}
		});
	}



	public void setScrollCallback(IHorizontalScroll scrollCallback) {
		this.mScrollCallback = scrollCallback;
	}

	private void addCapturedOverlayView(View view) {
		Log.d(TAG, "addCapturedOverlayView: ");
/*		TimeToWidth.getInstance().setDuration(5000);
		TimeToWidth.getInstance().setAllWidth(getWidth() - getPaddingLeft() - getPaddingRight());*/
		if (mConsumerOverlayView == null) {
			mConsumerOverlayView = new ConsumerOverlayView(getContext());
			mConsumerOverlayView.setCaptionOverlayWindowListener(new ConsumerOverlayView.CaptionOverlayWindowListener() {
				@Override
				public void onClick(MotionEvent event) {
					ConsumerMovedView view = findBottomChildUnder((int) event.getX(), (int) event.getY());
					if (view != null && mCapturedView != view) {
						mCapturedView = view;
						mConsumerOverlayView.setSelectedConsumerBean(new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()), getHorizontalLeftPadding(), getWidth() - getHorizontalRightPadding());
						mConsumerOverlayView.bringToFront();
						mConsumerOverlayView.requestLayout();
					} else {
						clearConsumerOverLayView();
					}
				}

				@Override
				public void onWindowAdjust(int left, int right) {
					mStateFlag |= STATE_ADJUST_ITEM;
					if (mCapturedView != null) {
						LayoutParams lp = (LayoutParams) mCapturedView.getLayoutParams();
						lp.width = right - left;
						lp.leftMargin = left;
						mCapturedView.setLayoutParams(lp);
					}
				}

				@Override
				public void onWindowAdjustFinish(int left, int right) {
					mStateFlag &= ~STATE_ADJUST_ITEM;
				}
			});
			LayoutParams lp = new LayoutParams(getWidth(), getHeight());
			addView(mConsumerOverlayView, 0, lp);
			mConsumerOverlayView.setTag(mConsumerOverlayView);
		} else {
			mConsumerOverlayView.setVisibility(View.VISIBLE);
			LayoutParams lp = (LayoutParams) mConsumerOverlayView.getLayoutParams();
			if (lp.width != getWidth() || lp.height != getHeight()) {
				lp.width = getWidth();
				lp.height = getHeight();
				mConsumerOverlayView.setLayoutParams(lp);
			}
		}
		mConsumerOverlayView.setSelectedConsumerBean(new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()), getHorizontalLeftPadding(), getWidth() - getHorizontalRightPadding());
		mConsumerOverlayView.bringToFront();

	}

	private void clearConsumerOverLayView() {
		mCapturedView = null;
		mConsumerOverlayView.setVisibility(View.GONE);
		mStateFlag &= ~STATE_CHOSEN_ITEM;
	}

	private int getHorizontalLeftPadding() {
		return mDefaultHorizontalPadding;
	}

	private int getHorizontalRightPadding() {
		return mDefaultHorizontalPadding;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG, "onDraw: ");
		super.onDraw(canvas);
/*		canvas.save();
		Drawable drawable = null;
		if ((mStateFlag & STATE_SELECT_ITEM) != 0) {
			drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.bg_caption_item_overlay, null);
		} else if ((mStateFlag & STATE_ADJUST_ITEM) != 0) {
			drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.bg_caption_item_overlay_active, null);
		}
		if (drawable != null && mCapturedView != null) {
			Log.d(TAG, "onDraw: ");
			drawable.setBounds(mCapturedView.getLeft() - getResources().getDimensionPixelOffset(R.dimen.caption_time_adjust_handler_width), 0,
					mCapturedView.getRight() + getResources().getDimensionPixelOffset(R.dimen.caption_time_adjust_handler_width), getHeight());
			drawable.draw(canvas);
		}*/
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(TAG, "onMeasure: ");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.d(TAG, "onLayout: ");
		super.onLayout(changed, l, t, r, b);
	}


	@Override
	public void requestLayout() {
		Log.d(TAG, "requestLayout: ");
		super.requestLayout();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.d(TAG, "dispatchTouchEvent: ev action = " + ev.getAction());
		mGestureDetector.onTouchEvent(ev);
/*		if ((mStateFlag & STATE_SELECT_ITEM) != 0) {
			if (getChildCount() > 1) {
				View view = getChildAt(0);

			}
		}*/
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.d(TAG, "onInterceptTouchEvent: ");
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			if (mCapturedView != null && (mStateFlag & STATE_LONG_CLICK) != 0) {
				Log.d(TAG, "onInterceptTouchEvent: intercept");
				getParent().requestDisallowInterceptTouchEvent(true);
				return true;
			}
		} else if ((mStateFlag & STATE_CHOSEN_ITEM) == 0) {//当前状态是调节view大小的时候，不取消disallow效果
			Log.d(TAG, "onInterceptTouchEvent: not intercept");
			getParent().requestDisallowInterceptTouchEvent(false);
		}
		return super.onInterceptTouchEvent(ev);
//		return mDragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		mDragHelper.processTouchEvent(event);
		Log.d(TAG, "onTouchEvent:  = " + event.getAction());

		if ((mStateFlag & STATE_LONG_CLICK) != 0) {
			//长按状态处理事件
			processLongTouchEvent(event);
		} else if ((mStateFlag & STATE_CHOSEN_ITEM) != 0) {
			//选中item，进行拖动状态进行消费
//			processAdjustItemEvent(event);
			return super.onTouchEvent(event);
		} else if (event.getActionMasked() != MotionEvent.ACTION_DOWN) {
			//所有Action_DOWN事件都必须消费，因为mGestureDetector会进行长按检测，如果没有消费，就会出现判断失误
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			mCapturedView = findTopChildUnder(x, y);
			mLastMotionX = x;
			mLastMotionY = y;
		} else {
			return super.onTouchEvent(event);
		}
		return true;
	}

	private void processLongTouchEvent(MotionEvent event) {
		Log.d(TAG, "processLongTouchEvent: ");
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int biasX = (int) (x - mLastMotionX);
		final int biasY = (int) (y - mLastMotionY);
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				mCapturedView = findTopChildUnder(x, y);
				mLastMotionX = x;
				mLastMotionY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				mLastMotionX = x;
				mLastMotionY = y;
/*				int rawX = (int) event.getRawX();
				if (rawX < getResources().getDimensionPixelSize(R.dimen.board_padding)) {
					Log.d(TAG, "onTouchEvent: ");
				}*/
				dragTo(mCapturedView.getLeft() + biasX, mCapturedView.getTop() + biasY, biasX, biasY);
				checkEventToScroll(event);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				dragTo(mCapturedView.getLeft() + biasX, mCapturedView.getTop() + biasY, biasX, biasY);
				saveCapturedLocation();
				mCapturedView.setLongPress(false);
				mCapturedView = null;
				mStateFlag &= ~STATE_LONG_CLICK;
				break;
		}
	}

	private void checkEventToScroll(MotionEvent e) {
		if(DeviceInfoUtil.getScreenWidthPx(getContext()) - e.getRawX() < mScrollIntervalSpace || e.getRawX() < mScrollIntervalSpace) {
			mScrollCallback.scrollBy(mScrollBias);
		}
	}

	private void saveCapturedLocation() {
		FrameLayout.LayoutParams lp = (LayoutParams) mCapturedView.getLayoutParams();
		lp.leftMargin = mCapturedView.getLeft() - getPaddingLeft();
		lp.topMargin = mCapturedView.getTop();
		mCapturedView.setLayoutParams(lp);
	}

	@Nullable
	private ConsumerMovedView findTopChildUnder(int x, int y) {
		final int childCount = getChildCount();
		for (int i = childCount - 1; i >= 0; i--) {
			final View child = getChildAt(i);
			if (x >= child.getLeft() && x < child.getRight()
					&& y >= child.getTop() && y < child.getBottom()) {
				if (!checkWhetherChosen(child)) {
					continue;
				}
				return (ConsumerMovedView) child;
			}
		}
		return null;
	}

	@Nullable
	private ConsumerMovedView findBottomChildUnder(int x, int y) {
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			if (x >= child.getLeft() && x < child.getRight()
					&& y >= child.getTop() && y < child.getBottom()) {
				if (!checkWhetherChosen(child)) {
					continue;
				}
				return (ConsumerMovedView) child;
			}
		}
		return null;
	}

	private void dragTo(int left, int top, int dx, int dy) {
		final int oldLeft = mCapturedView.getLeft();
		final int oldTop = mCapturedView.getTop();
		if (dx != 0) {
			ViewCompat.offsetLeftAndRight(mCapturedView, left - oldLeft);
		}
		if (dy != 0) {
//			ViewCompat.offsetTopAndBottom(mCapturedView, top - oldTop);
		}
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		Log.d(TAG, "addView: ");
		if (child instanceof ConsumerView) {
			((LayoutParams) params).leftMargin = mDefaultHorizontalPadding;
		}
		super.addView(child, index, params);
	}


	@Override
	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
		Log.d(TAG, "requestDisallowInterceptTouchEvent: disallowIntercept = " + disallowIntercept);
		super.requestDisallowInterceptTouchEvent(disallowIntercept);
	}

	public interface IHorizontalScroll {
		void scrollBy(int biasX);
		int getCenterPosition();
		void stateSelectedChange(boolean selected);
	}


	/**
	 * 设置当前view是选中状态（选中状态背景变粉色）
	 * @param chooseCenterView 是否选中当前居中的在中心的view
	 */
	public void setStateToSelected(boolean chooseCenterView) {
		mStateFlag = STATE_SELECTED;
		setBackgroundColor(getResources().getColor(R.color.colorBackgroundPinkSong, null));
		if (chooseCenterView) {
			int centerPosition = getCenterPosition();
			ConsumerMovedView view = findCenterView(centerPosition);
			if (view != null) {
				setSingleChooseView(view);
			}
		}
	}

	private ConsumerMovedView findCenterView(int centerPosition) {
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			if (centerPosition >= child.getLeft() && centerPosition < child.getRight()) {
				if (!checkWhetherChosen(child)) {
					continue;
				}
				return (ConsumerMovedView) child;
			}
		}
		return null;
	}


	private boolean checkWhetherChosen(View view) {
		return view instanceof ConsumerMovedView;
	}

	public void cancelSelected() {
		mStateFlag = 0;
		if (mConsumerOverlayView != null) {
			clearConsumerOverLayView();
		}
		setBackgroundColor(getResources().getColor(R.color.colorBackgroundGraySong, null));
	}

	/**
	 * 向外界寻找中心的位置 ，  需要加上padding
	 * @return
	 */
	private int getCenterPosition() {
		int centerPosition = mScrollCallback.getCenterPosition();
		if (centerPosition < 0) {
			return -1;
		} else {
			return centerPosition;
		}
	}

	private void setSingleChooseView(ConsumerMovedView view) {
		mStateFlag = STATE_CHOSEN_ITEM;
		mCapturedView = view;
		addCapturedOverlayView(view);
		notifyStateToSelected();
		requestDisallowInterceptTouchEvent(true);
	}

	private void notifyStateToSelected() {
		if ((mStateFlag & STATE_SELECTED) == 0 ) {//还没有被通知状态改变，现在要改变状态
			setBackgroundColor(getResources().getColor(R.color.colorBackgroundPinkSong, null));
			mScrollCallback.stateSelectedChange(true);
		}
	}


}
