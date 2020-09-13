package com.ArnoVanEetvelde.fitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private ProgressDialog progressDialog;

    private LinearLayout page0, pageL1, pageR1, settingsPage, progressChartProgress;
    private int currentScreen, numberOfScreens = 3;
    private float widthScreen, heightScreen, animationVelocity = 2f;
    private ImageView imageView, butCancelSettings;
    private View selector;
    private HashMap<String, Object> userDB;
    private ArrayList<HashMap<String, Object>> workoutsDB;
    private TextView textUser, textAverage, textMonth, textWeek, textDay;
    private RecyclerView listWorkoutHome, listWorkout;
    private WorkoutHomeAdapter listAdapterHome;
    private WorkoutAdapter listAdapterWorkout;
    private CardView settingsPageCard, settingsBlur;
    private MainSwipeListener swipeListener;
    private ArrayList<Double> testData;
    private String userID;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userDB = (HashMap<String, Object>) extras.getSerializable("user");
        }

        db = FirebaseFirestore.getInstance();

        page0 = (LinearLayout) findViewById(R.id.page0);
        pageL1 = (LinearLayout) findViewById(R.id.pageL1);
        pageR1 = (LinearLayout) findViewById(R.id.pageR1);
        settingsPage = (LinearLayout) findViewById(R.id.settingsPage);
        settingsPageCard = (CardView) findViewById(R.id.settingsPageCard);
        settingsBlur = (CardView) findViewById(R.id.settingsBlur);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

        selector = findViewById(R.id.selector);
        textUser = (TextView) findViewById(R.id.textUser);
        butCancelSettings = (ImageView) findViewById(R.id.butCancelSettings);
        textAverage = (TextView) findViewById(R.id.textAverage);
        textMonth = (TextView) findViewById(R.id.textMonth);
        textWeek = (TextView) findViewById(R.id.textWeek);
        textDay = (TextView) findViewById(R.id.textDay);

        listWorkoutHome = (RecyclerView) findViewById(R.id.listWorkoutHome);
        listWorkout = (RecyclerView) findViewById(R.id.listWorkout);

        workoutsDB = new ArrayList<>();
        getWorkoutsFromDB();
        //TODO: getProgressFromDB();

        currentScreen = 0;

        imageView = (ImageView) findViewById(R.id.swipeDetection);
        imageView.getLayoutParams().width = (int) heightScreen;
        swipeListener = new MainSwipeListener(MainActivity.this) {
            public void moving(int x){
                if (currentScreen == -1){
                    page0.setTranslationX(x + widthScreen);
                    pageL1.setTranslationX(x);
                } else if (currentScreen == 0){
                    page0.setTranslationX(x);
                    pageL1.setTranslationX(x - widthScreen);
                    pageR1.setTranslationX(x + widthScreen);
                } else if (currentScreen == 1){
                    page0.setTranslationX(x - widthScreen);
                    pageR1.setTranslationX(x);
                }
            }
            public void cancel(int loc){
                moveCancel(loc);
            }
            public void confirm(int loc){
                if (loc > 0){
                    movePrevious(loc);
                } else {
                    moveNext(-loc);
                }
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void movingOpenSettings(int x){
                if (x < widthScreen*3/4) {
                    settingsPageCard.setTranslationX(x - widthScreen * 3 / 4);
                    settingsBlur.setCardBackgroundColor((int) Color.argb((Float) 0.25f*(1+(x - widthScreen * 3 / 4)/(widthScreen*3/4)),0.0f,0.0f,0.0f));
                }
            }
            public void cancelOpenSettings(int loc){
                ValueAnimator anim = ValueAnimator.ofFloat(loc, 0);
                anim.setDuration((int) ((widthScreen * 3 / 4 - loc)/animationVelocity));
                anim.start();

                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float value = (Float) animation.getAnimatedValue();
                        settingsPageCard.setTranslationX(value-widthScreen*3/4);
                        settingsBlur.setCardBackgroundColor((int) Color.argb((Float) 0.25f*(1+(value-widthScreen*3/4)/(widthScreen*3/4)),0.0f,0.0f,0.0f));
                    }
                });
            }
            public void confirmOpenSettings(int loc){
                openSettings(loc);
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void movingCloseSettings(int x){
                if (x < 0) {
                    settingsPageCard.setTranslationX(x);
                    settingsBlur.setCardBackgroundColor((int) Color.argb((Float) 0.25f*(1+(x)/(widthScreen*3/4)),0.0f,0.0f,0.0f));
                }
            }
            public void cancelCloseSettings(int loc){
                ValueAnimator anim = ValueAnimator.ofFloat(loc, 0);
                anim.setDuration((int) ((widthScreen*3/4 + loc)/animationVelocity));
                anim.start();

                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float value = (Float) animation.getAnimatedValue();
                        settingsPageCard.setTranslationX(value);
                        settingsBlur.setCardBackgroundColor((int) Color.argb((Float) 0.25f*(1+(value)/(widthScreen*3/4)),0.0f,0.0f,0.0f));
                    }
                });
            }
            public void confirmCloseSettings(int loc){
                closeSettings(loc);
            }
        };
        imageView.setOnTouchListener(swipeListener);
    }

    public void updateUI(){

        LinearLayoutManager layoutManagerHome = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        listWorkoutHome.setLayoutManager(layoutManagerHome);
        listWorkoutHome.addItemDecoration(new HorizontalSpaceItemDecoration(32));

        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        listWorkout.setLayoutManager(customLinearLayoutManager);
        listWorkout.addItemDecoration(new VerticalSpaceItemDecoration(32));

        listAdapterHome = new WorkoutHomeAdapter(workoutsDB, this, listWorkoutHome, userID);
        listWorkoutHome.setAdapter(listAdapterHome);

        listAdapterWorkout = new WorkoutAdapter(true, workoutsDB, this, listWorkout, customLinearLayoutManager, (int) widthScreen);
        listWorkout.setAdapter(listAdapterWorkout);

        pageL1.setTranslationX(-widthScreen);
        pageR1.setTranslationX(widthScreen);
        settingsPageCard.getLayoutParams().width = (int) widthScreen*3/4;
        settingsPageCard.setTranslationX(-widthScreen*3/4);
        butCancelSettings.getLayoutParams().width = (int) widthScreen/4;
        butCancelSettings.setTranslationX(widthScreen/4);

        textUser.setText((String) userDB.get("username"));

        testData = new ArrayList<>();
        testData.add(0.0);
        testData.add(1.0);
        testData.add(3.0);
        testData.add(2.0);
        testData.add(3.0);
        testData.add(4.0);
        testData.add(1.0);

        double average = 0.0;

        for (double number : testData){
            average += number;
        }
        average = average/testData.size();

        textAverage.setText("Average: " + Double.toString(average));

        ProgressChart progressChartH = new ProgressChart(testData, 2.0, 0.05, 0.5, 5, false, 8, 0);
        XYMultipleSeriesRenderer mRendererH = progressChartH.getChartView();
        XYMultipleSeriesDataset datasetH = progressChartH.getDataSet();
        GraphicalView chartViewH = ChartFactory.getLineChartView(getApplicationContext(), datasetH, mRendererH);

        LinearLayout progressChartHome = (LinearLayout) findViewById(R.id.progressChartHome);
        progressChartHome.addView(chartViewH);

        ProgressChart progressChartP = new ProgressChart(testData, 2.0, 0.05, 0.5, 10, true, 8, 2);
        XYMultipleSeriesRenderer mRendererP = progressChartP.getChartView();
        XYMultipleSeriesDataset datasetP = progressChartP.getDataSet();
        GraphicalView chartViewP = ChartFactory.getLineChartView(getApplicationContext(), datasetP, mRendererP);

        progressChartProgress = (LinearLayout) findViewById(R.id.progressChartProgress);
        progressChartProgress.addView(chartViewP);
    }

    public void getWorkoutsFromDB(){

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Syncing data...");
        progressDialog.show();

        userID = userDB.get("ID").toString();

        db.collection("User").document(userID).collection("WorkOuts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String, Object> workoutObject = new HashMap<>();
                                workoutObject.put("name", document.get("Name").toString());
                                Context context = getApplicationContext();
                                int id = getResources().getIdentifier(document.get("Picture").toString(), "drawable", context.getPackageName());
                                workoutObject.put("imagePath", id);
                                workoutObject.put("ID", document.getId());

                                workoutsDB.add(workoutObject);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.hide();
                        updateUI();
                    }
                });

    }

    public void movePrevious(int loc){
        if (currentScreen > -(numberOfScreens-1)/2) {
            currentScreen--;

            ValueAnimator anim = ValueAnimator.ofFloat(loc, widthScreen);
            anim.setDuration((int) ((widthScreen-loc)/animationVelocity));
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    if(currentScreen == 0) {
                        pageR1.setTranslationX(value);
                        page0.setTranslationX(value - widthScreen);
                        selector.setTranslationX((widthScreen/4)-(widthScreen/4)*(value-loc)/(widthScreen-loc));
                    } else if (currentScreen == -1){
                        page0.setTranslationX(value);
                        pageL1.setTranslationX(value - widthScreen);
                        selector.setTranslationX(-(widthScreen/4)*(value-loc)/(widthScreen-loc));
                    }
                }
            });
        } else {
            moveCancel(loc);
        }
    }

    public void moveDoublePrevious(){
        if (currentScreen > -(numberOfScreens-1)/2) {
            currentScreen-=2;

            ValueAnimator anim = ValueAnimator.ofFloat(0, widthScreen*2);
            anim.setDuration((int) ((widthScreen)/animationVelocity));
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    if(value < widthScreen) {
                        pageR1.setTranslationX(value);
                        page0.setTranslationX(value - widthScreen);
                        selector.setTranslationX((widthScreen/4)-(widthScreen/4)*(value)/(widthScreen));
                    } else if (value > widthScreen){
                        page0.setTranslationX(value - widthScreen);
                        pageL1.setTranslationX(value - widthScreen*2);
                        selector.setTranslationX(-(widthScreen/4)*(value-widthScreen)/(widthScreen));
                    }
                }
            });
        } else {
            moveCancel(0);
        }
    }

    public void moveNext(int loc){
        if (currentScreen < +(numberOfScreens-1)/2) {
            currentScreen ++;

            ValueAnimator anim = ValueAnimator.ofFloat(loc, widthScreen);
            anim.setDuration((int) ((widthScreen-loc)/animationVelocity));
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    if(currentScreen == 0) {
                        pageL1.setTranslationX(-value);
                        page0.setTranslationX(widthScreen - value);
                        selector.setTranslationX(-(widthScreen/4)+(widthScreen/4)*(value-loc)/(widthScreen-loc));
                    } else if (currentScreen == 1){
                        page0.setTranslationX(-value);
                        pageR1.setTranslationX(widthScreen - value);
                        selector.setTranslationX((widthScreen/4)*(value-loc)/(widthScreen-loc));
                    }
                }
            });
        } else {
            moveCancel(-loc);
        }
    }

    public void moveDoubleNext(){
        if (currentScreen < +(numberOfScreens-1)/2) {
            currentScreen +=2;

            ValueAnimator anim = ValueAnimator.ofFloat(0, widthScreen*2);
            anim.setDuration((int) ((widthScreen)/animationVelocity));
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    if(value < widthScreen) {
                        pageL1.setTranslationX(-value);
                        page0.setTranslationX(widthScreen - value);
                        selector.setTranslationX(-(widthScreen/4)+(widthScreen/4)*(value)/(widthScreen));
                    } else if (value > widthScreen){
                        page0.setTranslationX(-value + widthScreen);
                        pageR1.setTranslationX(widthScreen-value+widthScreen);
                        selector.setTranslationX((widthScreen/4)*(value-widthScreen)/(widthScreen));
                    }
                }
            });
        } else {
            moveCancel(0);
        }
    }

    public void moveCancel(int loc){
        ValueAnimator anim = ValueAnimator.ofFloat(loc, 0);
        loc = Math.abs(loc);
        anim.setDuration((int) (loc/animationVelocity));
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                if (currentScreen == -1) {
                    page0.setTranslationX(value + widthScreen);
                    pageL1.setTranslationX(value);
                } else if(currentScreen == 0) {
                    page0.setTranslationX(value);
                    pageL1.setTranslationX(value - widthScreen);
                    pageR1.setTranslationX(value + widthScreen);
                } else if (currentScreen == 1){
                    page0.setTranslationX(value - widthScreen);
                    pageR1.setTranslationX(value);
                }
            }
        });
    }

    public void openSettings(int loc){
        if (loc < widthScreen*3/4) {
            ValueAnimator anim = ValueAnimator.ofFloat(loc, widthScreen * 3 / 4);
            anim.setDuration((int) ((widthScreen * 3 / 4 - loc) / animationVelocity));
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    settingsPageCard.setTranslationX(value - widthScreen * 3 / 4);
                    settingsBlur.setCardBackgroundColor((int) Color.argb((Float) 0.25f*(1+(value - widthScreen * 3 / 4)/(widthScreen*3/4)),0.0f,0.0f,0.0f));
                }
            });
        } else {
            settingsPage.setTranslationX(0);
        }
        swipeListener.setSettings(true);
        butCancelSettings.setTranslationX(0);
    }

    public void closeSettings(int loc){

        if (loc > -widthScreen*3/4) {
            ValueAnimator anim = ValueAnimator.ofFloat(loc, -widthScreen*3/4);
            anim.setDuration((int) ((widthScreen*3/4 + loc) / animationVelocity));
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    settingsPageCard.setTranslationX(value);
                    settingsBlur.setCardBackgroundColor((int) Color.argb((Float) 0.25f*(1+(value)/(widthScreen*3/4)),0.0f,0.0f,0.0f));
                }
            });
        } else {
            settingsPage.setTranslationX(-widthScreen*3/4);
        }
        swipeListener.setSettings(false);
        butCancelSettings.setTranslationX(widthScreen/4);
    }

    public void butProgress(View caller) {
        if (currentScreen == -1){

        } else if (currentScreen == 0){
            movePrevious(0);
        } else if (currentScreen == 1){
            moveDoublePrevious();
        }
    }

    public void butHome(View caller){
        if (currentScreen == -1){
            moveNext(0);
        } else if (currentScreen == 0){

        } else if (currentScreen == 1){
            movePrevious(0);
        }
    }

    public void butWorkout(View caller){
        if (currentScreen == -1){
            moveDoubleNext();
        } else if (currentScreen == 0){
            moveNext(0);
        } else if (currentScreen == 1){

        }
    }

    public void butSettings(View caller){
        openSettings(0);
    }

    public void butCloseSettings(View caller){
        closeSettings(0);
    }

    public void butMonth (View caller){
        textMonth.setTypeface(null, Typeface.BOLD);
        textWeek.setTypeface(null, Typeface.NORMAL);
        textDay.setTypeface(null, Typeface.NORMAL);

        progressChartProgress.removeAllViews();

        ProgressChart progressChartP = new ProgressChart(testData, 2.0, 0.05, 0.5, 10, true, 8, 2);
        XYMultipleSeriesRenderer mRendererP = progressChartP.getChartView();
        XYMultipleSeriesDataset datasetP = progressChartP.getDataSet();
        GraphicalView chartViewP = ChartFactory.getLineChartView(getApplicationContext(), datasetP, mRendererP);

        progressChartProgress = (LinearLayout) findViewById(R.id.progressChartProgress);
        progressChartProgress.addView(chartViewP);
    }

    public void butWeek (View caller){
        textMonth.setTypeface(null, Typeface.NORMAL);
        textWeek.setTypeface(null, Typeface.BOLD);
        textDay.setTypeface(null, Typeface.NORMAL);

        progressChartProgress.removeAllViews();

        ProgressChart progressChartP = new ProgressChart(testData, 2.0, 0.05, 0.5, 10, true, 8, 1);
        XYMultipleSeriesRenderer mRendererP = progressChartP.getChartView();
        XYMultipleSeriesDataset datasetP = progressChartP.getDataSet();
        GraphicalView chartViewP = ChartFactory.getLineChartView(getApplicationContext(), datasetP, mRendererP);

        progressChartProgress = (LinearLayout) findViewById(R.id.progressChartProgress);
        progressChartProgress.addView(chartViewP);
    }

    public void butDay (View caller){
        textMonth.setTypeface(null, Typeface.NORMAL);
        textWeek.setTypeface(null, Typeface.NORMAL);
        textDay.setTypeface(null, Typeface.BOLD);

        progressChartProgress.removeAllViews();

        ProgressChart progressChartP = new ProgressChart(testData, 2.0, 0.05, 0.5, 10, true, 8, 0);
        XYMultipleSeriesRenderer mRendererP = progressChartP.getChartView();
        XYMultipleSeriesDataset datasetP = progressChartP.getDataSet();
        GraphicalView chartViewP = ChartFactory.getLineChartView(getApplicationContext(), datasetP, mRendererP);

        progressChartProgress = (LinearLayout) findViewById(R.id.progressChartProgress);
        progressChartProgress.addView(chartViewP);
    }

    public void butSignOut(View caller){
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}