package com.arthenica.mysongapplication.chooseaudio;

public class AudioItem {
	private String audioName;
	private String audioPath;
	private boolean playing;
	private long currentTime;
	private long duration;

	public AudioItem(String audioPath, String audioName) {
		this.audioName = audioName;
		this.audioPath = audioPath;
		this.duration = -1;
		this.currentTime = -1;
		this.playing = false;
	}

	public String getAudioName() {
		return audioName;
	}

	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}

	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
}
