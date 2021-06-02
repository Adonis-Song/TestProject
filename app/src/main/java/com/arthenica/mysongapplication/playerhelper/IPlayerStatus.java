package com.arthenica.mysongapplication.playerhelper;

/**
 * des 播放状态,需要注入到播放控制器中,用于播放状态的回调
 * @author zs
 * @data 2020-06-23
 */
public interface IPlayerStatus {

	/**
	 * 缓冲更新
	 */
	void onBufferingUpdate(int percent);

	/**
	 * 播放结束
	 */
	void onComplete();

}