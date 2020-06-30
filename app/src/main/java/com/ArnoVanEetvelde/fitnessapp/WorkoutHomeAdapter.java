package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkoutHomeAdapter extends RecyclerView.Adapter<WorkoutHomeAdapter.WorkoutHolder> {

    private ArrayList<HashMap<String, Object>> workoutsDB;
    private Context mContext;

    // Counstructor for the Class
    public WorkoutHomeAdapter(ArrayList<HashMap<String, Object>> workoutsDB, Context context) {
        this.workoutsDB = workoutsDB;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.list_workout_home, parent, false);
        return new WorkoutHolder(view);
    }

    @Override
    public int getItemCount() {
        return workoutsDB == null? 0: workoutsDB.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull WorkoutHolder holder, final int position) {
        final HashMap<String, Object> workout = workoutsDB.get(position);

        // Set the data to the views here
        holder.setWorkoutName((String)workout.get("name"));

        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class WorkoutHolder extends RecyclerView.ViewHolder {

        private TextView textName;

        public WorkoutHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textName);
        }

        public void setWorkoutName(String name) {
            textName.setText(name);
        }
    }
}
