package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutHolder> {

    private ArrayList<HashMap<String, Object>> workoutsDB;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private CustomLinearLayoutManager customLinearLayoutManager;
    private View.OnTouchListener mOnSwipeListener;
    private int width;

    public WorkoutAdapter(ArrayList<HashMap<String, Object>> workoutsDB, Context context, RecyclerView mRecyclerView, CustomLinearLayoutManager customLinearLayoutManager, int width) {
        this.workoutsDB = workoutsDB;
        this.mContext = context;
        this.mRecyclerView = mRecyclerView;
        this.customLinearLayoutManager = customLinearLayoutManager;
        this.width = width;
    }

    @Override
    public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_workout, parent, false);
        WorkoutHolder mWorkoutHolder = new WorkoutHolder(view);
        mOnSwipeListener = new RecyclerOnSwipeListener(mRecyclerView, mContext, mWorkoutHolder, customLinearLayoutManager, width);
        view.setOnTouchListener(mOnSwipeListener);
        return new WorkoutHolder(view);
    }

    @Override
    public int getItemCount() {
        return workoutsDB == null? 0: workoutsDB.size();
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutHolder holder, final int position) {
        final HashMap<String, Object> workout = workoutsDB.get(position);

        holder.setWorkoutName((String)workout.get("name"));
        holder.setWorkoutImage((int)workout.get("imagePath"));

        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    public class WorkoutHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private ImageView thumbnail;
        private CardView topLayer;

        public WorkoutHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textName);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            topLayer = itemView.findViewById(R.id.topLayer);

        }

        public void moveCard(float x){
            topLayer.setTranslationX(x);
        }

        public void setWorkoutName(String name) {
            textName.setText(name);
        }
        public void setWorkoutImage(int id){
            thumbnail.setImageResource(id);
        }
    }
}