package com.example.android.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private WeatherData[] forecast;
    private ViewHolder viewHolder;

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView temperature;
        private TextView date;
        private TextView weatherInfo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            temperature = itemView.findViewById(R.id.temperature);
            date = itemView.findViewById(R.id.date);
            weatherInfo = itemView.findViewById(R.id.info);
        }
    }
    public WeatherAdapter( WeatherData[] forecast) {
        this.forecast = forecast;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder viewHolder, int i) {
        viewHolder.temperature.setText(String.valueOf((int) forecast[i].getTemperature()));
        viewHolder.date.setText(forecast[i].getDate());
        viewHolder.weatherInfo.setText(forecast[i].getWeatherInfo());

    }

    @Override
    public int getItemCount() {
        return forecast.length;
    }

}
