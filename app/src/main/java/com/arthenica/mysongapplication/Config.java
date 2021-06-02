package com.arthenica.mysongapplication;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {

    private static final String TAG = "Config";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };



    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSubtitleFromFile(String assPath){
        File file = new File(assPath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        try (InputStream in = new FileInputStream(file); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
            String line = bufferedReader.readLine();
            StringBuilder stringBuffer = new StringBuilder();
            do {
                stringBuffer.append(line);
                stringBuffer.append(" \n");
                line = bufferedReader.readLine();
            } while (line != null);
            //stringBuffer.append("\n");
            return String.valueOf(stringBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int dpToPx(int dp) {
        return (int)dpToPx((float) dp);
    }

    public static float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }


}
