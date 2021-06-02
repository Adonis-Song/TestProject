package com.arthenica.mysongapplication;

import android.util.Log;

public class Bean {
	private static final String TAG = "Bean";
	@Override
	protected void finalize() throws Throwable {
		Log.d(TAG, "finalize: BeanBeanBeanBeanBeanBeanBean");
		super.finalize();
	}
}
