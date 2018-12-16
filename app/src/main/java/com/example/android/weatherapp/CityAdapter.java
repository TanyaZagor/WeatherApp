package com.example.android.weatherapp;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityAdapter extends BaseAdapter {
    private final Resources resources;
    private final String[] cityArr;

    public CityAdapter(Resources resources) {
        this.resources = resources;
        cityArr = this.resources.getStringArray(R.array.Cities);
    }

    @Override
    public int getCount() {
        return cityArr.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        TextView cityView = view.findViewById(R.id.city);
        cityView.setText(cityArr[position]);
        return view;
    }
}
