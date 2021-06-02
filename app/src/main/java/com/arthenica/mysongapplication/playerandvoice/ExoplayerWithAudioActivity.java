package com.arthenica.mysongapplication.playerandvoice;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.arthenica.mysongapplication.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.File;

public class ExoplayerWithAudioActivity extends AppCompatActivity {
	private static final String TAG = "ExoplayerWithAudioActiv";

	float volume = 0f;
	MultiTrackRenderersFactory multiTrackRenderersFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exoplayer_with_audio);

		initPlayer();

		findViewById(R.id.volume).setOnClickListener(v -> {
			multiTrackRenderersFactory.setVolume(volume);
			volume += 0.1f;
			Log.d(TAG, "onCreate: volume = " + volume);
		});
	}

	private void initPlayer() {

		//https://github.com/google/ExoPlayer/issues/6589
		//https://blog.csdn.net/xdestiny110/article/details/79347656#comments_13313302

		MultiTrackSelector selector = new MultiTrackSelector();
		multiTrackRenderersFactory = new MultiTrackRenderersFactory(2, this);

		MediaSource videoSource = new DefaultMediaSourceFactory(this).createMediaSource(MediaItem.fromUri(Uri.fromFile(new File("/sdcard/DCIM/Camera/个人交医保.mp4"))));

		SimpleExoPlayer player = new SimpleExoPlayer.Builder(this, multiTrackRenderersFactory)
				.setTrackSelector(selector).build();

//		player.setVolume(0f);

		MediaSource audioSource = new DefaultMediaSourceFactory(this).createMediaSource(MediaItem.fromUri(Uri.fromFile(new File("/sdcard/DCIM/Camera/song.mp4.wav"))));
		MediaSource mediaSource = new MergingMediaSource(videoSource, audioSource);

		player.setMediaSource(mediaSource);
		((PlayerView) (findViewById(R.id.player_view))).setPlayer(player);
		player.prepare();
	}


	@Override
	protected void onDestroy() {
		((PlayerView) (findViewById(R.id.player_view))).getPlayer().release();
		super.onDestroy();
	}
}