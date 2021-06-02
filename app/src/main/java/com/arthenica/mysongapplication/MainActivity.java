package com.arthenica.mysongapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.arthenica.mysongapplication.audio.audioedit.AudioEditActivity;
import com.arthenica.mysongapplication.chooseaudio.ChooseAudioActivity;
import com.arthenica.mysongapplication.consumeScrollview.ConsumerScrollActivity;
import com.arthenica.mysongapplication.dialogfragment.DialogFragmentActivity;
import com.arthenica.mysongapplication.example.ExampleActivity;
import com.arthenica.mysongapplication.exportservice.IExportVideoCallback;
import com.arthenica.mysongapplication.exportservice.IExportVideoOperation;
import com.arthenica.mysongapplication.ffmpegexport.FfmpegActivity;
import com.arthenica.mysongapplication.imagecut.ImageActivity;
import com.arthenica.mysongapplication.playerandvoice.ExoplayerWithAudioActivity;
import com.arthenica.mysongapplication.progress.ProgressActivity;
import com.arthenica.mysongapplication.recycledemo.LoadMoreWrapperActivity;
import com.arthenica.mysongapplication.recycledemo.LoadMoreWrapperAdapter;
import com.arthenica.mysongapplication.recyclemvvm.RecycleMvvmActivity;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Emitter;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_zhuo";


    private String mUrl = "content://media/external/video/media/1102396";
    private String mediaPath = "/sdcard/hai.mp4";
    private WeakReference<Bean> mWeak;
    private Bean mBean= new Bean();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
//        mWeak = new WeakReference<>(new Bean());
        mWeak = new WeakReference<>(mBean);

/*        int flags = getWindow().getDecorView().getSystemUiVisibility();
        flags = flags | View.SYSTEM_UI_FLAG_FULLSCREEN & ~View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(flags);*/
//        connection = new MyServiceConnection();
//        connection.setIExportVideoCallback(mIExportVideoCallback);
        final Button button = findViewById(R.id.button);
        Config.verifyStoragePermissions(this);

        View view = findViewById(R.id.jump_recycle);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainActivity.this, LoadMoreWrapperActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/*                Path path = new Path();
                path.moveTo(1,1);
                path.lineTo(2, 2);
                path.lineTo(1.5f, 1.5f);
                path.lineTo(1, 1);
                ObjectAnimator animator = ObjectAnimator.ofFloat(button, "scaleX", "scaleY", path);
                animator.setDuration(1000);
                animator.setRepeatCount(5);
                animator.start();*/

                int flags = getWindow().getDecorView().getSystemUiVisibility();
                flags = flags | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION & ~View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                getWindow().getDecorView().setSystemUiVisibility(flags);


                int videoWidth;
                int videoHeight;
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(MainActivity.this,
                        Uri.fromFile(new File(mediaPath)));
                Bitmap thumb = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                try {
                    String metaRotation = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
                    int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
                    int width = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    int height = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                    if (rotation == 90 || rotation == 270) {
                        videoWidth = height;
                        videoHeight = width;
                    } else {
                        videoWidth = width;
                        videoHeight = height;
                    }
                } catch (Exception e) {
                    videoWidth = thumb.getWidth();
                    videoHeight = thumb.getHeight();
                }
                mediaMetadataRetriever.release();
                PreviewEffectDialog dialog = new PreviewEffectDialog(MainActivity.this, mediaPath, videoWidth, videoHeight);
                dialog.setTimeAndAssString(5000, 8000, null);
                dialog.show(MainActivity.this);
/*                Intent intent = new Intent(AudioEditActivity.this, ImageActivity.class);
                startActivity(intent);*/
            }
        });

        Button button1 = findViewById(R.id.button_send_get);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                File file = new File("/storage/emulated/0/hanyu-export.mp4");
                if (file.exists()) {
                    boolean success = file.delete();
                    Log.d(TAG, "onClick: is success = " + success);
                }*/
/*                EditText text = findViewById(R.id.edit_text);
                text.setText("我 +" + "\u2060" + "你");*/

/*                int flags = getWindow().getDecorView().getSystemUiVisibility();
                flags = flags  & ~View.SYSTEM_UI_FLAG_FULLSCREEN & ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN &~View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                getWindow().getDecorView().setSystemUiVisibility(flags);*/
/*                Observable.create((Action1<Emitter<Long>>) emitter -> {
                    Log.d(TAG, "call: " + 12345 + "   thread id " + Thread.currentThread().getId());
                    emitter.onCompleted();
                }, Emitter.BackpressureMode.ERROR)
                        .subscribeOn(Schedulers.io())
//                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "call: " + 54321 + "   thread id " + Thread.currentThread().getId());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Long aLong) {

                           }
                        }); */


                startActivity(new Intent(MainActivity.this, DialogFragmentActivity.class));

            }
        });
        Log.d(TAG, "onCreate: Thread = " + Thread.currentThread().getId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCreate: Thread = " + Thread.currentThread().getId());
            }
        }).start();

        findViewById(R.id.button_play_auido).setOnClickListener(v -> {
            startActivity(new Intent(this, AudioEditActivity.class));
        });

        findViewById(R.id.bt_ffmpeg).setOnClickListener(v -> {
            startActivity(new Intent(this, FfmpegActivity.class));
        });
    }

    public void jumpMvvm (View view) {
        List<Byte[]> list=new ArrayList<>();
        for(int i=0;i<160;i++){
            list.add(new Byte[1024]);
        }
        System.gc();
        Log.d(TAG, "jumpMvvm: weak = " + (mWeak.get() == null));
//        mWeak = new WeakReference<>(new Bean());
        Intent intent = new Intent(MainActivity.this, RecycleMvvmActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: resultCode = " + resultCode + " requestCode = " + requestCode);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (connection != null) {
//            unbindService(connection);
        }
        super.onDestroy();
    }



    public void jumpToConsumer(View consumer) {
        Intent intent = new Intent(this, ConsumerScrollActivity.class);
        startActivity(intent);
    }



    //export service
    private static IExportVideoOperation exportOperation;
    private MyServiceConnection connection;

    public static class MyServiceConnection implements ServiceConnection {
        private IExportVideoCallback.Stub mIExportVideoCallback;

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            exportOperation = IExportVideoOperation.Stub.asInterface(iBinder);
            try {
                exportOperation.registerCallback(this.mIExportVideoCallback.asBinder());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            exportOperation = null;
        }

        public void setIExportVideoCallback(IExportVideoCallback.Stub iExportVideoCallback) {
            this.mIExportVideoCallback = iExportVideoCallback;
        }
    }

    private IExportVideoCallback.Stub mIExportVideoCallback = new IExportVideoCallback.Stub() {
        @Override
        public void setCurrentExportTime(int time) throws RemoteException {
            Log.d(TAG, "setCurrentExportTime: time = " + time);
//            Toast.makeText(AudioEditActivity.this, "time = " + time , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void setExportResult(boolean result) throws RemoteException {
            Log.d(TAG, "setExportResult: result = " + result);
        }
    };


}