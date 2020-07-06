package com.ArnoVanEetvelde.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StartWorkout extends AppCompatActivity {

    private CardView cardView;
    private TextView textName, textDescription;
    private RecyclerView listExercises;
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

        listExercises = (RecyclerView) findViewById(R.id.listExercises);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        listExercises.setLayoutManager(layoutManager);
        listExercises.addItemDecoration(new VerticalSpaceItemDecoration(32));

        ArrayList<String> test = new ArrayList<>();
        test.add("Naam1");
        test.add("Naam2");
        test.add("Naam1");
        test.add("Naam2");
        test.add("Naam1");
        test.add("Naam2");

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(test);
        listExercises.setAdapter(exerciseAdapter);

        updateUI();

    }

    public void updateUI (){

        cardView.getLayoutParams().height = (int) heightScreen*3/4;
        cardView.setTranslationY(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, getResources().getDisplayMetrics()));

        textName.setText("Test Naam");
        textDescription.setText("Test Description");

    }
}