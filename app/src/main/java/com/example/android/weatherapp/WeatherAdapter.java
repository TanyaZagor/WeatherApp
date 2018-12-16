package com.example.android.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    final Calendar calendar = Calendar.getInstance();

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView temperature;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            temperature = itemView.findViewById(R.id.temperature);
            date = itemView.findViewById(R.id.date);
        }
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder viewHolder, int i) {

        calendar.add(Calendar.DATE, +1);
        viewHolder.temperature.setText("0 -10");
        viewHolder.date.setText(DateFormat.getDateInstance().format(calendar.getTime()));

    }

    @Override
    public int getItemCount() {
        return 1;
    }

}
