package com.arthenica.mysongapplication.consumeScrollview;

public class TimeToWidth {
//	private static TimeToWidth sInstance;
	private int duration;
	private int allWidth;

	public TimeToWidth(){

	}

/*	public static TimeToWidth getInstance() {
		if (sInstance == null) {
			sInstance = new TimeToWidth();
		}
		return sInstance;
	}*/

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setAllWidth(int allWidth) {
		this.allWidth = allWidth;
	}

	public int getWidthByTime(int time) {
		return (int) Math.min(time * allWidth * 1f / duration, allWidth);
	}

	public int getTimeByWidth(int width) {
		return (int) Math.min(width * duration * 1f / allWidth, duration);
	}
}
