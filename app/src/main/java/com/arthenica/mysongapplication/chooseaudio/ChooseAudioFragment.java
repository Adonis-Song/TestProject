package com.arthenica.mysongapplication.chooseaudio;

import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.arthenica.mysongapplication.R;


public class ChooseAudioFragment extends Fragment {
	private static final String TAG = "ChooseAud zhuo";

	private ChooseAudioViewModel mViewModel;
	private ChooseAudioAdapter mAdapter;
	private Dialog mChangeNameDialog;

	public static ChooseAudioFragment newInstance() {
		return new ChooseAudioFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.choose_audio_fragment, container, false);
		RecyclerView recyclerView = rootView.findViewById(R.id.recycle_view_audio);
		mViewModel = new ViewModelProvider(this).get(ChooseAudioViewModel.class);
		mViewModel.getAudioFromCache(getContext());
		initRecycleView(recyclerView);
		mViewModel.getAudioList().addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<AudioItem>>() {
			@Override
			public void onChanged(ObservableList<AudioItem> sender) {
				Log.d(TAG, "onChanged: ");
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onItemRangeChanged(ObservableList<AudioItem> sender, int positionStart, int itemCount) {
				Log.d(TAG, "onItemRangeChanged: ");
				if (itemCount == 1) {
					mAdapter.notifyOneItemChange(positionStart, recyclerView.findViewHolderForAdapterPosition(positionStart), sender.get(positionStart));
				} else {
					mAdapter.notifyItemRangeChanged(positionStart, itemCount);
				}
			}

			@Override
			public void onItemRangeInserted(ObservableList<AudioItem> sender, int positionStart, int itemCount) {
				Log.d(TAG, "onItemRangeInserted: ");
				mAdapter.notifyItemRangeInserted(positionStart, itemCount);
//				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onItemRangeMoved(ObservableList<AudioItem> sender, int fromPosition, int toPosition, int itemCount) {
				Log.d(TAG, "onItemRangeMoved: ");
				mAdapter.notifyItemMoved(fromPosition, toPosition);
//				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onItemRangeRemoved(ObservableList<AudioItem> sender, int positionStart, int itemCount) {
				Log.d(TAG, "onItemRangeRemoved: ");
				mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
//				mAdapter.notifyDataSetChanged();
			}
		});
		return rootView;
	}

	private void initRecycleView(RecyclerView recyclerView) {
		mAdapter = new ChooseAudioAdapter(mViewModel.getAudioList());
		recyclerView.setAdapter(mAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		mAdapter.setChangeNameCallBack(new ChooseAudioAdapter.ICallback() {
			@Override
			public void changeName(AudioItem audioItem) {
				mViewModel.changeName(audioItem);
				if (mChangeNameDialog == null) {
					initChangeNameDialog();
				}
				mChangeNameDialog.show();
			}

			@Override
			public void playAudio(AudioItem audioItem) {
				mViewModel.playAudio(audioItem);
			}

			@Override
			public void stopAudio(AudioItem audioItem) {
				mViewModel.stopAudio(audioItem);
			}

			@Override
			public void seekTo(AudioItem audioItem, long time) {
				mViewModel.seekTo(audioItem, time);
			}

			@Override
			public void delete(AudioItem audioItem) {
				mViewModel.deleteAudio(audioItem);
			}

			@Override
			public void chooseAudio(AudioItem audioItem) {
				if (audioItem != null && getActivity() != null) {
//					getActivity().setResult();
				}
			}
		});
	}

	private void initChangeNameDialog() {
		mChangeNameDialog = new Dialog(getContext());
		mChangeNameDialog.setContentView(R.layout.dialog_change_audio_name);
		mChangeNameDialog.setCanceledOnTouchOutside(false);
		mChangeNameDialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> mChangeNameDialog.dismiss());
		mChangeNameDialog.findViewById(R.id.iv_cancel).setOnClickListener(v -> mChangeNameDialog.dismiss());
		mChangeNameDialog.findViewById(R.id.tv_confirm).setOnClickListener(v -> {
			mChangeNameDialog.dismiss();
			mViewModel.changeName(((EditText) mChangeNameDialog.findViewById(R.id.et_audio_name)).getText());
		});
	}

}