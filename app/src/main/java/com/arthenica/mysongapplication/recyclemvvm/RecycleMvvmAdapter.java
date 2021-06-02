package com.arthenica.mysongapplication.recyclemvvm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mysongapplication.R;

public class RecycleMvvmAdapter extends RecyclerView.Adapter<RecycleMvvmAdapter.BaseViewHolder> {

	@NonNull
	@Override
	public RecycleMvvmAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_recycleview_mvvm, parent, false).getRoot());
	}

	@Override
	public void onBindViewHolder(@NonNull RecycleMvvmAdapter.BaseViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return 0;
	}

	public static class BaseViewHolder extends RecyclerView.ViewHolder {

		public BaseViewHolder(@NonNull View itemView) {
			super(itemView);
		}
	}
}
