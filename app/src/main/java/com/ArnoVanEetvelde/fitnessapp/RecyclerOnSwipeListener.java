package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerOnSwipeListener implements View.OnTouchListener {

    private RecyclerView mRecyclerView;
    private Context mContext;

    public RecyclerOnSwipeListener(RecyclerView mRecyclerView, Context mContext){
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
