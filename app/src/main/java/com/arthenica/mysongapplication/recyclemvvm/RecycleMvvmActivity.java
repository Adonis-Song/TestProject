package com.arthenica.mysongapplication.recyclemvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.arthenica.mysongapplication.R;

public class RecycleMvvmActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recycle_mvvm_activity);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, RecycleMvvmFragment.newInstance())
					.commitNow();
		}
	}
}