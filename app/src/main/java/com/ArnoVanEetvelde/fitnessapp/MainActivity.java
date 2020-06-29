package com.ArnoVanEetvelde.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private LinearLayout page1, page2, page3;
    private int currentScreen, numberOfScreens = 3;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        page1 = (LinearLayout) findViewById(R.id.page1);
        page2 = (LinearLayout) findViewById(R.id.page2);
        page3 = (LinearLayout) findViewById(R.id.page3);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;

        page2.setTranslationX(-width);
        page3.setTranslationX(+width);

        currentScreen = 0;

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(getApplicationContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
                moveRight();
            }
            public void onSwipeLeft() {
                Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
                moveLeft();
            }
            public void onSwipeBottom() {
                Toast.makeText(getApplicationContext(), "bottom", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeExtremeRight(){
                Toast.makeText(getApplicationContext(), "extreme right", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void moveRight(){

        if (currentScreen > -(numberOfScreens-1)/2) {

            currentScreen--;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float height = displayMetrics.heightPixels;
            float width = displayMetrics.widthPixels;

            ValueAnimator anim = ValueAnimator.ofFloat(0f, width);
            anim.setDuration(1000);
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    if(currentScreen == 0) {
                        page3.setTranslationX(value);
                        page1.setTranslationX(value - width);
                    } else if (currentScreen == -1){
                        page1.setTranslationX(value);
                        page2.setTranslationX(value - width);
                    }
                }
            });

        }
    }

    public void moveLeft(){

        if (currentScreen < +(numberOfScreens-1)/2) {

            currentScreen ++;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float height = displayMetrics.heightPixels;
            float width = displayMetrics.widthPixels;

            ValueAnimator anim = ValueAnimator.ofFloat(0f, width);
            anim.setDuration(1000);
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    if(currentScreen == 0) {
                        page2.setTranslationX(-value);
                        page1.setTranslationX(width - value);
                    } else if (currentScreen == 1){
                        page1.setTranslationX(-value);
                        page3.setTranslationX(width - value);
                    }
                }
            });

        }
    }
}