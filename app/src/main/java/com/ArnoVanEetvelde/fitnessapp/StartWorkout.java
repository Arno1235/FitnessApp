package com.ArnoVanEetvelde.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

public class StartWorkout extends AppCompatActivity {

    private CardView cardView;
    private TextView textName, textDescription;
    private float widthScreen, heightScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

        cardView = (CardView) findViewById(R.id.cardView);
        textName = (TextView) findViewById(R.id.textName);
        textDescription = (TextView) findViewById(R.id.textDescription);

        updateUI();

    }

    public void updateUI (){

        cardView.getLayoutParams().height = (int) heightScreen*3/4;
        cardView.setTranslationY(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, getResources().getDisplayMetrics()));

        textName.setText("Test Naam");
        textDescription.setText("Test Description");

    }
}