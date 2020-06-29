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

    private LinearLayout page0, pageL1, pageR1;
    private int currentScreen, numberOfScreens = 3;
    private float widthScreen, heightScreen, animationVelocity = 1f;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        page0 = (LinearLayout) findViewById(R.id.page0);
        pageL1 = (LinearLayout) findViewById(R.id.pageL1);
        pageR1 = (LinearLayout) findViewById(R.id.pageR1);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

        pageL1.setTranslationX(-widthScreen);
        pageR1.setTranslationX(widthScreen);

        currentScreen = 0;

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void moving(int x){
                if (currentScreen == -1){
                    page0.setTranslationX(x + widthScreen);
                    pageL1.setTranslationX(x);
                } else if (currentScreen == 0){
                    page0.setTranslationX(x);
                    pageL1.setTranslationX(x - widthScreen);
                    pageR1.setTranslationX(x + widthScreen);
                } else if (currentScreen == 1){
                    page0.setTranslationX(x - widthScreen);
                    pageR1.setTranslationX(x);
                }
            }
            public void cancel(int loc){
                ValueAnimator anim = ValueAnimator.ofFloat(loc, 0);
                loc = Math.abs(loc);
                anim.setDuration((int) (loc/animationVelocity));
                anim.start();

                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float value = (Float) animation.getAnimatedValue();
                        if (currentScreen == -1) {
                            page0.setTranslationX(value + widthScreen);
                            pageL1.setTranslationX(value);
                        } else if(currentScreen == 0) {
                            page0.setTranslationX(value);
                            pageL1.setTranslationX(value - widthScreen);
                            pageR1.setTranslationX(value + widthScreen);
                        } else if (currentScreen == 1){
                            page0.setTranslationX(value - widthScreen);
                            pageR1.setTranslationX(value);
                        }
                    }
                });
            }
            public void confirm(int loc){
                if (loc > 0){
                    movePrevious(loc);
                } else {
                    moveNext(-loc);
                }
            }
        });
    }

    public void movePrevious(int loc){
        if (currentScreen > -(numberOfScreens-1)/2) {
            currentScreen--;

            ValueAnimator anim = ValueAnimator.ofFloat(loc, widthScreen);
            anim.setDuration((int) (loc/animationVelocity));
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    if(currentScreen == 0) {
                        pageR1.setTranslationX(value);
                        page0.setTranslationX(value - widthScreen);
                    } else if (currentScreen == -1){
                        page0.setTranslationX(value);
                        pageL1.setTranslationX(value - widthScreen);
                    }
                }
            });
        }
    }

    public void moveNext(int loc){
        if (currentScreen < +(numberOfScreens-1)/2) {
            currentScreen ++;

            ValueAnimator anim = ValueAnimator.ofFloat(loc, widthScreen);
            anim.setDuration((int) (loc/animationVelocity));
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    if(currentScreen == 0) {
                        pageL1.setTranslationX(-value);
                        page0.setTranslationX(widthScreen - value);
                    } else if (currentScreen == 1){
                        page0.setTranslationX(-value);
                        pageR1.setTranslationX(widthScreen - value);
                    }
                }
            });
        }
    }
}