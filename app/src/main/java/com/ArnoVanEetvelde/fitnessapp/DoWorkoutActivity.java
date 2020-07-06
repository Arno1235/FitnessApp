package com.ArnoVanEetvelde.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class DoWorkoutActivity extends AppCompatActivity {

    private CardView cardCurrentExercise, cardNextExercise;
    private TextView textName, textDescription, textButNext;
    private float widthScreen, heightScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

        cardCurrentExercise = (CardView) findViewById(R.id.cardCurrentExercise);
        cardNextExercise = (CardView) findViewById(R.id.cardNextExercise);
        textName = (TextView) findViewById(R.id.textName);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textButNext = (TextView) findViewById(R.id.textButNext);

        updateUI();

    }

    public void updateUI (){

        textName.setText("Test Naam");
        textDescription.setText("Test Descriptie");
        cardCurrentExercise.getLayoutParams().height = cardCurrentExercise.getLayoutParams().width;
        cardNextExercise.getLayoutParams().height = cardCurrentExercise.getLayoutParams().width;
        cardNextExercise.setTranslationX(widthScreen);

    }

    public void nextExercise(String Name, String Description, int Time){

    }


}