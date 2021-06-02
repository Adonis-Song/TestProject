package com.arthenica.mysongapplication.dialogfragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.arthenica.mysongapplication.R;
import com.arthenica.mysongapplication.chooseaudio.ChooseAudioFragment;


public class MyDialogFragment extends Fragment implements KeyboardHeightObserver {

    private static final String TAG = "songzhuo";
    private KeyboardHeightProvider provider;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_fragment, container, false);
        rootView.findViewById(R.id.message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(TAG);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (fragment == null) {
                    fragment = new ChooseAudioFragment();
                    transaction.add(R.id.fragment_contain, fragment, TAG);
                }
                transaction.show(fragment);
                transaction.commitNow();

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    provider = new KeyboardHeightProvider(MyDialogFragment.this);
                    provider.setKeyboardHeightObserver(MyDialogFragment.this);
                    provider.start();
                }
            }
        }, 1000);
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
        TextView textView = getView().findViewById(R.id.message);
        ViewGroup.LayoutParams lp = textView.getLayoutParams();
        if (height != 0) {
            lp.height = height;
            textView.setLayoutParams(lp);
        }
        ((TextView) getView().findViewById(R.id.message)).setText(String.valueOf(height));
        Log.e(TAG, "onKeyboardHeightChanged: height = " + height );
    }
    
}