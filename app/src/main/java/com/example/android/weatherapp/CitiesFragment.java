package com.example.android.weatherapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import static com.example.android.weatherapp.WeatherFragment.PARCEL;

public class CitiesFragment extends ListFragment {
    private boolean isExist;
    private Parcel currentParcel;

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

        View detailsFrame = getActivity().findViewById(R.id.weather_details);

        isExist = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {

            currentParcel = savedInstanceState.getParcelable("CurrentCity");
        }
        else {
            currentParcel = new Parcel(0, getResources().getTextArray(R.array.Cities)[0].toString());
        }

        if (isExist){
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(currentParcel);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("CurrentCity", currentParcel);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CardView cityCardView = (CardView) v;
        TextView cityNameView = cityCardView.findViewById(R.id.city);
        currentParcel =  new Parcel(position, cityNameView.getText().toString());
        showDetails(currentParcel);
    }

    private void showDetails(Parcel parcel) {
        if (isExist) {
            getListView().setItemChecked( parcel.getImageIndex(), true);
            WeatherFragment detail = (WeatherFragment)
                    getFragmentManager().findFragmentById(R.id.weather_details);
            if (detail == null || detail.getParcel().getImageIndex() != parcel.getImageIndex()) {
                detail = WeatherFragment.create(parcel);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.weather_details, detail);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }
        else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), MainActivity.class);
            intent.putExtra(PARCEL, parcel);
            startActivity(intent);
        }
    }
}
