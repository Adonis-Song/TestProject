package com.arthenica.mysongapplication;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class UploadFileService extends IntentService {

    public UploadFileService() {
        super("UpdateFileService");
    }


    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
        }
    }

}
