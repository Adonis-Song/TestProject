package com.arthenica.mysongapplication.consumeScrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.arthenica.mysongapplication.DeviceInfoUtil;
import com.arthenica.mysongapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class WaveFormView extends View {

    /**
     * 波形View的高度, 单位dp
     */
    public static final int VISUALIZER_HEIGHT = 42;

    /**
     * 线条宽度, 单位dp
     */
    private static final float STROKE_WIDTH = 1.0f;

    /**
     * 每个线条使用1600个采样点的平均音量值
     */
    private static final int NUM_OF_BYTES_PER_BAR = 1600;

    private static final int SAMPLE_RATE = 16000;
    private static final int BYTE_RATE = 2;

    /**
     * 高亮bar的数量
     */
    private static final int HIGHLIGHT_BARS = 2;

    /**
     * 每个bar的时长, 单位毫秒
     */
    private static final int MILLISECOND_PER_BAR = NUM_OF_BYTES_PER_BAR * 1000 / (SAMPLE_RATE * BYTE_RATE);

    /**
     * 音频文件的bytes array
     */
    private byte[] bytes;

    /**
     * 普通颜色
     */
    private Paint paintNormal = new Paint();

    /**
     * 高亮颜色
     */
    private Paint paintHighlight = new Paint();

    // 单位: 毫秒
    private int duration;
    // 单位: 毫秒
    private int highlightPoint = -1;
    private int width;
    private int height;

    /**
     * 线条宽度，单位px
     */
    private float mStrokeWidth;

    private List<Float> mNormalizedVolumeData;

    public WaveFormView(Context context, byte[] data, int duration) {
        super(context);
        init(data, duration);
    }

    public WaveFormView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(byte[] data, int duration) {
        this.bytes = data;
        this.duration = duration;
        mStrokeWidth = DeviceInfoUtil.dpToPx(STROKE_WIDTH);
        paintNormal.setStrokeWidth(mStrokeWidth);
        paintNormal.setAntiAlias(false);
        paintNormal.setColor(ContextCompat.getColor(getContext(), R.color.colorWaveNormal));
        paintHighlight.setStrokeWidth(mStrokeWidth);
        paintHighlight.setAntiAlias(false);
        paintHighlight.setColor(ContextCompat.getColor(getContext(), R.color.colorWaveHighlight));
        mNormalizedVolumeData = new ArrayList<>();

    }

    public void setHighlightPoint(int point) {
        highlightPoint = point;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = (int) (mStrokeWidth / MILLISECOND_PER_BAR * this.duration);
        height = DeviceInfoUtil.dpToPx(VISUALIZER_HEIGHT);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSample(canvas);
    }

    private void drawSample(Canvas canvas) {
        volumeDataNormalization();
        float centerY = (float) this.height / 2;
        for (int i = 0; i < mNormalizedVolumeData.size(); i++) {
            float left_x = i * mStrokeWidth;
            float volume = mNormalizedVolumeData.get(i);
            volume = centerY * volume;
            volume = Math.max(2, volume);
            if (highlightPoint >= 0) {
                if (i * MILLISECOND_PER_BAR < (highlightPoint - MILLISECOND_PER_BAR * HIGHLIGHT_BARS / 2)
                        && (i + 1) * MILLISECOND_PER_BAR > (highlightPoint - MILLISECOND_PER_BAR * HIGHLIGHT_BARS / 2)) {
                    canvas.drawLine(left_x, 0, left_x, centerY + centerY, paintHighlight);
                } else if (i * MILLISECOND_PER_BAR >= (highlightPoint - MILLISECOND_PER_BAR * HIGHLIGHT_BARS / 2)
                        && (i + 1) * MILLISECOND_PER_BAR <= (highlightPoint + MILLISECOND_PER_BAR * HIGHLIGHT_BARS / 2)) {
                    canvas.drawLine(left_x, 0, left_x, centerY + centerY, paintHighlight);
                } else if (i * MILLISECOND_PER_BAR < (highlightPoint + MILLISECOND_PER_BAR * HIGHLIGHT_BARS / 2)
                        && (i + 1) * MILLISECOND_PER_BAR > (highlightPoint + MILLISECOND_PER_BAR * HIGHLIGHT_BARS / 2)) {
                    canvas.drawLine(left_x, 0, left_x, centerY + centerY, paintHighlight);
                } else {
                    canvas.drawLine(left_x, centerY - volume, left_x, centerY + volume, paintNormal);
                }
            } else {
                canvas.drawLine(left_x, centerY - volume, left_x, centerY + volume, paintNormal);
            }
        }
    }

    /**
     * 从波形计算原始的分贝值数据
     * @param buffer
     * @param begin
     * @param end
     * @return
     */
    private double calculateVolume(byte[] buffer, int begin, int end) {
        double sumVolume = 0.0;
        double avgVolume = 0.0;
        double volume = 0.0;
        for(int i = begin; i < Math.min(end, bytes.length); i+=2) {
            int v1 = buffer[i] & 0xFF;
            int v2 = buffer[i + 1] & 0xFF;
            int temp = v1 + (v2 << 8);// 小端
            if (temp >= 0x8000) {
                temp = 0xffff - temp;
            }
            sumVolume += Math.abs(temp);
        }
        avgVolume = sumVolume / buffer.length / 2;
        volume = Math.log10(1 + avgVolume) * 10;
//        Log.d("BPCP", "分贝值2: " + avgVolume + ", " + volume);
        return volume;
    }

    /**
     * 获取最大的分贝值
     *
     * @param byteBuffer
     * @param samplesCount
     * @return
     */
    private double getMaxValue(byte[] byteBuffer, int samplesCount) {
        List list = new ArrayList();
        for (int i = 0; i < samplesCount; i++) {
            double volume = calculateVolume(byteBuffer, i * NUM_OF_BYTES_PER_BAR, (i + 1) * NUM_OF_BYTES_PER_BAR);
            list.add(volume);
        }
        Collections.sort(list);
        return (double) list.get(samplesCount - 1);
    }

    /**
     * 获取50分位的数值
     *
     * @param byteBuffer
     * @param samplesCount
     * @return
     */
    private double get50PercentageValue(byte[] byteBuffer, int samplesCount) {
        List list = new ArrayList();
        for (int i = 0; i < samplesCount; i++) {
            double volume = calculateVolume(byteBuffer, i * NUM_OF_BYTES_PER_BAR, (i + 1) * NUM_OF_BYTES_PER_BAR);
            list.add(volume);
        }
        Collections.sort(list);
        return (double) list.get(samplesCount / 2);
    }

    /**
     * x between [0, 1]
     * after sigmoid func, return y between (0, 1)
     *
     * @param x
     * @return
     */
    private double sigmoid(double x) {
        double sig_x = (x - 0.5) * 10;
        return 1 / (1 + Math.exp(-(sig_x)));
    }

    /**
     * 获取归一化之后的音频数据
     * 数值分布为(0, 1)
     */
    private void volumeDataNormalization() {
        mNormalizedVolumeData.clear();
        int samplesCount = (int) (bytes.length / (float) NUM_OF_BYTES_PER_BAR);
        double maxValue = getMaxValue(this.bytes, samplesCount);
        double fiftyPercentageValue = get50PercentageValue(this.bytes, samplesCount);
        if (maxValue > fiftyPercentageValue * 5)
            maxValue = fiftyPercentageValue * 5;
        for (int i = 0; i < samplesCount; i++) {
            // calculate raw volume value
            double rawVolumeValue = calculateVolume(bytes, i * NUM_OF_BYTES_PER_BAR, (i + 1) * NUM_OF_BYTES_PER_BAR);
            // constraint to [0, maxValue]
            rawVolumeValue = Math.min(rawVolumeValue, maxValue);
            rawVolumeValue = Math.max(rawVolumeValue, 0);
            // normalization, map [0, maxValue] to [0, 1]
            if (maxValue != 0.0) {
                rawVolumeValue = rawVolumeValue / maxValue;
            } else {
                rawVolumeValue = 0.0;
            }

            // sigmoid activation
//            rawVolumeValue = sigmoid(rawVolumeValue);
            mNormalizedVolumeData.add(i, (float) rawVolumeValue);
        }
    }

    public float getHighlightWidth(){
        return DeviceInfoUtil.getDensity(getContext()) * HIGHLIGHT_BARS * STROKE_WIDTH;
    }
}
