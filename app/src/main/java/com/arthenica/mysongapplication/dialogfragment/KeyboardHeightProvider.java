package com.arthenica.mysongapplication.dialogfragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;

import com.arthenica.mysongapplication.R;

public class KeyboardHeightProvider extends PopupWindow {
    /** The tag for logging purposes */
//    private final static String TAG = "sample_KeyboardHeightProvider";

    /** The keyboard height observer */
    private KeyboardHeightObserver observer;

    /** The cached landscape height of the keyboard */
    private int keyboardLandscapeHeight;

    /** The cached portrait height of the keyboard */
    private int keyboardPortraitHeight;

    /** The view that is used to calculate the keyboard height */
    private View popupView;

    /** The parent view */
    private View parentView;

    /** The root activity that uses this KeyboardHeightProvider */
    private Fragment mFragment;

    private int mBottom;

    private boolean mFirst;

    public static String TAG = "keyboardHeightProvider";

    /**
     * Construct a new KeyboardHeightProvider
     *
     */
    public KeyboardHeightProvider(Fragment fragment) {
        if (fragment == null || fragment.getActivity() == null || fragment.getView() == null) return;
        LayoutInflater inflater = (LayoutInflater) fragment.getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.popupView = inflater.inflate(R.layout.keyboard_popup_window, null, false);
        setContentView(popupView);

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        parentView = fragment.getView();
        mFragment = fragment;
        setWidth(0);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);

        popupView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (popupView != null) {
                    handleOnGlobalLayout();
                }
            }
        });
    }

    /**
     * Start the KeyboardHeightProvider, this must be called after the onResume of the Activity.
     * PopupWindows are not allowed to be registered before the onResume has finished
     * of the Activity.
     */
    public void start() {
        if (!isShowing() && parentView.getWindowToken() != null) {
            setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    /**
     * Close the keyboard height provider,
     * this provider will not be used anymore.
     */
    public void close() {
        this.observer = null;
        dismiss();
    }

    /**
     * Set the keyboard height observer to this provider. The
     * observer will be notified when the keyboard height has changed.
     * For example when the keyboard is opened or closed.
     *
     * @param observer The observer to be added to this provider.
     */
    public void setKeyboardHeightObserver(KeyboardHeightObserver observer) {
        this.observer = observer;
    }

    /**
     * Get the screen orientation
     *
     * @return the screen orientation
     */
    private int getScreenOrientation() {
        if (mFragment != null && mFragment.getActivity() != null) {
            return mFragment.getActivity().getResources().getConfiguration().orientation;
        } else {
            return Configuration.ORIENTATION_PORTRAIT;
        }
    }

    /**
     * Popup window itself is as big as the window of the Activity.
     * The keyboard can then be calculated by extracting the popup view bottom
     * from the activity window height.
     */
    private void handleOnGlobalLayout() {
        Rect rect = new Rect();
        popupView.getWindowVisibleDisplayFrame(rect);

        int orientation = getScreenOrientation();
        if (!mFirst) {
            mBottom = rect.bottom;
            mFirst = true;
        }

        int keyboardHeight = mBottom - rect.bottom;

        if (keyboardHeight == 0) {
            notifyKeyboardHeightChanged(0, orientation);
        }
        else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.keyboardPortraitHeight = keyboardHeight;
            notifyKeyboardHeightChanged(keyboardPortraitHeight, orientation);
        }
        else {
            this.keyboardLandscapeHeight = keyboardHeight;
            notifyKeyboardHeightChanged(keyboardLandscapeHeight, orientation);
        }
    }

    private void notifyKeyboardHeightChanged(int height, int orientation) {
        if (observer != null) {
            observer.onKeyboardHeightChanged(height, orientation);
        }
    }
}


