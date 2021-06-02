package com.arthenica.mysongapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Build;
import android.os.StatFs;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 设备相关的方法
 */
public class DeviceInfoUtil {

    private static String sHuaweiBuildNumber;
    private static String sDeviceBuildNumber;

    public static final boolean checkLocationSwitch(final Context context) {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getPhoneInfo() {
        return "Device Info: " + getPhoneBrand() + "-" + getPhoneModel() + "-" + getRomReleaseVersion()
                + "-"+ getRomSdkVersion();
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    public static int getRomSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getRomReleaseVersion() {
        return Build.VERSION.RELEASE;
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidthPx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightPx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    public static int dpToPx(int dp) {
        return (int)dpToPx((float) dp);
    }

    public static float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
    public static int pxToDp(int px) {
        return Math.round(px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String getRomReleaseVersionWithAndroid() {
        return "Android " + getRomReleaseVersion();
    }

    public static long usedMemory() {
        final Runtime s_runtime = Runtime.getRuntime();
        return (s_runtime.totalMemory() - s_runtime.freeMemory());
    }

    public static int getPssMemory(Context context) {
        final ActivityManager am =
                (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        android.os.Debug.MemoryInfo[] memoryInfoArray =
                am.getProcessMemoryInfo(new int[]{android.os.Process.myPid()});

        return memoryInfoArray[0].getTotalPss();
    }

    /**
     * Gets the total available memory (KB) on the system.
     *
     * @param context the context
     * @return the total available memory
     */
    public static long getTotalAvailableMemory(Context context) {
        try {
            final ActivityManager am =
                    (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(outInfo);
            return (long) outInfo.availMem / 1024;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Gets the total memory (KB) on the system.
     *
     * @param context the context
     * @return the total memory
     */
    public static long getTotalMemory(Context context) {
        try {
            final ActivityManager am =
                    (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(outInfo);
            if (getRomSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN) {
                return (long) outInfo.totalMem / 1024;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Gets the available internal memory (ROM) size (KB).
     *
     * @return the available internal memory size
     */
    public long getAvailableInternalMemorySize() {
        File path = android.os.Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * Gets the total internal memory (ROM) size (KB).
     *
     * @return the total internal memory size
     */
    public long getTotalInternalMemorySize() {
        File path = android.os.Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static String getDeviceBrand(){
        return Build.BRAND;
    }

    public static boolean isHuaweiDevice() {
        return "huawei".equalsIgnoreCase(getDeviceBrand());
    }

    public static String getDeviceBuildNumber() {
        if (sDeviceBuildNumber == null) {
            String buildNumber = "";
            try {
                Method method = Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class);
                if (method != null) {
                    Object obj = method.invoke(null, "ro.build.display.id", "");
                    buildNumber = (String) obj;
                }
            } catch (Exception e) {
            }
            sDeviceBuildNumber = buildNumber;
        }
        return sDeviceBuildNumber;
    }

    public static String getHuaweiBuildNumber() {
        if (sHuaweiBuildNumber == null && isHuaweiDevice()) {
            String buildNumber = "";
            try {
                Method method = Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class);
                if (method != null) {
                    Object obj = method.invoke(null, "ro.build.hw_real.version", "");
                    buildNumber = (String) obj;
                }
            } catch (Exception e) {
            }
            sHuaweiBuildNumber = buildNumber;
        }
        return sHuaweiBuildNumber;
    }

    public static boolean isMiui() {
        try {
            Class miui = Class.forName("miui.os.Build");
            if (miui != null)
                return true;
        } catch (Exception e) {
        }

        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class);
            if (method != null) {
                Object objectMiuiVersionName = method.invoke(null, "ro.miui.ui.version.name", "");
                String versionName = (String)objectMiuiVersionName;
                if (versionName != null && !versionName.equals("") )
                    return true;
                Object objectBuildId = method.invoke(null, "ro.build.id", "");
                String buildId = (String)objectBuildId;
                if (buildId != null && buildId.contains("MIUI"))
                    return true;
                Object objectDisplayBuildId = method.invoke(null, "ro.build.display.id", "");
                String displayBuildId = (String)objectDisplayBuildId;
                if (displayBuildId != null && displayBuildId.contains("MIUI"))
                    return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    public static int getNavigationBarHeight(Activity activity) {
        if (activity == null) return 0;
        String NAVIGATION = "navigationBarBackground";
        ViewGroup vp = (ViewGroup)activity.getWindow().getDecorView();
        for (int i = 0;i<vp.getChildCount();i++) {
            View childView = vp.getChildAt(i);
            if (childView.getId() != View.NO_ID && NAVIGATION.equals(activity.getResources().getResourceEntryName(childView.getId()))) {
                //底部状态栏是展示的，并返回其高度
                return childView.getHeight();
            }
        }
        return 0;
    }

    public static int getStatusBarHeightCompat(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        if (result <= 0) {
            result = dpToPx(25);
        }
        return result;
    }

}
