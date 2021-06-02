package com.arthenica.mysongapplication.dialogfragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.arthenica.mysongapplication.R;

public class DialogFragmentActivity extends AppCompatActivity implements KeyboardHeightObserver  {

    private static final String TAG = "SongZhuo";
    KeyboardHeightProvider provider;
    MyDialogFragment dialogFragment = new MyDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             Log.d(TAG, "onClick: ");
                                                         }
                                                     
//            dialogFragment.show(getSupportFragmentManager(), "dialog");
    });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        provider.setKeyboardHeightObserver(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        provider.close();
    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        Log.e(TAG, "onKeyboardHeightChanged: height = " + height );
    }
}