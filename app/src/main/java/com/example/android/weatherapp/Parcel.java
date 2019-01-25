package com.example.android.weatherapp;

import android.os.Parcelable;

public class Parcel implements Parcelable {
    private final int imageIndex;
    private final String cityName;
    private int temp;

    protected Parcel(android.os.Parcel in) {
        imageIndex = in.readInt();
        cityName = in.readString();
        temp = in.readInt();
    }

    public static final Creator<Parcel> CREATOR = new Creator<Parcel>() {
        @Override
        public Parcel createFromParcel(android.os.Parcel in) {
            return new Parcel(in);
        }

        @Override
        public Parcel[] newArray(int size) {
            return new Parcel[size];
        }
    };

    public int getImageIndex() {
        return imageIndex;
    }

    public String getCityName() {
        return cityName;
    }

    public Parcel(int imageIndex, String cityName) {
        this.imageIndex = imageIndex;
        this.cityName = cityName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(imageIndex);
        dest.writeString(cityName);
    }
}
