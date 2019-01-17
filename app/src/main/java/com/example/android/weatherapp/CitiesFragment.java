package com.example.android.weatherapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.ListFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class CitiesFragment extends ListFragment {
    private String preferenceName = "preference";
    private String city;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CityAdapter adapter = new CityAdapter(getResources());

        setListAdapter(adapter);

        sharedPref = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
    }

    private void savePreferences(SharedPreferences sharedPref) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("City",city);
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CardView cityCardView = (CardView) v;
        TextView cityNameView = cityCardView.findViewById(R.id.city);
        city = cityNameView.getText().toString();
        savePreferences(sharedPref);
        showDetails();
    }

    private void showDetails() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}
