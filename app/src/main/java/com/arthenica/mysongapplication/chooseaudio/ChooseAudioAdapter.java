package com.arthenica.mysongapplication.chooseaudio;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mysongapplication.R;
import com.arthenica.mysongapplication.UiThreadExecutor;
import com.arthenica.mysongapplication.databinding.ItemChooseAudioBinding;

import java.util.List;

public class ChooseAudioAdapter extends RecyclerView.Adapter<ChooseAudioAdapter.AudioHolder> {

	private static final String TAG = "ChooseAudi  zhuo";
	private final List<AudioItem> mDataList;
	private ICallback mCallback;

	interface ICallback {
		void changeName(AudioItem audioItem);
		void playAudio(AudioItem audioItem);
		void stopAudio(AudioItem audioItem);
		void seekTo(AudioItem audioItem, long time);
		void delete(AudioItem audioItem);
		void chooseAudio(AudioItem audioItem);
	}

	public ChooseAudioAdapter(List<AudioItem> data) {
		this.mDataList = data;
/*		mBufSize = AudioTrack.getMinBufferSize(WAV_SAMPLE_RATE,
				AudioFormat.CHANNEL_OUT_STEREO,
				AudioFormat.ENCODING_PCM_16BIT);*/
	}

	@NonNull
	@Override
	public ChooseAudioAdapter.AudioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new AudioHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_choose_audio, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull ChooseAudioAdapter.AudioHolder holder, int position) {
		ItemChooseAudioBinding mBinding = holder.getBinding();
		updateViewByData(mBinding, mDataList.get(position));
		AudioItem data = mDataList.get(position);
		mBinding.ivPlayState.setOnClickListener(v -> {
			if (!data.isPlaying()) {
				mCallback.playAudio(data);
			} else {
				mBinding.ivPlayState.setImageResource(R.drawable.icon_player_play);
				data.setPlaying(false);
				mCallback.stopAudio(data);
			}
		});
		mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					mCallback.stopAudio(null);
					mBinding.ivPlayState.setImageResource(R.drawable.icon_player_play);
					data.setPlaying(false);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mCallback.seekTo(data, seekBar.getProgress() * 1000);
			}
		});
		mBinding.ivDeleteAudio.setOnClickListener(v -> mCallback.delete(data));
		mBinding.ivEditAudioName.setOnClickListener(v -> mCallback.changeName(data));
		mBinding.tvUseAudio.setOnClickListener(v -> {mCallback.chooseAudio(data);});
	}

	private void updateViewByData(ItemChooseAudioBinding mBinding, AudioItem data) {
		mBinding.ivPlayState.setImageResource(data.isPlaying() ? R.drawable.icon_player_pause : R.drawable.icon_player_play);
		mBinding.tvAudioName.setText(data.getAudioName());
		mBinding.seekBar.setMax((int) Math.max(1, data.getDuration() / 1000));
		mBinding.seekBar.setProgress((int) Math.max(0, data.getCurrentTime() / 1000));
		mBinding.tvTime.setText(String.valueOf(data.getCurrentTime()));
		mBinding.layoutAudioControl.setVisibility(data.getCurrentTime() < 0 ? View.GONE : View.VISIBLE);
		Log.d(TAG, "updateViewByData: data current time = " + data.getCurrentTime() + "  name = " + data.getAudioName());
	}


	@Override
	public int getItemCount() {
		return mDataList.size();
	}


	public void notifyOneItemChange(int position, RecyclerView.ViewHolder viewHolderForAdapterPosition, AudioItem data) {
		if (viewHolderForAdapterPosition == null) {
			notifyItemChanged(position);
			return;
		}
		if (viewHolderForAdapterPosition instanceof AudioHolder) {
			Log.d(TAG, "notifyOneItemChange: ");
			ItemChooseAudioBinding binding = ((AudioHolder) (viewHolderForAdapterPosition)).mBinding;
			updateViewByData(binding, data);
		}
	}

	public void setChangeNameCallBack(ICallback changeName) {
		this.mCallback = changeName;
	}

	/*	private long playAudio(String path) throws IOException {

		File mAudioFile = new File(path);

		byte[] mAudioData = new byte[0];
		long mAudioDuration;
 		InputStream in = null;
		try {
			in = FileUtils.openInputStream(mAudioFile);
			in.skip(WAV_HEAD_LEN);
			int totalLength = (int) mAudioFile.length() - WAV_HEAD_LEN;
			long availableBytes = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory();
//                    LogUtil.d("BPCP", availableBytes +  " = available bytes, audio bytes = " + totalLength);
			if (availableBytes > totalLength * 1.25) {
//                    if (false) {
				mAudioData = new byte[totalLength];
				byte[] buffer = new byte[1024 * 1024];
				int bytesAmount = 0;
				int destPos = 0;
				while ((bytesAmount = in.read(buffer)) > 0) {
					// byte[] src, int srcPos, byte[] dst, int dstPos, int length
					System.arraycopy(buffer, 0, mAudioData, destPos, bytesAmount);
					destPos += bytesAmount;
				}
			}

			// set audio duration
			mAudioDuration = totalLength / (WAV_SAMPLE_RATE  * WAV_BYTE_PER_SAMPLE / 1000);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}


		if (mAudioTrack != null) {
			if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
				mAudioTrack.stop();
			}
			mAudioTrack.release();
		}

		int bytePerSecond = WAV_SAMPLE_RATE  * WAV_BYTE_PER_SAMPLE /1000; //32

		mAudioTrack = new AudioTrack(
				AudioManager.STREAM_MUSIC,
				(int) (WAV_SAMPLE_RATE ),
				AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				mAudioData.length,
				AudioTrack.MODE_STATIC);

		mAudioTrack.setNotificationMarkerPosition(mAudioData.length / WAV_BYTE_PER_SAMPLE);
		mAudioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
			@Override
			public void onMarkerReached(AudioTrack track) {
//				mView.captionPlayBtnChange(item,false);
			}

			@Override
			public void onPeriodicNotification(AudioTrack track) {

			}
		});
		mAudioTrack.write(mAudioData, 0, mAudioData.length);
		if (mAudioTrack.getState() != AudioTrack.STATE_INITIALIZED) {
//			mView.showToast(Application.appContext.getString(R.string.toast_audio_play_error));
//			mView.captionPlayBtnChange(item,false);
		} else {
			mAudioTrack.play();
		}
		return 0;
	}*/


	public static class AudioHolder extends RecyclerView.ViewHolder
	{
		final private ItemChooseAudioBinding mBinding;

		public AudioHolder(ItemChooseAudioBinding binding) {
			super(binding.getRoot());
			mBinding = binding;
		}

		public ItemChooseAudioBinding getBinding() {
			return mBinding;
		}
	}
}
