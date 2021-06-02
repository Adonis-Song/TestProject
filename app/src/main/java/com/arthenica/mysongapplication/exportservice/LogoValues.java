package com.arthenica.mysongapplication.exportservice;

import android.os.Parcel;
import android.os.Parcelable;

public class LogoValues implements Parcelable {
    int logoStartTime;
    int logoEndTime;
    int logoScaleWidth;
    int logoScaleHeight;
    int logoMarginH;
    int logoMarginV;
    String logoPath;

    public LogoValues() {
    }

    protected LogoValues(Parcel in) {
        logoStartTime = in.readInt();
        logoEndTime = in.readInt();
        logoScaleWidth = in.readInt();
        logoScaleHeight = in.readInt();
        logoMarginH = in.readInt();
        logoMarginV = in.readInt();
        logoPath = in.readString();
    }


    public static final Creator<LogoValues> CREATOR = new Creator<LogoValues>() {
        @Override
        public LogoValues createFromParcel(Parcel in) {
            return new LogoValues(in);
        }

        @Override
        public LogoValues[] newArray(int size) {
            return new LogoValues[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(logoStartTime);
        parcel.writeInt(logoEndTime);
        parcel.writeInt(logoScaleWidth);
        parcel.writeInt(logoScaleHeight);
        parcel.writeInt(logoMarginH);
        parcel.writeInt(logoMarginV);
        parcel.writeString(logoPath);
    }
}
