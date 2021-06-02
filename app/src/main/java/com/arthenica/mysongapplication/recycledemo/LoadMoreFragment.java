 package com.arthenica.mysongapplication.recycledemo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arthenica.mysongapplication.R;
import com.arthenica.mysongapplication.databinding.FragmentLoadMoreBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoadMoreFragment extends Fragment {

	private LoadMoreWrapper loadMoreWrapper;
	private FragmentLoadMoreBinding binding;
	private DataViewModel mViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_load_more, container, false);
		mViewModel = new ViewModelProvider(this).get(DataViewModel.class);
		mViewModel.addData(getData());
//		binding.setRvData(mViewModel.getData().getValue());
		init();
		return binding.getRoot();
	}

	private void init() {
//		toolbar = (Toolbar) findViewById(R.id.toolbar);

		// 使用Toolbar替换ActionBar
		/*		setSupportActionBar(toolbar);*/

		// 设置刷新控件颜色
		binding.swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));

		// 模拟获取数据
//		getData();
		LoadMoreWrapperAdapter loadMoreWrapperAdapter = new LoadMoreWrapperAdapter(mViewModel.getData().getValue());
		loadMoreWrapper = new LoadMoreWrapper(loadMoreWrapperAdapter);
		binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		binding.recyclerView.setAdapter(loadMoreWrapper);

		// 设置下拉刷新
		binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 刷新数据
				mViewModel.getData().getValue().clear();
				mViewModel.getData().getValue().addAll(getData());
				loadMoreWrapper.notifyDataSetChanged();

				// 延时1s关闭下拉刷新
				binding.swipeRefreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing()) {
							binding.swipeRefreshLayout.setRefreshing(false);
						}
					}
				}, 1000);
			}
		});

		// 设置加载更多监听
		binding.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
			@Override
			public void onLoadMore() {
				loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

				if (mViewModel.getData().getValue().size() < 100) {
					// 模拟获取网络数据，延时1s
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
//									getData();
									mViewModel.getData().getValue().addAll(getData());
									loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
								}
							});
						}
					}, 1000);
				} else {
					// 显示加载到底的提示
					loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
				}
			}
		});
	}



	private List<DataViewModel.Data> getData() {
		char letter = 'A';
		List<DataViewModel.Data> dataList = new ArrayList<>();
		for (int i = 0; i < 26; i++) {
			dataList.add(new DataViewModel.Data(String.valueOf(letter)));
			letter++;
		}
		return dataList;
	}


}
