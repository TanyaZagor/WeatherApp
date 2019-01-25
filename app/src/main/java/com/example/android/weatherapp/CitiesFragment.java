package com.example.android.weatherapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CitiesFragment extends Fragment {
    private String preferenceName = "preference";
    private String city;
    private SharedPreferences sharedPref;
    private DataBaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_list, container, false);

        db = new DataBaseHelper(getActivity());
        List<String> cities = db.getCities();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, cities);
        final AutoCompleteTextView textView = layout.findViewById(R.id.city);
        textView.setAdapter(adapter);
        Button button = layout.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = textView.getText().toString();
                savePreferences(sharedPref);
                showDetails();
            }
        });
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    private void showDetails() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}
