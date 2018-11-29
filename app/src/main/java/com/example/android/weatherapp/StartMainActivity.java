package com.example.android.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class StartMainActivity implements View.OnClickListener {
    public final static String TEXT = "Text";

    private final Activity sourceActivity;

    public StartMainActivity(Activity sourceActivity) {
        this.sourceActivity = sourceActivity;
    }
    @Override
    public void onClick(View v) {
        EditText txt = sourceActivity.findViewById(R.id.editText);

        Parcel parcel = new Parcel();
        parcel.city = txt.getText().toString();

        Intent intent = new Intent(sourceActivity, MainActivity.class);
        intent.putExtra(TEXT, parcel);

        sourceActivity.startActivity(intent);
    }
}
