package com.arthenica.mysongapplication.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.arthenica.mysongapplication.R;

public class ExampleActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);

		findViewById(R.id.button).setOnClickListener(v -> {
			View view = findViewById(R.id.tv_long);
			if (view.getVisibility() == View.VISIBLE) {
				view.setVisibility(View.GONE);

			} else {
				view.setVisibility(View.VISIBLE);
			}
		});
	}
}