package com.ArnoVanEetvelde.fitnessapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class EditWorkoutActivity extends AppCompatActivity {

    private TextView textName, textDescription, textNrRounds;
    private View bgBlur;
    private CardView butSave, butRemove, cardPopup;
    private RecyclerView listExercise;
    private int heightScreen, widthScreen;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        bgBlur = (View) findViewById(R.id.bgBlur);
        butSave = (CardView) findViewById(R.id.butSave);
        butRemove = (CardView) findViewById(R.id.butRemove);
        cardPopup = (CardView) findViewById(R.id.cardPopup);

        updateUI();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updateUI(){

        textName.setText("test naam");
        textDescription.setText("test Descriptie");
        textNrRounds.setText("3");
        bgBlur.setElevation(24f);
        bgBlur.setAlpha(0);
        bgBlur.setClickable(true);
        bgBlur.setVisibility(View.GONE);
        cardPopup.setAlpha(0);
        cardPopup.setVisibility(View.GONE);
        butSave.setElevation(16f);
        butRemove.setElevation(16f);

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

    public void editProperties(View caller){

        cardPopup.setVisibility(View.VISIBLE);
        bgBlur.setVisibility(View.VISIBLE);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.setDuration(500);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                bgBlur.setAlpha(value);
                cardPopup.setAlpha(value);
            }
        });

    }

    public void saveProperties(View caller){

        ValueAnimator anim = ValueAnimator.ofFloat(1, 0);
        anim.setDuration(500);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                bgBlur.setAlpha(value);
                cardPopup.setAlpha(value);
            }
        });
        anim.addListener(new ValueAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                cardPopup.setVisibility(View.GONE);
                bgBlur.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                cardPopup.setVisibility(View.GONE);
                bgBlur.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    public void cancelProperties(View caller){

        ValueAnimator anim = ValueAnimator.ofFloat(1, 0);
        anim.setDuration(500);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                bgBlur.setAlpha(value);
                cardPopup.setAlpha(value);
            }
        });
        anim.addListener(new ValueAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                cardPopup.setVisibility(View.GONE);
                bgBlur.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                cardPopup.setVisibility(View.GONE);
                bgBlur.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }
}