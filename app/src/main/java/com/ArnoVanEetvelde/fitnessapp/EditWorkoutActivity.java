package com.ArnoVanEetvelde.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class EditWorkoutActivity extends AppCompatActivity {

    private TextView textName, textDescription, textNrRounds;
    private RecyclerView listExercise;
    private int heightScreen, widthScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

        textName = (TextView) findViewById(R.id.textName);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textNrRounds = (TextView) findViewById(R.id.textNrRounds);
        listExercise = (RecyclerView) findViewById(R.id.listExercise);

        updateUI();

    }

    public void updateUI(){

        textName.setText("test naam");
        textDescription.setText("test Descriptie");
        textNrRounds.setText("3");

        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listExercise.setLayoutManager(customLinearLayoutManager);
        listExercise.addItemDecoration(new VerticalSpaceItemDecoration(32));

        ArrayList exercicesDB = new ArrayList<>();
        HashMap<String, Object> test = new HashMap<>();
        test.put("name", "10 pushups");
        test.put("description", "test beschrijving");
        exercicesDB.add(test);
        exercicesDB.add(test);
        exercicesDB.add(test);
        exercicesDB.add(test);
        exercicesDB.add(test);
        exercicesDB.add(test);
        exercicesDB.add(test);
        exercicesDB.add(test);

        WorkoutAdapter listAdapterWorkout = new WorkoutAdapter(false, exercicesDB, this, listExercise, customLinearLayoutManager, (int) widthScreen);
        listExercise.setAdapter(listAdapterWorkout);

    }
}