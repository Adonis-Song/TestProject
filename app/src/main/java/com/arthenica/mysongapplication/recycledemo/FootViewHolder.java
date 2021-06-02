package com.arthenica.mysongapplication.recycledemo;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mysongapplication.R;

public class FootViewHolder extends RecyclerView.ViewHolder {

	ProgressBar pbLoading;
	TextView tvLoading;
	LinearLayout llEnd;

	FootViewHolder(View itemView) {
		super(itemView);
		pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
		tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
		llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
	}
}