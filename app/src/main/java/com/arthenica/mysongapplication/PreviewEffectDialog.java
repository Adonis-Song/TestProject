package com.arthenica.mysongapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class PreviewEffectDialog  implements View.OnClickListener{
	private static final String TAG = "PreviewEffectDialog";

	private static final int UPDATE_CAPTION = 1;
	private final int PLAYER_VIEW_MARGIN_HORIZONTAL_DP = 36;
	private TextView mBtBecomeVip;
	private PlayerView mPlayerView;
	private SimpleExoPlayer mPlayer;
	private View mDialogView;
	private AlertDialog mDialog;
	private MessageHandler mHandler;
	private int mVideoHeightSrc;
	private int mVideoWidthSrc;
	private int mStartTime;
	private int mEndTime;
	private float mScaleRatio;//缩小比例
	private String mAssString;


	public PreviewEffectDialog(Context context, String mediaPath, int videoWidthSrc, int videoHeightSrc){
		init(context, mediaPath, videoWidthSrc, videoHeightSrc);
	}

	private void init(Context context, String mediaPath , int videoWidthSrc, int videoHeightSrc) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.dialog_effect_preview, null);
		mDialogView = view;
		mPlayerView = view.findViewById(R.id.dialog_preview_player);

		TextView btCancel = view.findViewById(R.id.bt_cancel);
//		btCancel.setOnClickListener(this);

		ImageView btImageCancel = view.findViewById(R.id.bt_image_cancel);
		btImageCancel.setOnClickListener(this);

		TextView vipText = view.findViewById(R.id.bt_become_vip);
		vipText.setOnClickListener(this);

		mVideoHeightSrc = videoHeightSrc;
		mVideoWidthSrc = videoWidthSrc;

		mHandler = new MessageHandler();

/*		// 创建带宽
		BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
		// 创建轨道选择工厂
		TrackSelection.Factory videoTrackSelectionFactory =
				new AdaptiveTrackSelection.Factory(bandwidthMeter);
		// 创建轨道选择实例
		TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
		// 创建播放器实例
		DefaultRenderersFactory renderersFactory =
				new DefaultRenderersFactory(context, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);
		mPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
		mPlayerView.setPlayer(mPlayer);
		mPlayerView.setUseController(false);
		DataSource.Factory dataSourceFactory
				= new DefaultDataSourceFactory(context, Util.getUserAgent(context, BuildConfig.APPLICATION_ID), null);
		MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(new File(mediaPath)));
		mPlayer.prepare(videoSource);*/
		mPlayer = new SimpleExoPlayer.Builder(context).build();
		MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(new File(mediaPath)));
		mPlayer.setMediaItem(mediaItem);
		mPlayer.prepare();

		final SeekBar seekBar = view.findViewById(R.id.seek_bar);
		seekBar.setVisibility(View.GONE);
//		seekBar.setProgress((int)mPlayer.getDuration());
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				Log.d(TAG, "onProgressChanged: progress " + progress);
				mPlayer.seekTo(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		mPlayer.addListener(new Player.EventListener() {
			@Override
			public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
				if (playWhenReady && playbackState == Player.STATE_READY) {
					if (seekBar.getMax() != mPlayer.getDuration()) {
						seekBar.setMax((int)mPlayer.getDuration());
						Log.d(TAG, "onPlayWhenReadyChanged: " + (int)mPlayer.getDuration());
						seekBar.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		mPlayerView.setPlayer(mPlayer);
		mPlayerView.setUseController(false);



/*		PlayerControlView playerControlView = view.findViewById(R.id.exo_controller_player);
		playerControlView.setPlayer(mPlayer);*/
/*		DefaultTimeBar defaultTimeBar = playerControlView.findViewById(R.id.exo_progress);
		defaultTimeBar.addListener(new TimeBar.OnScrubListener() {
			@Override
			public void onScrubStart(TimeBar timeBar, long position) {

			}

			@Override
			public void onScrubMove(TimeBar timeBar, long position) {
				Log.d(TAG, "onScrubMove: position = " + position);
				Log.d(TAG, "onScrubMove: current position " + mPlayer.getCurrentPosition());
				mPlayer.seekTo(position);
			}

			@Override
			public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {

			}
		});*/


	}

	public void setTimeAndAssString(int startTime, int endTime, String assString){
		if (startTime < 0 || endTime < 0 || startTime > endTime || assString == null) {
			startTime = 0;
			endTime = 0;
			assString = null;
		}
		mStartTime = startTime;
		mEndTime = endTime;
		mAssString = assString;
	}

	public void show(Context context) {
		mPlayer.setPlayWhenReady(true);
		mPlayer.seekTo(mStartTime);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(mDialogView);
		mDialog = builder.create();
		mDialog.setCancelable(false);
		mDialog.show();
		Window window = mDialog.getWindow();
		if (window != null) {//调整高度
			WindowManager.LayoutParams windowLp = window.getAttributes();
			DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			windowLp.width = (int) (displayMetrics.widthPixels * 0.8f);
			window.setAttributes(windowLp);

			ViewGroup.LayoutParams lp = mDialogView.findViewById(R.id.dialog_player_parent).getLayoutParams();
			lp.height = (int) ((windowLp.width - 2 * dpToPx(mVideoWidthSrc > mVideoHeightSrc ? 0 : PLAYER_VIEW_MARGIN_HORIZONTAL_DP)) * 1.0f * mVideoHeightSrc / mVideoWidthSrc);
			mScaleRatio = lp.height * 1.0f / mVideoHeightSrc;
			mDialogView.findViewById(R.id.dialog_player_parent).setLayoutParams(lp);
		}
		mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (mPlayer!= null) {
					mPlayer.release();
				}
				if (mHandler != null) {
					mHandler.removeCallbacksAndMessages(null);
				}
			}
		});
		mHandler.sendEmptyMessage(UPDATE_CAPTION);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.bt_image_cancel:
			case R.id.bt_cancel:
				if(mDialog != null) {
					mDialog.dismiss();
					mPlayer.release();
				}
				break;
			case R.id.bt_become_vip:
				mPlayer.setPlayWhenReady(false);
				mHandler.sendEmptyMessage(15);
				break;
		}
	}

	public static float dpToPx(float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
	}

	private class MessageHandler extends Handler {
		public MessageHandler (){
			super(Looper.getMainLooper());
		}

		@Override
		public void handleMessage(@NonNull Message msg) {
			super.handleMessage(msg);
/*			if (msg.what == UPDATE_CAPTION) {
				if (mPlayer.getCurrentPosition() >= mEndTime) {
					mPlayer.seekTo(mStartTime);
				}
				sendEmptyMessageDelayed(UPDATE_CAPTION, 10);
			}*/
			switch (msg.what) {
				case 15:
					mPlayer.seekTo(mPlayer.getCurrentPosition() + 100);
					mHandler.sendEmptyMessageDelayed(15, 100);
					Log.d(TAG, "handleMessage: time = " + mPlayer.getCurrentPosition() + 100);
			}
		}
	}

}
