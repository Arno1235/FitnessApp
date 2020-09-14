package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutHolder> {

    private ArrayList<HashMap<String, Object>> workoutsDB;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private CustomLinearLayoutManager customLinearLayoutManager;
    private View.OnTouchListener mOnSwipeListener;
    private int width;
    private boolean WorkOrExer;

    public WorkoutAdapter(boolean WorkOrExer, ArrayList<HashMap<String, Object>> workoutsDB, Context context, RecyclerView mRecyclerView, CustomLinearLayoutManager customLinearLayoutManager, int width) {
        this.workoutsDB = workoutsDB;
        this.mContext = context;
        this.mRecyclerView = mRecyclerView;
        this.customLinearLayoutManager = customLinearLayoutManager;
        this.width = width;
        this.WorkOrExer = WorkOrExer;
    }

    @Override
    public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_workout, parent, false);
        WorkoutHolder mWorkoutHolder = new WorkoutHolder(view);
        mOnSwipeListener = new RecyclerOnSwipeListener(WorkOrExer, mRecyclerView, mContext, mWorkoutHolder, customLinearLayoutManager, width);
        view.setOnTouchListener(mOnSwipeListener);
        return new WorkoutHolder(view);
    }

    @Override
    public int getItemCount() {
        return workoutsDB == null? 1: workoutsDB.size() + 1;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutHolder holder, final int position) {
        if (position < workoutsDB.size()) {
            final Map<String, Object> workout = workoutsDB.get(position);

            holder.setWorkoutName((String) workout.get("name"));
            if (workout.containsKey("imagePath")) {
                holder.setWorkoutImage((int) workout.get("imagePath"));
            }
            if (workout.containsKey("description")) {
                holder.setWorkoutDescription((String) workout.get("description"));
            } else {
                holder.setWorkoutDescription("");
            }
        } else {
            holder.setWorkoutName("Add New");
            holder.setWorkoutDescription("Click to add new");
            String picture = "pic";
            int random = (int) Math.round(Math.random() * 17);
            if (random < 10){
                picture += "0";
            }
            picture += Integer.toString(random);
            holder.setWorkoutImage(mContext.getResources().getIdentifier(picture, "drawable", mContext.getPackageName()));
        }

        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    public class WorkoutHolder extends RecyclerView.ViewHolder {

        private TextView textName, textDescription;
        private ImageView thumbnail;
        private CardView topLayer;

        public WorkoutHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textName);
            textDescription = itemView.findViewById(R.id.textDescription);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            topLayer = itemView.findViewById(R.id.topLayer);

        }

        public void moveCard(float x){
            if(mRecyclerView.getChildLayoutPosition(itemView) < workoutsDB.size()) {
                topLayer.setTranslationX(x);
            }
        }

        public void setWorkoutName(String name) {
            textName.setText(name);
        }
        public void setWorkoutDescription(String description){
            textDescription.setText(description);
        }
        public void setWorkoutImage(int id){
            thumbnail.setImageResource(id);
        }

    }
}