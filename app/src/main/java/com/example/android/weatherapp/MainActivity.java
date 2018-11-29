package com.example.android.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int temperature;
    private int humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoMsg("onCreate()");

        setTemperature(20);
        setHumidity(50);

        Parcel parcel = (Parcel) getIntent().getExtras().getSerializable(StartMainActivity.TEXT);
        TextView cityTextView = findViewById(R.id.city);
        TextView temperatureTextView = findViewById(R.id.temperatureTextView);
        TextView humidityTextView = findViewById(R.id.humidityTextView);

        cityTextView.setText(parcel.city);
        temperatureTextView.setText(String.valueOf(getTemperature()));
        humidityTextView.setText(String.valueOf(getHumidity()));
    }
    @Override
    protected void onStart() {
        super.onStart();
        infoMsg("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        infoMsg("onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        infoMsg("onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        infoMsg("onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        infoMsg("onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infoMsg("onDestroy()");
    }

    private void infoMsg(String method) {
        Log.i("Logs", method);
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
