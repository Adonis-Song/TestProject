package com.arthenica.mysongapplication.recycledemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mysongapplication.R;
import com.arthenica.mysongapplication.databinding.AdapterRecyclerviewBinding;

import java.util.List;

/**
 * 上拉加载更多
 * Created by yangle on 2017/10/12.
 */
public class LoadMoreWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final List<DataViewModel.Data> dataList;

	public LoadMoreWrapperAdapter(List<DataViewModel.Data> dataList) {
		this.dataList = dataList;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		AdapterRecyclerviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_recyclerview, parent, false );
/*		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.adapter_recyclerview, parent, false);*/
		return new RecyclerViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		AdapterRecyclerviewBinding binding = DataBindingUtil.bind(holder.itemView);
		binding.setData(dataList.get(position));
		binding.executePendingBindings();
/*		RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
		recyclerViewHolder.tvItem.setText(.getName());*/
	}

	@Override
	public int getItemCount() {
		return dataList.size();
	}

	private static class RecyclerViewHolder extends RecyclerView.ViewHolder {

//		TextView tvItem;

		RecyclerViewHolder(View itemView) {
			super(itemView);
//			tvItem = itemView.findViewById(R.id.tv_item);
		}
	}
}
