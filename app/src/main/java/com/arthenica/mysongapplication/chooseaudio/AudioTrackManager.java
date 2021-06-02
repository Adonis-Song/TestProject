package com.arthenica.mysongapplication.chooseaudio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

public class AudioTrackManager {

	private static final String TAG = "AudioTrackManager";

	private final int MSG_PLAY = 1;
	private final int MSG_ERROR = 2;
	private final int MSG_STOP = 3;
	private final int MSG_TIME_CHECK = 4;
	private final int MSG_SEEK = 5;
	private final int MSG_SEEK_TIMEOUT = 6;
	private final int MSG_RESET = 7;
	private MediaPlayer mMediaPlayer;
	private final Handler mHandler;
	private final HandlerThread mThread;
	private AudioPlayerListener mAudioListener;
	private boolean mIsSeeking;
	private String mCurrentPath;

	interface AudioPlayerListener {
		default void playerStart(long totalTime) {}
		void playerEnd();
		void playerError();
		void timeChange(long currentTime);
	}

	public AudioTrackManager(){
		mThread = new HandlerThread("AUDIO_PLAY");
		mThread.start();
		mHandler = new Handler(mThread.getLooper()) {
			@Override
			public void handleMessage(@NonNull Message msg) {
				handlerPlayerMsg(msg);
			}
		};
	}

	private void handlerPlayerMsg(Message msg) {
		switch (msg.what) {
			case MSG_PLAY:
				playAudio((String) msg.obj);
				break;
			case MSG_STOP:
				if (mMediaPlayer != null) {
					mMediaPlayer.pause();
				}
				break;
			case MSG_TIME_CHECK:
				if (mMediaPlayer == null) return;
				if (mAudioListener != null) {
					mAudioListener.timeChange(mMediaPlayer.getCurrentPosition());
				}
				if (mMediaPlayer.isPlaying()) {
					mHandler.sendEmptyMessageDelayed(MSG_TIME_CHECK, 200);
				}
				break;
			case MSG_ERROR:
				if (mAudioListener != null) {
					mAudioListener.playerError();
				}
				break;
			case MSG_SEEK:
				if (mIsSeeking) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SEEK, msg.arg1, msg.arg2), 100);
				} else if (mMediaPlayer != null) {
					mMediaPlayer.seekTo(Math.min(mMediaPlayer.getDuration(), msg.arg1));
					mHandler.sendEmptyMessage(MSG_TIME_CHECK);
					mIsSeeking = true;
					mHandler.removeMessages(MSG_SEEK_TIMEOUT);
					mHandler.sendEmptyMessageDelayed(MSG_SEEK_TIMEOUT, 1000);
				}
				break;
			case MSG_SEEK_TIMEOUT:
				if (mIsSeeking) {
					mHandler.removeMessages(MSG_SEEK_TIMEOUT);
					mIsSeeking = false;
				}
				break;
			case MSG_RESET:
				try {
					mMediaPlayer.reset();
					mMediaPlayer.setDataSource((String) msg.obj);
					mMediaPlayer.prepare();
					mMediaPlayer.seekTo(msg.arg1);
					mCurrentPath = (String) msg.obj;
					mHandler.sendEmptyMessageDelayed(MSG_SEEK_TIMEOUT, 1000);
				} catch (IOException e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(MSG_ERROR);
				}
				break;
		}
	}

	private void playAudio(String path) {
		try {
			if (path.equals(mCurrentPath)) {
				Log.d(TAG, "playAudio: start");
				mMediaPlayer.start();
			} else {
				Log.d(TAG, "playAudio: reset");
				mMediaPlayer.reset();
				mMediaPlayer.setDataSource(path);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
				mCurrentPath = path;
			}
			if (mAudioListener != null) {
				mAudioListener.playerStart(mMediaPlayer.getDuration());
			}
			mHandler.sendEmptyMessage(MSG_TIME_CHECK);
		} catch (IOException e) {
			e.printStackTrace();
			mHandler.removeCallbacksAndMessages(null);
			mHandler.sendEmptyMessage(MSG_ERROR);
		}
	}
	private void initMediaPlay() {
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnCompletionListener(mp -> {
			mp.pause();
			mp.seekTo(0);
			if (mAudioListener != null) {
				mAudioListener.playerEnd();
				mAudioListener.timeChange(mp.getDuration());
			}
		});

		mMediaPlayer.setOnSeekCompleteListener(mp -> {
			mIsSeeking = false;
		});

		mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
			mHandler.sendEmptyMessage(MSG_ERROR);
			return true;
		});
	}

	public void stopPlay() {
		mHandler.removeMessages(MSG_STOP);
		mHandler.sendEmptyMessage(MSG_STOP);
	}

	public void startPlay(String path) {
		if (mMediaPlayer == null) {
			initMediaPlay();
		}
		mHandler.removeMessages(MSG_PLAY);
		mHandler.sendMessage(mHandler.obtainMessage(MSG_PLAY, path));
	}

	public void seekTo(long time) {
		mHandler.removeMessages(MSG_SEEK);
		if (mIsSeeking) {
			mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SEEK, (int) time, 0), 100);
		} else {
			mHandler.sendMessage(mHandler.obtainMessage(MSG_SEEK, (int) time, 0));
		}
	}

	public void resetPlay(String audioPath, long currentTime) {
		if (mMediaPlayer != null) {
			releaseMedia();
		}
		initMediaPlay();
		mIsSeeking = true;
		mHandler.removeCallbacksAndMessages(null);
		mHandler.sendMessage(mHandler.obtainMessage(MSG_RESET, (int) currentTime, 0, audioPath));
	}

	public void releaseMedia() {
		mHandler.removeCallbacksAndMessages(null);
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public void release() {
		releaseMedia();
		mThread.quit();
	}

	public void setPlayListener(AudioPlayerListener listener) {
		mAudioListener = listener;
	}
}
