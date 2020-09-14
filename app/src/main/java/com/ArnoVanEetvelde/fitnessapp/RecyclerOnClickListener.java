package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerOnClickListener implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private Context mContext;
    private ArrayList<HashMap<String, Object>> workoutsDB;
    private String userID;

    public RecyclerOnClickListener(RecyclerView mRecyclerView, Context mContext, ArrayList<HashMap<String, Object>> workoutsDB, String userID){
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
        this.workoutsDB = workoutsDB;
        this.userID = userID;
    }

    @Override
    public void onClick(View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        Intent intent = new Intent(mContext, StartWorkoutActivity.class);
        intent.putExtra("workoutObject", workoutsDB.get(itemPosition));
        intent.putExtra("userID", userID);
        mContext.startActivity(intent);
    }
}