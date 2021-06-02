package com.arthenica.mysongapplication.audio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.arthenica.mysongapplication.R;

public class AudioActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_activity);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, AudioFragment.newInstance())
					.commitNow();
		}
	}
}