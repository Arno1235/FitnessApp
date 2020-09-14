package com.ArnoVanEetvelde.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ThrowOnExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;

public class StartWorkoutActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private ProgressDialog progressDialog;

    private CardView cardView;
    private TextView textName, textDescription, textNrRounds;
    private RecyclerView listExercises;
    private float widthScreen, heightScreen;
    private HashMap<String, Object> workoutObject;
    private ArrayList<HashMap<String, Object>> exercisesDB;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workoutObject = (HashMap<String, Object>) extras.getSerializable("workoutObject");
            userID = extras.getString("userID");
        }

        db = FirebaseFirestore.getInstance();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

        cardView = (CardView) findViewById(R.id.cardView);
        textName = (TextView) findViewById(R.id.textName);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textNrRounds = (TextView) findViewById(R.id.textNrRounds);

        listExercises = (RecyclerView) findViewById(R.id.listExercises);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        listExercises.setLayoutManager(layoutManager);
        listExercises.addItemDecoration(new VerticalSpaceItemDecoration(32));

        exercisesDB = new ArrayList<>();

        //TODO: getExercisesFromDB();
        getExercisesFromDB();

    }

    public void updateUI (){

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exercisesDB);
        listExercises.setAdapter(exerciseAdapter);

        cardView.getLayoutParams().height = (int) heightScreen*3/4;
        cardView.setTranslationY(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, getResources().getDisplayMetrics()));

        textName.setText(workoutObject.get("name").toString());
        textDescription.setText(workoutObject.get("description").toString());
        textNrRounds.setText(workoutObject.get("rounds").toString());

    }

    public void getExercisesFromDB (){

        progressDialog = new ProgressDialog(StartWorkoutActivity.this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        db.collection("User").document(userID).collection("Workouts").document(workoutObject.get("ID").toString()).collection("Exercise")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String, Object> exercise = (HashMap<String, Object>) document.getData();
                                exercise.put("ID", document.getId());
                                exercisesDB.add(exercise);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.hide();
                        updateUI();
                    }
                });

    }

    public void goToDoWorkout(View caller){
        Intent intent = new Intent(this, DoWorkoutActivity.class);
        startActivity(intent);
    }
}