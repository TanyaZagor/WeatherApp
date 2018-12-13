package com.example.android.weatherapp;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class WeatherFragment extends Fragment {
    public static final String PARCEL = "parcel";

    public static WeatherFragment create(Parcel parcel) {
        WeatherFragment f = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARCEL, parcel);
        f.setArguments(args);
        return f;
    }

    public Parcel getParcel() {
        Parcel parcel = (Parcel) getArguments().getSerializable(PARCEL);
        return parcel;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_weather, container, false);
        Date date = new Date();



        TextView cityNameView = layout.findViewById(R.id.city);
        TextView dateView = layout.findViewById(R.id.date);
        TextView weatherView = layout.findViewById(R.id.weather);
        TextView temperatureView = layout.findViewById(R.id.temperature);
        TextView pressureView = layout.findViewById(R.id.pressure);
        TextView windSpeedView = layout.findViewById(R.id.windSpeed);
        TextView humidityView = layout.findViewById(R.id.humidity);

        Parcel parcel = getParcel();

        cityNameView.setText(parcel.getCityName());
        dateView.setText(DateFormat.getDateInstance().format(date));
        weatherView.setText("Небольшой снег");
        temperatureView.setText("-1 -5");
        pressureView.setText("700 мм");
        windSpeedView.setText("3 м/с");
        humidityView.setText("80 %");
        return layout;
    }
}
