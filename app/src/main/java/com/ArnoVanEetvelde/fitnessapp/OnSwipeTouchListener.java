package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class OnSwipeTouchListener implements OnTouchListener {

    private int prevX, prevY, startX, velocityTreshold = 5, swipeTreshold = 300;
    private boolean allowSwipe = false, first = true;

    public OnSwipeTouchListener (Context ctx){
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
                moving(x - prevX);
            } else if (Math.abs(prevX - x) > velocityTreshold && Math.abs(prevX - x) > Math.abs(prevY - y) && first){
                allowSwipe = true;
            }
            first = false;
        } else if (event.getAction() == MotionEvent.ACTION_UP && allowSwipe){
            if (Math.abs(x - startX) > swipeTreshold){
                confirm(x - startX);
            } else {
                cancel(x - startX);
            }
        }
        return true;
    }

    public void moving(int x){
    }
    public void cancel(int loc){
    }
    public void confirm(int loc){
    }
}