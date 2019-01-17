package com.example.android.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db"; // название бд
    private static final int DATABASE_VERSION = 1;

    private static final String WEATHER_TABLE = "weather";
    private static final String ID = "id";
    private static final String CITY_NAME = "city_name";
    private static final String WEATHER_INFO = "weather_info";
    private static final String TEMPERATURE = "temperature";
    private static final String PRESSURE = "pressure";
    private static final String WIND_SPEED = "wind_speed";
    private static final String HUMIDITY = "humidity";

    public DataBaseHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + WEATHER_TABLE + "("
                + ID + " INTEGER PRIMARY KEY,"
                + CITY_NAME + " TEXT,"
                + WEATHER_INFO + "TEXT,"
                + TEMPERATURE + " REAL,"
                + PRESSURE + " REAL,"
                + WIND_SPEED + " REAL,"
                + HUMIDITY + " REAL"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void updateData(WeatherData weatherData) {

        boolean isCityExist;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query( WEATHER_TABLE,
                new String[] { },
                CITY_NAME + "=?",
                new String[] { String.valueOf(weatherData.getCity()) },
                null, null, null, null);

        if (!cursor.moveToFirst()) {
            isCityExist = false;
            cursor.close();
        } else {
            isCityExist = cursor.getCount() > 0;
            cursor.close();
        }

        if (isCityExist) {
            updateWeather(weatherData);
        } else {
            addCity(weatherData);
        }
    }

    private void updateWeather(WeatherData weatherData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CITY_NAME, weatherData.getCity());
        cv.put(WEATHER_INFO, weatherData.getWeatherInfo());
        cv.put(TEMPERATURE, weatherData.getTemperature());
        cv.put(WIND_SPEED, weatherData.getWindSpeed());
        cv.put(HUMIDITY, weatherData.getHumidity());
        cv.put(PRESSURE, weatherData.getPressure());
        db.insert(WEATHER_TABLE, null, cv);
        db.close();
    }

    private void addCity(WeatherData weatherData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CITY_NAME, weatherData.getCity());
        cv.put(WEATHER_INFO, weatherData.getWeatherInfo());
        cv.put(TEMPERATURE, weatherData.getTemperature());
        cv.put(WIND_SPEED, weatherData.getWindSpeed());
        cv.put(HUMIDITY, weatherData.getHumidity());
        cv.put(PRESSURE, weatherData.getPressure());
        db.update(WEATHER_TABLE, cv, CITY_NAME + "=?", new String[]{weatherData.getCity()});
        db.close();
    }
}
