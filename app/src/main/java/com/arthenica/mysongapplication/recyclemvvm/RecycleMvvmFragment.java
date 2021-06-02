package com.arthenica.mysongapplication.recyclemvvm;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arthenica.mysongapplication.R;

import java.util.Arrays;

public class RecycleMvvmFragment extends Fragment {
	private static final String TAG = "RecycleMvvmFragment";

	private RecycleMvvmViewModel mViewModel;

	public static RecycleMvvmFragment newInstance() {
		return new RecycleMvvmFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		return DataBindingUtil.inflate(inflater, R.layout.recycle_mvvm_fragment, container, false).getRoot();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewModel = new ViewModelProvider(this).get(RecycleMvvmViewModel.class);
		mViewModel.initCaptionList(Arrays.asList(
				new RecycleMvvmBean("1"),
				new RecycleMvvmBean("2"),
				new RecycleMvvmBean("3"),
				new RecycleMvvmBean("4"),
				new RecycleMvvmBean("5")
		));
	}

	private void test (long value) {
		Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
//		mViewModel.getTest().removeObserver(this::test);
//		mViewModel.getTest().
		Log.d(TAG, "onDestroy: " + mViewModel.getTest().hasObservers());
		super.onDestroy();
	}
}