package com.arthenica.mysongapplication.exportservice;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.arthenica.mobileffmpeg.AsyncFFmpegExecuteTask;
import com.arthenica.mobileffmpeg.ExecuteCallback;

import com.arthenica.mysongapplication.exportservice.LogoValues;

public class ExportVideoService extends Service {

    private static final String TAG = "ExportVideoService_song";
    private IExportVideoCallback iExportVideoCallback;

    public ExportVideoService() {

    }

    private IExportVideoOperation.Stub mIExportVideoOperation = new IExportVideoOperation.Stub() {
        @Override
        public void exportVideoByAss(int targetVideoWidth, int targetVideoHeight, int targetVideoQuality, String srcVideoPath, String dstVideoPath, String assFilePath, LogoValues logoValues) throws RemoteException {
            exportVideoByAssInner(targetVideoWidth, targetVideoHeight, targetVideoQuality, srcVideoPath, dstVideoPath, assFilePath, logoValues);
            Log.d(TAG, "exportVideoByAss: ");
        }

        @Override
        public void cancel() throws RemoteException {
            FFmpeg.cancel();
        }


        @Override
        public void registerCallback(IBinder binder) throws RemoteException {
            iExportVideoCallback = IExportVideoCallback.Stub.asInterface(binder);
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIExportVideoOperation.asBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 根据ASS导出视频
     * @param targetVideoWidth 导出视频的宽
     * @param targetVideoHeight 导出视频的高
     * @param targetVideoQuality 导出视频的压缩率
     * @param srcVideoPath 原视频的路径
     * @param dstVideoPath 导出视频存放路径
     * @param assFilePath 导出视频所需要的ass文件的路径
     * @param logoValues 导出视频的logo
     */
    private void exportVideoByAssInner(int targetVideoWidth, int targetVideoHeight, int targetVideoQuality,
                                       String srcVideoPath, String dstVideoPath, String assFilePath,
                                       LogoValues logoValues) {
        String[] ffmpegCommand;
        if (logoValues == null) {
            ffmpegCommand = new String[]{"-hide_banner", "-i", srcVideoPath,
                    "-vcodec", "libx264",
                    "-pix_fmt", "yuv420p",
                    "-crf", ExportUtil.getCrf(targetVideoQuality),
                    "-preset", ExportUtil.getPreset(targetVideoQuality),
                    "-vf", "ass=" + assFilePath + ",scale=" + targetVideoWidth + ":" + targetVideoHeight + ",fps=30",
                    "-vprofile", "baseline",
                    "-c:a", "aac",
                    "-max_muxing_queue_size", "9999",
                    dstVideoPath};
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("[0:v]scale=" + targetVideoWidth + ":" + targetVideoHeight + ",ass=" + assFilePath + ",fps=30[bg];");
            sb.append("[1:v]scale=" + logoValues.logoScaleWidth + ":" + logoValues.logoScaleHeight + "[bg1];");
            sb.append("[bg][bg1]overlay=" + logoValues.logoMarginH + ":" + logoValues.logoMarginV + ":enable='between(t," + logoValues.logoStartTime + "," + logoValues.logoEndTime + ")'");

            ffmpegCommand = new String[]{"-hide_banner",
                    "-i", srcVideoPath,
                    "-itsoffset", String.valueOf(logoValues.logoStartTime),
                    "-i", logoValues.logoPath,
                    "-vcodec", "libx264",
                    "-filter_complex", sb.toString(),
                    "-pix_fmt", "yuv420p",
                    "-crf", ExportUtil.getCrf(targetVideoQuality),
                    "-preset", ExportUtil.getPreset(targetVideoQuality),
                    "-vprofile", "baseline",
                    "-c:a", "aac",
                    "-max_muxing_queue_size", "9999",
                    dstVideoPath};
        }

        final AsyncFFmpegExecuteTask asyncCommandTask = new AsyncFFmpegExecuteTask(ffmpegCommand, new ExecuteCallback() {

            @Override
            public void apply(long executionId, int result) {
                if (iExportVideoCallback != null) {
                    try {
                        iExportVideoCallback.setExportResult(result == 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        StringBuffer sb = new StringBuffer();
        for (String tmp : ffmpegCommand) {
            sb.append(tmp).append(" ");
        }
        Log.d("bpvg", "cmd = " + sb.toString());

        asyncCommandTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Config.resetStatistics();
        Config.enableStatisticsCallback(new StatisticsCallback() {
            public void apply(Statistics newStatistics) {
                if (iExportVideoCallback != null) {
                    try {
                        iExportVideoCallback.setCurrentExportTime(newStatistics.getTime());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "apply: iExportVideoCallback != null");
                }
/*                final int time = newStatistics.getTime();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Works works = DBManager.getInstance().queryWorksBySId(mOrderId);
                        if (works != null) {
                            int percentage = (int) Math.round(100.0 * time / 1000 / works.getDuration());
                            updateUploadProgress(percentage);
                        }
                    }
                });*/
            }
        });


    }


}
