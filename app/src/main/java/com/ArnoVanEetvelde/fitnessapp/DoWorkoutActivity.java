package com.ArnoVanEetvelde.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class DoWorkoutActivity extends AppCompatActivity {

    private CardView cardCurrentExercise, cardNextExercise;
    private TextView textName, textDescription, textCurrentRound, textButNext,
            textCurrentExerciseName, textCurrentExerciseDescription, textCurrentExerciseTime,
            textNextExerciseName, textNextExerciseDescription, textNextExerciseTime;
    private float widthScreen, heightScreen, animationVelocity = 2f;

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
        textCurrentRound = (TextView) findViewById(R.id.textCurrentRound);
        textButNext = (TextView) findViewById(R.id.textButNext);
        textCurrentExerciseName = (TextView) findViewById(R.id.textCurrentExerciseName);
        textCurrentExerciseDescription = (TextView) findViewById(R.id.textCurrentExerciseDescription);
        textCurrentExerciseTime = (TextView) findViewById(R.id.textCurrentExerciseTime);
        textNextExerciseName = (TextView) findViewById(R.id.textNextExerciseName);
        textNextExerciseDescription = (TextView) findViewById(R.id.textNextExerciseDescription);
        textNextExerciseTime = (TextView) findViewById(R.id.textNextExerciseTime);

        updateUI();

    }

    public void updateUI (){

        textName.setText("Test Naam");
        textDescription.setText("Test Descriptie");
        textCurrentRound.setText("0 / 3");
        cardCurrentExercise.getLayoutParams().height = (int) (widthScreen - 2*dpToPx(64f));
        cardNextExercise.getLayoutParams().height = (int) (widthScreen - 2*dpToPx(64f));
        cardCurrentExercise.setTranslationX(0);
        cardNextExercise.setTranslationX(widthScreen);

    }

    public void butNext(View caller){
        nextExercise("test", "test", "00 : 15");
    }

    public void nextExercise(String Name, String Description, String Time){

        textNextExerciseName.setText(Name);
        textNextExerciseDescription.setText(Description);
        textNextExerciseTime.setText(Time);

        ValueAnimator anim = ValueAnimator.ofFloat(0, widthScreen);
        anim.setDuration((int) (widthScreen/animationVelocity));
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                cardCurrentExercise.setTranslationX(-value);
                cardNextExercise.setTranslationX(widthScreen-value);
            }
        });
        anim.addListener(new ValueAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                cardCurrentExercise.setTranslationX(0);
                cardNextExercise.setTranslationX(widthScreen);
                textCurrentExerciseName.setText(Name);
                textCurrentExerciseDescription.setText(Description);
                textCurrentExerciseTime.setText(Time);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                cardCurrentExercise.setTranslationX(0);
                cardNextExercise.setTranslationX(widthScreen);
                textCurrentExerciseName.setText(Name);
                textCurrentExerciseDescription.setText(Description);
                textCurrentExerciseTime.setText(Time);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    public float dpToPx(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}