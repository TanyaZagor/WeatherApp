package com.example.android.weatherapp;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.concurrent.Executor;

import static android.content.Context.MODE_PRIVATE;

public class CitiesFragment extends Fragment {
    private String preferenceName = "preference";
    private String city;
    private float lat = 0;
    private float lon = 0;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private SharedPreferences sharedPref;
    private DataBaseHelper db;
    private FusedLocationProviderClient fusedLocationClient;

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
                if (!textView.getText().toString().equals("")) {
                    city = textView.getText().toString();
                    savePreferences(sharedPref);
                    showDetails();
                }
            }
        });

        Button buttonCoord = layout.findViewById(R.id.buttonCoord);
        buttonCoord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getCoord();
            }
        });
        return layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCoord() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }
        else {
            requestLocationPermissions();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lat = (float) location.getLatitude();
                            lon = (float) location.getLongitude();
                            savePreferences(sharedPref, lon, lat);
                            showDetails();
                        }
                        else
                            Log.i("Location", "can't define location");
                    }
                });
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPref = getActivity().getSharedPreferences(preferenceName, MODE_PRIVATE);
    }

    private void savePreferences(SharedPreferences sharedPref) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("ByCoord", false);
        editor.putString("City",city);
        editor.apply();
    }
    private void savePreferences(SharedPreferences sharedPref, float lon, float lat) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("ByCoord", true);
        editor.putString("City","Coord");
        editor.putFloat("lon", lon);
        editor.putFloat("lat", lat);
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
