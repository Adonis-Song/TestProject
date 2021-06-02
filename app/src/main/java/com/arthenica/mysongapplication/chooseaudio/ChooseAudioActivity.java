package com.arthenica.mysongapplication.chooseaudio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.arthenica.mysongapplication.R;

public class ChooseAudioActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_audio_activity);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, ChooseAudioFragment.newInstance())
					.commitNow();
		}
	}
}