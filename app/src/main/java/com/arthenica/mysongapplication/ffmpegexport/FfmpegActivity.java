package com.arthenica.mysongapplication.ffmpegexport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.arthenica.mobileffmpeg.AsyncFFmpegExecuteTask;
import com.arthenica.mobileffmpeg.ExecuteCallback;

import com.arthenica.mysongapplication.R;

public class FfmpegActivity extends AppCompatActivity {
    private static final String TAG = "FfmpegActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg);
        findViewById(R.id.bt_start_ffmpeg).setOnClickListener(v -> startFFmpeg());
    }

    public void startFFmpeg() {
        String filter = "[0:a]aformat=sample_fmts=fltp:channel_layouts=stereo,volume=0.0[a0];"
                + "[1:a]aformat=sample_fmts=fltp:channel_layouts=stereo,volume=5.0,atrim=5.0:10.0,adelay=2000|2000[a1];"
                + "[a0][a1]amix=inputs=2:duration=first[a];" + "[0:v]scale=1080:1908";
        String[] ffmpegCommend = new String[] {
                "-hide_banner", "-y",
                "-i", "/sdcard/ffmpeg/song.mp4",
                "-i", "/sdcard/ffmpeg/song.mp4.wav",
                "-max_muxing_queue_size", "9999",
                "-vcodec", "libx264",
                "-pix_fmt","yuv420p",
                "-crf", "18",
                "-preset", "superfast",
                "-vprofile", "baseline",
                "-filter_complex", filter,
                "-map", "[a]",
                "-c:a", "aac",
                "/sdcard/ffmpeg/out.mp4"
        };
        final AsyncFFmpegExecuteTask asyncCommandTask = new AsyncFFmpegExecuteTask(ffmpegCommend, new ExecuteCallback() {

            @Override
            public void apply(long executionId, int result) {
                Log.d(TAG, "apply: executionId = " + executionId + " result = " + result);

            }
        });
        StringBuffer sb = new StringBuffer();
        for (String tmp : ffmpegCommend) {
            sb.append(tmp).append(" ");
        }
        Log.d("bpvg", "cmd = " + sb.toString());

        asyncCommandTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Config.resetStatistics();
        Config.enableStatisticsCallback(new StatisticsCallback() {
            public void apply(Statistics newStatistics) {
                Log.d(TAG, "apply: time = " + newStatistics.getTime());
                /*if (iExportVideoCallback != null) {
                    try {
                        iExportVideoCallback.setCurrentExportTime(newStatistics.getTime());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "apply: iExportVideoCallback != null");
                }*/
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