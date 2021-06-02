package com.arthenica.mysongapplication.recycledemo;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface FootCallback {

	RecyclerView.ViewHolder newHolder(View view);

	void onBinderFooterHolder(RecyclerView.ViewHolder holder, int position);
}
