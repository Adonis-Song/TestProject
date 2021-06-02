package com.arthenica.mysongapplication.exportservice;

public class ExportUtil {

    public static final int VIDEO_QUALITY_HIGH = 30; // 超高画质
    public static final int VIDEO_QUALITY_STANDARD = 20; // 标准画质
    public static final int VIDEO_QUALITY_LOW = 10; // 低画质

    public static String getCrf(int targetVideoQuality) {
//        return "23";
//        return "18"; // 超高画质
        return (targetVideoQuality == VIDEO_QUALITY_HIGH) ? "18" : "23";
    }

    public static String getPreset(int targetVideoQuality) {
//        return "superfast";
//        return "ultrafast"; // 超高画质
        return (targetVideoQuality == VIDEO_QUALITY_HIGH) ? "ultrafast" : "superfast";
    }

}
