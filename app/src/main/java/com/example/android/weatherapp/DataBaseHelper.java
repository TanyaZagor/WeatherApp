package com.example.android.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db"; // название бд
    private static final int DATABASE_VERSION = 1;

    private static final String WEATHER_TABLE = "weather";
    private static final String ID = BaseColumns._ID;
    public static final String CITY_NAME = "city_name";
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
                + WEATHER_INFO + " TEXT,"
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

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query( WEATHER_TABLE,
                new String[] { },
                CITY_NAME + "=?",
                new String[] { String.valueOf(weatherData.getCity()) },
                null, null, null, null);


        if (cursor.getCount() > 0) {
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
        db.update(WEATHER_TABLE, cv, CITY_NAME + "=?", new String[]{weatherData.getCity()});
        db.close();
        Log.i("WeatherFragment", "update");
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
        db.insert(WEATHER_TABLE, null, cv);
        db.close();
        Log.i("WeatherFragment", "add");
    }

    public int getSize() {
        SQLiteDatabase db = this.getWritableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, WEATHER_TABLE);
        return numRows;
    }

    public List<String> getCities() {
        List<String> cities = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + CITY_NAME + " FROM "+ WEATHER_TABLE;
        Cursor cursor = db.query(WEATHER_TABLE, new String[]{CITY_NAME}, null,null,null, null, null, null);
        while (cursor.moveToNext()) {
            cities.add(cursor.getString(cursor.getColumnIndex(CITY_NAME)));
        }
        return cities;
    }
}
