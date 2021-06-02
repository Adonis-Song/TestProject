package com.arthenica.mysongapplication.chooseaudio;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.ViewModel;

import com.arthenica.mysongapplication.UiThreadExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import util.FileUtil;

public class ChooseAudioViewModel extends ViewModel implements AudioTrackManager.AudioPlayerListener {
	// TODO: Implement the ViewModel

	private static final String TAG = "zhuo";
	private ObservableArrayList<AudioItem> mAudioList = new ObservableArrayList<>();
	private AudioItem mChangeNameItem;
	private AudioTrackManager mManager;
	private AudioItem mPlayingAudio;


	public void getAudioFromCache(Context context) {
		Observable.create((Action1<Emitter<List<AudioItem>>>) emitter -> {
			File file = context.getExternalCacheDir();
			if (file != null && file.exists()) {
				file = new File(file.getAbsolutePath() + File.separatorChar + "audio");
				if (file.exists() && file.isDirectory()) {
					File[] audioFileArray = file.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));
					if (audioFileArray != null) {
						List<AudioItem> childList = new ArrayList<>();
						for (File audioFile : audioFileArray) {
							String name = audioFile.getName();
							childList.add(new AudioItem(audioFile.getAbsolutePath(), name.substring(0, name.indexOf('.'))));
							if (childList.size() >= 5) {
								emitter.onNext(childList);
								childList = new ArrayList<>();
							}
						}
						emitter.onNext(childList);
					}
				}
				emitter.onCompleted();
			}
		}, Emitter.BackpressureMode.ERROR)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<List<AudioItem>>() {

					@Override
					public void onStart() {
						mAudioList.clear();
					}

					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(List<AudioItem> list) {
						mAudioList.addAll(list);
					}
				});
	}

	public ObservableArrayList<AudioItem> getAudioList() {
		return mAudioList;
	}

	@Override
	protected void onCleared() {
		super.onCleared();
		if (mManager != null) {
			mManager.release();
		}
	}

	public void changeName(AudioItem audioItem) {
		mChangeNameItem = audioItem;
		if (mChangeNameItem.equals(mPlayingAudio)) {
			mManager.releaseMedia();
			mPlayingAudio.setPlaying(false);
			updateItem(mPlayingAudio);
		}
	}

	public void changeName(Editable text) {
		if (mChangeNameItem != null && text != null) {
			String newName = text.toString();
			if (!mChangeNameItem.getAudioName().equals(newName)) {
				Observable.create((Action1<Emitter<Boolean>>) emitter -> {
					String dirPath = mChangeNameItem.getAudioPath().substring(0, mChangeNameItem.getAudioPath().lastIndexOf(File.separatorChar));
					String fileExtensions = mChangeNameItem.getAudioPath().substring(mChangeNameItem.getAudioPath().lastIndexOf('.'));
					if (FileUtil.renameFile(dirPath, mChangeNameItem.getAudioName() + fileExtensions, newName + fileExtensions)) {
						mChangeNameItem.setAudioName(newName);
						mChangeNameItem.setAudioPath(dirPath + File.separatorChar + newName + fileExtensions);
						updateItem(mChangeNameItem);
						if (mChangeNameItem.equals(mPlayingAudio)) {
							mManager.resetPlay(mChangeNameItem.getAudioPath(), mChangeNameItem.getCurrentTime());
						}

					} else {

					}
				}, Emitter.BackpressureMode.ERROR)
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(aBoolean -> {

						});
			}
		}
	}

	private void updateItem(AudioItem audioItem) {
		UiThreadExecutor.runTask("1", () -> {
			int index = mAudioList.indexOf(audioItem);
			if (index >= 0 && index < mAudioList.size()) {
				Log.d(TAG, "updateItem: name = " + audioItem.getAudioName() + "  time = " + audioItem.getCurrentTime());
				mAudioList.set(index, audioItem);
			}
		}, 0);

	}

	public void playAudio(AudioItem audioItem) {
		if (audioItem == null && mPlayingAudio == null) return;
		if (mManager == null) {
			mManager = new AudioTrackManager();
			mManager.setPlayListener(this);
		}
		if (mPlayingAudio == null) {
			mManager.startPlay(audioItem.getAudioPath());
			mPlayingAudio = audioItem;
		} else if (audioItem == null || audioItem.equals(mPlayingAudio)) {
			mManager.startPlay(mPlayingAudio.getAudioPath());
		} else { //需要播放的与当前播放的不一致，把当前的关闭
			stopAudio(true);
			mPlayingAudio = audioItem;
			mManager.startPlay(audioItem.getAudioPath());
		}
		mPlayingAudio.setPlaying(true);
	}

	public void stopAudio(AudioItem audioItem) {
		if (mPlayingAudio == null) return;
		//关闭条件：只能关闭当前播放的，所以默认为null或者传入的item是符合当前播放的audio，就停止
		if (audioItem == null || audioItem.equals(mPlayingAudio)) {
			stopAudio(false);
		}
	}

	private void stopAudio(boolean resetCurrentTime) {
		mManager.stopPlay();
		if (resetCurrentTime) {
			Log.d(TAG, "stopAudio: name = " + mPlayingAudio.getAudioName());
			mPlayingAudio.setCurrentTime(-1);
		}
		mPlayingAudio.setPlaying(false);
		updateItem(mPlayingAudio);
	}

	public void seekTo(AudioItem audioItem, long time) {
		if (mPlayingAudio != null && mManager != null && mPlayingAudio.equals(audioItem)) {
			mManager.seekTo(time);
		}
	}

	@Override
	public void playerStart(long totalTime) {
		if (mPlayingAudio != null) {
			mPlayingAudio.setDuration(totalTime);
			mPlayingAudio.setPlaying(true);
			updateItem(mPlayingAudio);
		}
	}

	@Override
	public void playerEnd() {
		if (mPlayingAudio != null) {
			mPlayingAudio.setCurrentTime(mPlayingAudio.getDuration());
			mPlayingAudio.setPlaying(false);
			updateItem(mPlayingAudio);
		}
	}

	@Override
	public void playerError() {
		mPlayingAudio = null;

	}

	@Override
	public void timeChange(long currentTime) {
		if (mPlayingAudio != null) {
			mPlayingAudio.setCurrentTime(currentTime);
			updateItem(mPlayingAudio);
		}
	}

	public void deleteAudio(AudioItem audioItem) {
		if (audioItem.equals(mPlayingAudio)) {
			mManager.releaseMedia();
		}
		mAudioList.remove(audioItem);
	}
}