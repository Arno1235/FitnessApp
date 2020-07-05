package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerOnClickListener implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private Context mContext;

    public RecyclerOnClickListener(RecyclerView mRecyclerView, Context mContext){
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public void onClick(View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        Intent intent = new Intent(mContext, StartWorkout.class);
        intent.putExtra("workoutID", itemPosition);
        mContext.startActivity(intent);
    }
}