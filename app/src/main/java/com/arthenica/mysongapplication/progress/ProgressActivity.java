package com.arthenica.mysongapplication.progress;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arthenica.mysongapplication.DeviceInfoUtil;
import com.arthenica.mysongapplication.R;

import rx.functions.Func1;

public class ProgressActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        initView();
    }

    private void initView() {
/*        ProgressSeekBar bar = findViewById(R.id.progress_bar);
        bar.setStyle(String::valueOf);*/
        FrameLayout root = findViewById(R.id.root);
        CaptionMoveView view = new CaptionMoveView(this);
        view.setText("SongZhuo");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DeviceInfoUtil.dpToPx(100));
        lp.gravity = Gravity.CENTER;
        root.addView(view, lp);
    }
}
