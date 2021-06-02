package com.arthenica.mysongapplication.consumeScrollview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.arthenica.mysongapplication.DeviceInfoUtil;
import com.arthenica.mysongapplication.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class ConsumerScrollActivity extends AppCompatActivity {

	private PlayerView mPlayer;
	private MediaPlayer mAudioPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumer_scroll);
/*		View view = findViewById(R.id.tv);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
		lp.width = (int) getResources().getDimension(R.dimen.DP1000);
		view.setLayoutParams(lp);*/

/*		initPlayer();
		initAudio();
		initSeekBar();

		initTextView();*/

		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		lp.width = (int) getResources().getDimension(R.dimen.DP60);
		lp.height = (int) getResources().getDimension(R.dimen.DP60);
		ConsumerViewGroup viewGroup = findViewById(R.id.consumer_view_group);
		viewGroup.addView(new ConsumerView(this), lp );
		viewGroup.setScrollCallback(new ConsumerViewGroup.IHorizontalScroll() {
			@Override
			public void scrollBy(int x) {
				findViewById(R.id.scroll).scrollBy(x,0);
			}

			@Override
			public int getCenterPosition() {
				return ((ConsumerMyScroller) findViewById(R.id.scroll)).getScrollStartPosition() + findViewById(R.id.scroll).getWidth() / 2;
			}

			@Override
			public void stateSelectedChange(boolean selected) {

			}
		});

//		initJson();

		findViewById(R.id.button_select).setOnClickListener(v -> viewGroup.setStateToSelected(true));

		findViewById(R.id.button_cancel).setOnClickListener(v -> viewGroup.cancelSelected());




	}

	private Drawable defineMyBG() {
		StateListDrawable sd = new StateListDrawable();
		//注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
		//所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
/*		sd.addState(new int[]{android.R.attr.state_selected}, getStateDrawable(State.SELECTED));
		sd.addState(new int[]{android.R.attr.state_focused}, getStateDrawable(State.FOCUSED));
		sd.addState(new int[]{android.R.attr.state_pressed}, getStateDrawable(State.PRESSED));
		//必选添加在最后一个状态
		sd.addState(new int[]{}, getStateDrawable(State.NORMAL));*/
		return sd;
	}

/*	private Drawable getStateDrawable(State state) {
		GradientDrawable shape = new GradientDrawable();
		//设置形状
		shape.setShape(GradientDrawable.RECTANGLE);//设置形状圆角矩形,椭圆;
		shape.setCornerRadius(20);//设置圆角大小
		switch (state) {
			case ENABLED://tip: 不能写成State.ENABLED 至于为什么我也不懂.
				shape.setColor(Color.RED);
				break;
			case FOCUSED:
				shape.setColor(Color.GREEN);
				break;
			case PRESSED:
				shape.setColor(Color.YELLOW);
				break;
			case NORMAL:
				shape.setColor(Color.BLUE);
				break;
		}
		//参数含义: 边线宽度;边线颜色;虚线长度dashWidth;虚线空格长度dashGap
		shape.setStroke(2, Color.BLACK, 5, 3);
		return shape;
	}*/

	private void initJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("version", 3);
			jsonObject.put("duration", 1000);
			TimeToWidth videoClipInfoEntity = new TimeToWidth();
			JSONObject videoClipInfoJson = new JSONObject(new Gson().toJson(videoClipInfoEntity));
			JSONObject videoChunksJson = new JSONObject();
			videoChunksJson.put("chunks", new JSONArray().put(0, videoClipInfoJson));
			JSONObject videoJson = new JSONObject();
			videoJson.put("tracks", new JSONArray().put(0, videoChunksJson));
			jsonObject.put("video", videoJson);
			jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initTextView() {

	}

	private void initSeekBar() {
	}



	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
}