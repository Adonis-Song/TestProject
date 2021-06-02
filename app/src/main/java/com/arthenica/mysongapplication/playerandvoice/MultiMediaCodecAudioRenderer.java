package com.arthenica.mysongapplication.playerandvoice;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecAdapter;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.util.MediaClock;

public class MultiMediaCodecAudioRenderer extends MediaCodecAudioRenderer {
	public MultiMediaCodecAudioRenderer(Context context, MediaCodecSelector mediaCodecSelector) {
		super(context, mediaCodecSelector);
	}

	public MultiMediaCodecAudioRenderer(Context context, MediaCodecSelector mediaCodecSelector, @Nullable Handler eventHandler, @Nullable AudioRendererEventListener eventListener) {
		super(context, mediaCodecSelector, eventHandler, eventListener);
	}

	public MultiMediaCodecAudioRenderer(Context context, MediaCodecSelector mediaCodecSelector, @Nullable Handler eventHandler, @Nullable AudioRendererEventListener eventListener, @Nullable AudioCapabilities audioCapabilities, AudioProcessor... audioProcessors) {
		super(context, mediaCodecSelector, eventHandler, eventListener, audioCapabilities, audioProcessors);
	}

	public MultiMediaCodecAudioRenderer(Context context, MediaCodecSelector mediaCodecSelector, @Nullable Handler eventHandler, @Nullable AudioRendererEventListener eventListener, AudioSink audioSink) {
		super(context, mediaCodecSelector, eventHandler, eventListener, audioSink);
	}

	public MultiMediaCodecAudioRenderer(Context context, MediaCodecSelector mediaCodecSelector, boolean enableDecoderFallback, @Nullable Handler eventHandler, @Nullable AudioRendererEventListener eventListener, AudioSink audioSink) {
		super(context, mediaCodecSelector, enableDecoderFallback, eventHandler, eventListener, audioSink);
	}

	public MultiMediaCodecAudioRenderer(Context context, MediaCodecAdapter.Factory codecAdapterFactory, MediaCodecSelector mediaCodecSelector, boolean enableDecoderFallback, @Nullable Handler eventHandler, @Nullable AudioRendererEventListener eventListener, AudioSink audioSink) {
		super(context, codecAdapterFactory, mediaCodecSelector, enableDecoderFallback, eventHandler, eventListener, audioSink);
	}


	private int index;

	public MultiMediaCodecAudioRenderer(int index, Context context, MediaCodecSelector mediaCodecSelector,Handler eventHandler,AudioRendererEventListener eventListener,AudioSink audioSink) {
//        super(context,mediaCodecSelector,eventHandler,eventListener,audioSink);
		super(context,mediaCodecSelector);
		this.index = index;
	}

	@Override
	public MediaClock getMediaClock() {
		if (index == 0) {
			return super.getMediaClock();
		}
		return null;
	}
}
