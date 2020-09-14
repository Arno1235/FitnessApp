package com.ArnoVanEetvelde.fitnessapp;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerOnSwipeListener implements View.OnTouchListener {

    private RecyclerView mRecyclerView;
    private Context mContext;
    private WorkoutAdapter.WorkoutHolder mWorkoutHolder;
    private CustomLinearLayoutManager customLinearLayoutManager;
    private float startX, startY;
    private boolean first = true, click = true, WorkOrExer;
    private int width, currentScreen = 0, maxMovement, ratioTreshold = 2, confirmTreshold, animationVelocity = 2, clickTreshold = 24;
    private String userID;
    private ArrayList<HashMap<String, Object>> workoutsDB;

    public RecyclerOnSwipeListener(boolean WorkOrExer, RecyclerView mRecyclerView, Context mContext, WorkoutAdapter.WorkoutHolder mWorkoutHolder, CustomLinearLayoutManager customLinearLayoutManager, int width, String userID, ArrayList<HashMap<String, Object>> workoutsDB){
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        this.mWorkoutHolder = mWorkoutHolder;
        this.customLinearLayoutManager = customLinearLayoutManager;
        this.maxMovement = (width/2) - 256;
        this.confirmTreshold = maxMovement/2;
        this.width = width;
        this.WorkOrExer = WorkOrExer;
        this.userID = userID;
        this.workoutsDB = workoutsDB;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            first = true;
            startX = motionEvent.getRawX();
            startY = motionEvent.getRawY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE){

            if (first && Math.abs(startX - motionEvent.getRawX()) > ratioTreshold * Math.abs(startY - motionEvent.getRawY())){
                customLinearLayoutManager.setScrollEnabled(false);
                first = false;
            }
            if ((motionEvent.getRawX() - startX < 0 && motionEvent.getRawX() - startX + currentScreen*maxMovement > -maxMovement) ||
                motionEvent.getRawX() - startX > 0 && motionEvent.getRawX() - startX + currentScreen*maxMovement < maxMovement) {
                mWorkoutHolder.moveCard(motionEvent.getRawX() - startX + currentScreen*maxMovement);
            }
            if (Math.abs(startX - motionEvent.getRawX()) > clickTreshold){
                click = false;
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP){

            if (currentScreen == 0) {
                if (click){
                    int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                    Intent intent = new Intent(mContext, StartWorkoutActivity.class);
                    intent.putExtra("workoutObject", workoutsDB.get(itemPosition));
                    intent.putExtra("userID", userID);
                    mContext.startActivity(intent);
                } else if (motionEvent.getRawX() - startX > confirmTreshold) {
                    currentScreen = 1;
                    if (motionEvent.getRawX() - startX > maxMovement) {
                        cardAnimation(maxMovement, maxMovement);
                    } else {
                        cardAnimation(motionEvent.getRawX() - startX, maxMovement);
                    }
                } else if (startX - motionEvent.getRawX() > confirmTreshold) {
                    currentScreen = -1;
                    if (startX - motionEvent.getRawX() > maxMovement) {
                        cardAnimation(-maxMovement, -maxMovement);
                    } else {
                        cardAnimation(motionEvent.getRawX() - startX, -maxMovement);
                    }
                } else {
                    currentScreen = 0;
                    cardAnimation(motionEvent.getRawX() - startX, 0);
                }
            } else if (currentScreen == -1){
                if (startX > width/2 + 96 && click){
                    Toast.makeText(mContext, "remove " + mRecyclerView.getChildLayoutPosition(view), Toast.LENGTH_SHORT).show();
                } else if (motionEvent.getRawX() - startX > confirmTreshold + maxMovement) {
                    currentScreen = 1;
                    if (motionEvent.getRawX() - startX > maxMovement) {
                        cardAnimation(maxMovement, maxMovement);
                    } else {
                        cardAnimation(motionEvent.getRawX() - startX, maxMovement);
                    }
                } else if (motionEvent.getRawX() - startX > confirmTreshold) {
                    currentScreen = 0;
                    cardAnimation(motionEvent.getRawX() - startX - maxMovement, 0);
                } else {
                    currentScreen = -1;
                    if (startX - motionEvent.getRawX() > maxMovement) {
                        cardAnimation(-maxMovement, -maxMovement);
                    } else {
                        cardAnimation(motionEvent.getRawX() - startX - maxMovement, -maxMovement);
                    }
                }
            } else if (currentScreen == 1){
                if (startX < width/2 - 96 && click){
                    Toast.makeText(mContext, "edit " + mRecyclerView.getChildLayoutPosition(view), Toast.LENGTH_SHORT).show();
                    if (WorkOrExer) {
                        Intent intent = new Intent(mContext, EditWorkoutActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, EditExerciseActivity.class);
                        mContext.startActivity(intent);
                    }
                } else if (motionEvent.getRawX() - startX < -confirmTreshold - maxMovement){
                    currentScreen = -1;
                    if (startX - motionEvent.getRawX() > maxMovement) {
                        cardAnimation(-maxMovement, -maxMovement);
                    } else {
                        cardAnimation(motionEvent.getRawX() - startX, -maxMovement);
                    }
                } else if (motionEvent.getRawX() - startX < -confirmTreshold){
                    currentScreen = 0;
                    cardAnimation(motionEvent.getRawX() - startX + maxMovement, 0);
                } else {
                    currentScreen = 1;
                    if (motionEvent.getRawX() - startX > maxMovement) {
                        cardAnimation(maxMovement, maxMovement);
                    } else {
                        cardAnimation(motionEvent.getRawX() - startX + maxMovement, maxMovement);
                    }
                }
            }

            click = true;

        }

        return true;
    }

    public void cardAnimation(float start, float end){
        ValueAnimator anim = ValueAnimator.ofFloat(start, end);
        anim.setDuration((int) Math.abs((end - start) / animationVelocity));
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                mWorkoutHolder.moveCard(value);
            }
        });
        anim.addListener(new ValueAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                customLinearLayoutManager.setScrollEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                customLinearLayoutManager.setScrollEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}