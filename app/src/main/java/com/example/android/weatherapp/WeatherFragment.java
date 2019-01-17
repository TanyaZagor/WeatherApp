package com.example.android.weatherapp;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.example.android.weatherapp.model.WeatherRequest;

import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.SENSOR_SERVICE;

public class WeatherFragment extends Fragment {

    private final String TAG = "WeatherFragment";
    public static final String PARCEL = "parcel";
    public static final String BROADCAST_ACTION = "com.example.android.weatherapp";
    MyBroadCastReceiver myBroadCastReceiver;

    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private SensorManager sensorManager;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;
    private TextView temperatureNow;
    private TextView humidityNow;
    private TextView weatherView;
    private TextView temperatureView;
    private TextView pressureView;
    private TextView windSpeedView;
    private TextView humidityView;
    private TextView cityNameView;
    private SharedPreferences sharedPref;
    private String preferenceName = "preference";
    private OpenWeather openWeather;

    private DataBaseHelper dataBase;

    private static final String API_KEY = "21dd341c1efc49455a1809a49af62a9d";

    public static WeatherFragment create(Parcel parcel) {
        WeatherFragment f = new WeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARCEL, parcel);
        f.setArguments(args);
        return f;
    }

    public Parcel getParcel() {
        Parcel parcel = getArguments().getParcelable(PARCEL);
        return parcel;
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_weather, container, false);
        Date date = new Date();

        dataBase = new DataBaseHelper(getActivity());
        //myBroadCastReceiver = new MyBroadCastReceiver();
        //registerMyReceiver();



        /*sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);

        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        sensorManager.registerListener(listenerTemp, sensorTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerHum, sensorHumidity, SensorManager.SENSOR_DELAY_NORMAL);

        temperatureNow = layout.findViewById(R.id.temperatureNow);
        humidityNow = layout.findViewById(R.id.humidityNow);*/

        cityNameView = layout.findViewById(R.id.city);
        TextView dateView = layout.findViewById(R.id.date);
        weatherView = layout.findViewById(R.id.weather_info);
        temperatureView = layout.findViewById(R.id.temperature);
        pressureView = layout.findViewById(R.id.pressure);
        windSpeedView = layout.findViewById(R.id.windSpeed);
        humidityView = layout.findViewById(R.id.humidity);

        sharedPref = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
        loadPreferences(sharedPref);

        //getActivity().startService(new Intent(getActivity(), Service.class));
        //myBroadCastReceiver = new MyBroadCastReceiver();

        //cityNameView.setText("Москва");
        dateView.setText(DateFormat.getDateInstance().format(date));
        weatherView.setText("");
        temperatureView.setText("");
        pressureView.setText("");
        windSpeedView.setText("");
        humidityView.setText("");

        initRetrofit();
        requestRetrofit(sharedPref.getString("City", "Moscow,ru"), API_KEY, "metric");

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

    private void loadPreferences(SharedPreferences sharedPref) {
        String value = sharedPref.getString("City", "Moscow,ru");
        cityNameView.setText(value);

    }

    /*private final SensorEventListener listenerTemp = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] temp = event.values;
            temperatureNow.setText(String.format("%,1f \u00B0", temp[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final SensorEventListener listenerHum = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] hum = event.values;
            humidityNow.setText(String.format("%,1f %", hum[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void registerMyReceiver() {

        try
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            getContext().registerReceiver(myBroadCastReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.toString());
        }

    }



    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String [] weather = intent.getStringArrayExtra("WeatherData");
            weatherView.setText(weather[0]);
            temperatureView.setText(weather[1]);
            pressureView.setText(weather[2]);
            windSpeedView.setText(weather[3]);
            humidityView.setText(weather[4]);
            Log.d(TAG, "onReceive");
        }
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
    }
}
