package com.example.android.weatherapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class WeatherFragment extends Fragment {

    public static final String PARCEL = "parcel";
    public String[] weather = {"Небольшой снег", "-1 -5", "700 мм", "3 м/с","80 %"};

    private RecyclerView recyclerView;
    private WeatherAdapter adapter;

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
    @TargetApi(Build.VERSION_CODES.M)
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
        weatherView.setText(weather[0]);
        temperatureView.setText(weather[1]);
        pressureView.setText(weather[2]);
        windSpeedView.setText(weather[3]);
        humidityView.setText(weather[4]);

        recyclerView = layout.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new WeatherAdapter();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return layout;
    }
}
