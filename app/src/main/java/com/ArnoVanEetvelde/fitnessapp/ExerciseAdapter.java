package com.ArnoVanEetvelde.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {

    private ArrayList<String> exerciseNames;

    public ExerciseAdapter(ArrayList<String> exerciseNames) {
        this.exerciseNames = exerciseNames;
    }

    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_exercise, parent, false);
        return new ExerciseHolder(view);
    }

    @Override
    public int getItemCount() {
        return exerciseNames == null? 0: exerciseNames.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, final int position) {
        holder.setExerciseName(exerciseNames.get(position));
    }

    public class ExerciseHolder extends RecyclerView.ViewHolder {

        private TextView textExerciseName;

        public ExerciseHolder(View itemView) {
            super(itemView);

            textExerciseName = itemView.findViewById(R.id.textExerciseName);
        }

        public void setExerciseName(String name) {
            textExerciseName.setText(name);
        }
    }
}