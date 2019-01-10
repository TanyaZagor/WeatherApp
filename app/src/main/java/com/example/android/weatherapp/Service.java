package com.example.android.weatherapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class Service extends IntentService {

    private final String TAG = "MyService";
    private String[] weatherData = {"Дождь", "+1 +10", "700 мм", "3 м/с","90 %"};

    public Service() {
        super(Service.class.getSimpleName());
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Start");

        sendMyBroadCast();
    }

    private void sendMyBroadCast()
    {
        try
        {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(WeatherFragment.BROADCAST_ACTION);

            broadCastIntent.putExtra("WeatherData", weatherData);

            sendBroadcast(broadCastIntent);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
