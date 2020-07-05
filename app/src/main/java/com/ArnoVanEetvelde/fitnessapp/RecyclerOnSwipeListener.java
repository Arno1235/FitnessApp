package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerOnSwipeListener implements View.OnTouchListener {

    private RecyclerView mRecyclerView;
    private Context mContext;
    private WorkoutAdapter.WorkoutHolder mWorkoutHolder;
    private float startX;

    public RecyclerOnSwipeListener(RecyclerView mRecyclerView, Context mContext, WorkoutAdapter.WorkoutHolder mWorkoutHolder){
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        this.mWorkoutHolder = mWorkoutHolder;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            startX = motionEvent.getRawX();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            mWorkoutHolder.moveCard(motionEvent.getRawX() - startX);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
            Toast.makeText(mContext, "UP", Toast.LENGTH_SHORT).show();
        }

        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        //Toast.makeText(mContext, Integer.toString(itemPosition) + " - " + motionEvent.getRawY(), Toast.LENGTH_SHORT).show();

        return true;
    }
}
