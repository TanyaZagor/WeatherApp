package com.example.android.weatherapp;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.weatherapp.interfaces.OpenWeather;
import com.example.android.weatherapp.model.ForecastRequest;
import com.example.android.weatherapp.model.WeatherRequest;

import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class WeatherFragment extends Fragment {

    private final String TAG = "WeatherFragment";
    public static final String PARCEL = "parcel";
    public static final String BROADCAST_ACTION = "com.example.android.weatherapp";

    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private TextView weatherView;
    private TextView temperatureView;
    private TextView pressureView;
    private TextView windSpeedView;
    private TextView humidityView;
    private TextView cityNameView;
    private SharedPreferences sharedPref;
    private String preferenceName = "preference";
    private OpenWeather openWeather;
    private WeatherRequest[] weatherRequests;
    private WeatherData[] forecast = new WeatherData[15];

    private DataBaseHelper dataBase;

    private static final String API_KEY = "21dd341c1efc49455a1809a49af62a9d";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_weather, container, false);
        Date date = new Date();

        dataBase = new DataBaseHelper(getActivity());

        cityNameView = layout.findViewById(R.id.city);
        TextView dateView = layout.findViewById(R.id.date);
        weatherView = layout.findViewById(R.id.weather_info);
        temperatureView = layout.findViewById(R.id.temperature);
        pressureView = layout.findViewById(R.id.pressure);
        windSpeedView = layout.findViewById(R.id.windSpeed);
        humidityView = layout.findViewById(R.id.humidity);

        sharedPref = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
        loadPreferences(sharedPref);

        dateView.setText(DateFormat.getDateInstance().format(date));
        weatherView.setText("");
        temperatureView.setText("");
        pressureView.setText("");
        windSpeedView.setText("");
        humidityView.setText("");

        initRetrofit();
        requestRetrofit(sharedPref.getString("City", "Moscow"), API_KEY, "metric");

        recyclerView = layout.findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        /*adapter = new WeatherAdapter(getContext(), forecast);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
        return layout;
    }

    private void loadPreferences(SharedPreferences sharedPref) {
        String value = sharedPref.getString("City", "Moscow,ru");
        cityNameView.setText(value);

    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }
    private void requestRetrofit(String city, String keyApi, String unit) {
        openWeather.loadWeather(city, keyApi, unit)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            cityNameView.setText(response.body().getName());
                            weatherView.setText(response.body().getWeather()[0].getDescription());
                            temperatureView.setText(Float.toString(response.body().getMain().getTemp()));
                            pressureView.setText(Float.toString(response.body().getMain().getPressure()));
                            humidityView.setText(Float.toString(response.body().getMain().getHumidity()));
                            windSpeedView.setText(Float.toString(response.body().getWind().getSpeed()));

                            WeatherData weatherData = new WeatherData();
                            weatherData.setCity(response.body().getName());
                            weatherData.setWeatherInfo(response.body().getWeather()[0].getDescription());
                            weatherData.setTemperature(response.body().getMain().getTemp());
                            weatherData.setPressure(response.body().getMain().getPressure());
                            weatherData.setHumidity(response.body().getMain().getHumidity());
                            weatherData.setWindSpeed(response.body().getWind().getSpeed());

                            dataBase.updateData(weatherData);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequest> call,
                                          @NonNull Throwable throwable) {
                        Log.e("Retrofit", "request failed", throwable);
                    }
                });
        openWeather.loadForecast(city, keyApi, unit)
                .enqueue(new Callback<ForecastRequest>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(@NonNull Call<ForecastRequest> call,
                                           @NonNull Response<ForecastRequest> response) {

                        if (response.body() != null) {
                            for (int i = 0; i < 15; i++) {
                                WeatherData weatherData = new WeatherData();
                                weatherData.setTemperature(response.body().getList()[i].getMain().getTemp());
                                weatherData.setDate(response.body().getList()[i].getDt_txt());
                                weatherData.setWeatherInfo(response.body().getList()[i].getWeather()[0].getDescription());
                                forecast[i] = weatherData;

                            }
                            adapter = new WeatherAdapter(forecast);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            Log.i("Forecast", "!!!!" + forecast[0].getDate());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ForecastRequest> call,
                                          @NonNull Throwable throwable) {
                        Log.i("Forecast", "request failed", throwable);
                    }
                });
    }
}
