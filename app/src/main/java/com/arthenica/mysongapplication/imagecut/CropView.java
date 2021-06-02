package com.arthenica.mysongapplication.imagecut;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.arthenica.mysongapplication.Config;


public class CropView extends View {
	private static final int CROP_BORD_WIDTH = 1;
	private static final int CROP_CORNER_WIDTH = 2;
	private static final int CROP_CORNER_HEIGHT = 10;
	private static final int CROP_CORNER_CLICK_AREA = 10;
	private static final int CROP_HORIZONTAL_PADDING = 4;
	private static final int CROP_VERTICAL_PADDING = 4;
	private Rect mCropRect;
	private Paint mBackgroundPaint;
	private Paint mCropPaint;
	private Paint mCropPressPaint;
	private Paint mCropCornerPaint;
	private int mCropCornerWidth;
	private int mCropCornerHeight;
	private int mCropCornerClickArea;
	private int mCropHorizontalPadding;
	private int mCropVerticalPadding;
	private MoveMode mMode;
	private int mDownX;
	private int mDownY;
	private boolean mPressed;

	public CropView(Context context) {
		super(context);
		init();
	}

	public CropView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		mCropCornerWidth = Config.dpToPx(CROP_CORNER_WIDTH);
		mCropCornerHeight = Config.dpToPx(CROP_CORNER_HEIGHT);
		mCropCornerClickArea = Config.dpToPx(CROP_CORNER_CLICK_AREA);
		mCropHorizontalPadding = Config.dpToPx(CROP_HORIZONTAL_PADDING);
		mCropVerticalPadding = Config.dpToPx(CROP_VERTICAL_PADDING);
		mCropRect = new Rect();
		mBackgroundPaint = new Paint();
		mBackgroundPaint.setAlpha(20);
		mCropPaint = new Paint();
		mCropPaint.setStrokeWidth(Config.dpToPx(CROP_BORD_WIDTH));
		mCropPaint.setStyle(Paint.Style.STROKE);
		mCropPaint.setColor(Color.WHITE);
		mCropPaint.setAntiAlias(true);
		mCropPressPaint = new Paint();
		mCropPressPaint.setStrokeWidth(Config.dpToPx(CROP_BORD_WIDTH * 2));
		mCropPressPaint.setStyle(Paint.Style.STROKE);
		mCropPressPaint.setColor(Color.WHITE);
		mCropPaint.setAntiAlias(true);
		mCropCornerPaint = new Paint();
		mCropCornerPaint.setStyle(Paint.Style.FILL);
		mCropCornerPaint.setColor(Color.WHITE);
		mCropCornerPaint.setAntiAlias(true);
		mPressed = false;
		mMode = MoveMode.CROP_NULL;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mCropRect.left = getMeasuredWidth() / 4;
		mCropRect.top = getMeasuredHeight() / 4;
		mCropRect.right = getMeasuredWidth() / 4 * 3;
		mCropRect.bottom = getMeasuredHeight() / 4 * 3;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//绘制蒙层
		//左边
		canvas.drawRect(0, 0, mCropRect.left, getHeight(), mBackgroundPaint);
		//上面
		canvas.drawRect(mCropRect.left, 0, mCropRect.right, mCropRect.top, mBackgroundPaint);
		//右边
		canvas.drawRect(mCropRect.right, 0, getWidth(), getHeight(), mBackgroundPaint);
		//下边
		canvas.drawRect(mCropRect.left, mCropRect.bottom, mCropRect.right, getHeight(), mBackgroundPaint);

		//绘制边界线
		//上
		canvas.drawLine(mCropRect.left, mCropRect.top, mCropRect.right, mCropRect.top, mCropPaint);
		//下
		canvas.drawLine(mCropRect.left, mCropRect.bottom, mCropRect.right, mCropRect.bottom, mCropPaint);
		//左
		canvas.drawLine(mCropRect.left, mCropRect.top, mCropRect.left, mCropRect.bottom, mCropPaint);
		//右
		canvas.drawLine(mCropRect.right, mCropRect.top, mCropRect.right, mCropRect.bottom, mCropPaint);

		if (mPressed) {
			switch (mMode) {
				case CROP_SCALE_TOP:
					canvas.drawLine(mCropRect.left, mCropRect.top, mCropRect.right, mCropRect.top, mCropPressPaint);
					break;
				case CROP_SCALE_BOTTOM:
					canvas.drawLine(mCropRect.left, mCropRect.bottom, mCropRect.right, mCropRect.bottom, mCropPressPaint);
					break;
				case CROP_SCALE_LEFT:
					canvas.drawLine(mCropRect.left, mCropRect.top, mCropRect.left, mCropRect.bottom, mCropPressPaint);
					break;
				case CROP_SCALE_RIGHT:
					canvas.drawLine(mCropRect.right, mCropRect.top, mCropRect.right, mCropRect.bottom, mCropPressPaint);
					break;
			}
		}

		//绘制边角
		//从左往右
		canvas.drawRect(mCropRect.left - mCropCornerWidth, mCropRect.top - mCropCornerWidth, mCropRect.left, mCropRect.top - mCropCornerWidth + mCropCornerHeight, mCropCornerPaint);
		canvas.drawRect(mCropRect.left - mCropCornerWidth, mCropRect.bottom - (mCropCornerHeight - mCropCornerWidth), mCropRect.left, mCropRect.bottom + mCropCornerWidth, mCropCornerPaint);

		canvas.drawRect(mCropRect.left - mCropCornerWidth, mCropRect.top - mCropCornerWidth, mCropRect.left + (mCropCornerHeight - mCropCornerWidth), mCropRect.top, mCropCornerPaint);
		canvas.drawRect(mCropRect.left - mCropCornerWidth, mCropRect.bottom, mCropRect.left + (mCropCornerHeight - mCropCornerWidth), mCropRect.bottom + mCropCornerWidth , mCropCornerPaint);

		canvas.drawRect(mCropRect.right - (mCropCornerHeight - mCropCornerWidth), mCropRect.top - mCropCornerWidth, mCropRect.right + mCropCornerWidth, mCropRect.top, mCropCornerPaint);
		canvas.drawRect(mCropRect.right - (mCropCornerHeight - mCropCornerWidth), mCropRect.bottom, mCropRect.right + mCropCornerWidth, mCropRect.bottom + mCropCornerWidth, mCropCornerPaint);

		canvas.drawRect(mCropRect.right, mCropRect.top - mCropCornerWidth, mCropRect.right + mCropCornerWidth, mCropRect.top + mCropCornerHeight - mCropCornerWidth, mCropCornerPaint);
		canvas.drawRect(mCropRect.right, mCropRect.bottom - (mCropCornerHeight - mCropCornerWidth), mCropRect.right + mCropCornerWidth, mCropRect.bottom + mCropCornerWidth, mCropCornerPaint);
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = (int) event.getX();
				mDownY = (int) event.getY();
				setAdjustMode(mDownX, mDownY);
				mPressed = true;
				return true;
			case MotionEvent.ACTION_MOVE:
				moveCrop((int) event.getX(), (int) event.getY());
				break;
			default:
				mPressed = false;
				mMode = MoveMode.CROP_NULL;
		}
		invalidate();
		return super.onTouchEvent(event);
	}

	private void moveCrop(int moveX, int moveY) {
		if (mMode == MoveMode.CROP_NULL) return;
		int biasX = moveX - mDownX;
		int biasY = moveY - mDownY;
		mDownX = moveX;
		mDownY = moveY;
		switch (mMode) {
			case CROP_MOVE:
				mCropRect.left += biasX;
				mCropRect.right += biasX;
				mCropRect.top += biasY;
				mCropRect.bottom += biasY;
				break;
			case CROP_SCALE_LEFT_TOP:
				mCropRect.left += biasX;
				mCropRect.top += biasY;
				break;
			case CROP_SCALE_LEFT_BOTTOM:
				mCropRect.left += biasX;
				mCropRect.bottom += biasY;
				break;
			case CROP_SCALE_RIGHT_TOP:
				mCropRect.right += biasX;
				mCropRect.top += biasY;
				break;
			case CROP_SCALE_RIGHT_BOTTOM:
				mCropRect.right += biasX;
				mCropRect.bottom += biasY;
				break;
			case CROP_SCALE_LEFT:
				mCropRect.left += biasX;
				break;
			case CROP_SCALE_RIGHT:
				mCropRect.right += biasX;
				break;
			case CROP_SCALE_BOTTOM:
				mCropRect.bottom += biasY;
				break;
			case CROP_SCALE_TOP:
				mCropRect.top += biasY;
				break;
		}
		fixCropRect();
	}

	private void fixCropRect() {
		int fixLeft = 0;
		int fixRight = 0;
		int fixTop = 0;
		int fixBottom = 0;
		int fixWidth = 0;
		int fixHeight = 0;
		if (mCropRect.left < mCropHorizontalPadding) {
			fixLeft = mCropHorizontalPadding - mCropRect.left ;
		}
		if (mCropRect.right > getWidth() - mCropHorizontalPadding) {
			fixRight = (getWidth() - mCropHorizontalPadding - mCropRect.right);
		}
		if (mCropRect.top < mCropVerticalPadding) {
			fixTop = mCropVerticalPadding - mCropRect.top;
		}
		if (mCropRect.bottom > getHeight() - mCropVerticalPadding) {
			fixBottom = (getHeight() - mCropVerticalPadding - mCropRect.bottom);
		}
		if (mCropRect.bottom - mCropRect.top <= 3 * mCropCornerClickArea) {
			fixHeight = 3 * mCropCornerClickArea - (mCropRect.bottom - mCropRect.top);
		}
		if (mCropRect.right - mCropRect.left <= 3 * mCropCornerClickArea) {
			fixWidth = 3 * mCropCornerClickArea - (mCropRect.right - mCropRect.left);
		}
		if (mMode == MoveMode.CROP_MOVE) {
			mCropRect.left += fixLeft + fixRight;
			mCropRect.right += fixLeft + fixRight;
			mCropRect.top += fixBottom + fixTop;
			mCropRect.bottom += fixBottom + fixTop;
		} else {
			mCropRect.left += fixLeft;
			mCropRect.right += fixRight;
			mCropRect.top += fixTop;
			mCropRect.bottom += fixBottom;
			switch (mMode) {
				case CROP_SCALE_LEFT:
				case CROP_SCALE_TOP:
				case CROP_SCALE_LEFT_TOP:
					mCropRect.left -= fixWidth;
					mCropRect.top -= fixHeight;
					break;
				case CROP_SCALE_LEFT_BOTTOM:
					mCropRect.left -= fixWidth;
					mCropRect.bottom += fixHeight;
					break;
				case CROP_SCALE_RIGHT:
				case CROP_SCALE_BOTTOM:
				case CROP_SCALE_RIGHT_TOP:
					mCropRect.right += fixWidth;
					mCropRect.top -= fixHeight;
					break;
				case CROP_SCALE_RIGHT_BOTTOM:
					mCropRect.right += fixWidth;
					mCropRect.bottom += fixHeight;
					break;
			}
		}
	}

	private void setAdjustMode(int downX, int downY) {
		if (clickCorner(mCropRect.left, mCropRect.top, downX, downY, mCropCornerClickArea)){
			mMode = MoveMode.CROP_SCALE_LEFT_TOP;
		} else if (clickCorner(mCropRect.left, mCropRect.bottom, downX, downY, mCropCornerClickArea)) {
			mMode = MoveMode.CROP_SCALE_LEFT_BOTTOM;
		} else if (clickCorner(mCropRect.right, mCropRect.top, downX, downY, mCropCornerClickArea)){
			mMode = MoveMode.CROP_SCALE_RIGHT_TOP;
		} else if (clickCorner(mCropRect.right, mCropRect.bottom, downX, downY, mCropCornerClickArea)) {
			mMode = MoveMode.CROP_SCALE_RIGHT_BOTTOM;
		} else if (clickLine(mCropRect.top, mCropRect.bottom, mCropRect.left, downY, downX, mCropCornerClickArea)) {
			mMode = MoveMode.CROP_SCALE_LEFT;
		} else if (clickLine(mCropRect.top, mCropRect.bottom, mCropRect.right, downY, downX, mCropCornerClickArea)) {
			mMode = MoveMode.CROP_SCALE_RIGHT;
		} else if (clickLine(mCropRect.left, mCropRect.right, mCropRect.top, downX, downY, mCropCornerClickArea)) {
			mMode = MoveMode.CROP_SCALE_TOP;
		} else if (clickLine(mCropRect.left, mCropRect.right, mCropRect.bottom, downX, downY, mCropCornerClickArea)) {
			mMode = MoveMode.CROP_SCALE_BOTTOM;
		} else if (mCropRect.contains(downX, downY)) {
			mMode = MoveMode.CROP_MOVE;
		} else {
			mMode = MoveMode.CROP_NULL;
		}
	}

	private boolean clickCorner(int posX, int posY, int downX, int downY, int areaRadius) {
		return Math.abs(posX - downX) <= areaRadius && Math.abs(posY - downY) <= areaRadius;
	}

	/**
	 * 点击的点是否在垂直或水平线的点击区域内
	 */
	private boolean clickLine(int posDifSmall, int posDifLarge, int posSame, int downDif, int downSame, int areaRadius) {
		if (downDif < posDifSmall || downDif > posDifLarge) return false;
		return Math.abs(downSame - posSame) <= areaRadius;
	}

	private enum MoveMode {
		CROP_MOVE, CROP_SCALE_LEFT_TOP, CROP_SCALE_LEFT_BOTTOM, CROP_SCALE_RIGHT_TOP, CROP_SCALE_RIGHT_BOTTOM, CROP_SCALE_RIGHT, CROP_SCALE_LEFT, CROP_SCALE_TOP, CROP_SCALE_BOTTOM, CROP_NULL
	}
}