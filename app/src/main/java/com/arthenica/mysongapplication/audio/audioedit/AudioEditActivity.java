package com.arthenica.mysongapplication.audio.audioedit;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arthenica.mysongapplication.R;

public class AudioEditActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_edit);

        MainFragment.newInstance().navigateTo(this, R.id.fl_content);
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            return;
        }
        super.onBackPressed();
    }
}
