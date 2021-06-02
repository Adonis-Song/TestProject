package com.arthenica.mysongapplication.playerandvoice;

import com.google.android.exoplayer2.audio.AudioProcessor;

import java.nio.ByteBuffer;

public class VolumeAudioProcess implements AudioProcessor {
	@Override
	public AudioFormat configure(AudioFormat inputAudioFormat) throws UnhandledAudioFormatException {
		return null;
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public void queueInput(ByteBuffer buffer) {

	}

	@Override
	public void queueEndOfStream() {

	}

	@Override
	public ByteBuffer getOutput() {
		return null;
	}

	@Override
	public boolean isEnded() {
		return false;
	}

	@Override
	public void flush() {

	}

	@Override
	public void reset() {

	}

}
