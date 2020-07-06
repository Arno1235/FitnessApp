package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MainSwipeListener implements OnTouchListener {

    private int prevX, prevY, startX, swipeTreshold = 200, settingsTreshold = 100;
    private final double velocityTreshold = 0.5;
    private boolean allowSwipe = false, first = true, settings = false;

    public MainSwipeListener(Context ctx){
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = x;
            prevX = x;
            prevY = y;
            allowSwipe = false;
            first = true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE){
            if (allowSwipe){
                if (settings){
                    movingCloseSettings(x - prevX);
                } else {
                    if (startX > settingsTreshold) {
                        moving(x - prevX);
                    } else {
                        movingOpenSettings(x - prevX);
                    }
                }
            } else if (Math.abs(prevX - x) > velocityTreshold && Math.abs(prevX - x) > Math.abs(prevY - y) && first){
                allowSwipe = true;
            }
            first = false;
        } else if (event.getAction() == MotionEvent.ACTION_UP && allowSwipe){
            if (Math.abs(x - startX) > swipeTreshold){
                if (settings){
                    confirmCloseSettings(x - startX);
                    settings = false;
                } else {
                    if (startX > settingsTreshold) {
                        confirm(x - startX);
                    } else {
                        if (x - startX > swipeTreshold) {
                            confirmOpenSettings(x - startX);
                            settings = true;
                        }
                    }
                }
            } else {
                if (settings){
                    cancelCloseSettings(x - startX);
                } else {
                    if (startX > settingsTreshold) {
                        cancel(x - startX);
                    } else {
                        cancelOpenSettings(x - startX);
                    }
                }
            }
        }
        return true;
    }

    public void setSettings (boolean settings){
        this.settings = settings;
    }

    public void moving(int x){
    }
    public void movingOpenSettings(int x){
    }
    public void movingCloseSettings(int x){
    }
    public void cancel(int loc){
    }
    public void cancelOpenSettings(int loc){
    }
    public void cancelCloseSettings(int loc){
    }
    public void confirm(int loc){
    }
    public void confirmOpenSettings(int loc){
    }
    public void confirmCloseSettings(int loc){
    }

}