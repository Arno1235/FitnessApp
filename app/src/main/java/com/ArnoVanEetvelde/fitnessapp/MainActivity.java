package com.ArnoVanEetvelde.fitnessapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {

    private LinearLayout page0, pageL1, pageR1;
    private int currentScreen, numberOfScreens = 3;
    private float widthScreen, heightScreen, animationVelocity = 2f;
    private ImageView appBar, progressIcon, homeIcon, workoutIcon;
    private View selector;
    private HashMap<String, Object> userDB;
    private ArrayList<HashMap<String, Object>> workoutsDB;
    private TextView textUser;
    private RecyclerView listWorkoutHome;
    private WorkoutHomeAdapter listAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userDB = (HashMap<String, Object>) extras.getSerializable("user");
        }

        page0 = (LinearLayout) findViewById(R.id.page0);
        pageL1 = (LinearLayout) findViewById(R.id.pageL1);
        pageR1 = (LinearLayout) findViewById(R.id.pageR1);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

        appBar = (ImageView) findViewById(R.id.appBar);
        progressIcon = (ImageView) findViewById(R.id.progressIcon);
        homeIcon = (ImageView) findViewById(R.id.homeIcon);
        workoutIcon = (ImageView) findViewById(R.id.workoutIcon);
        selector = findViewById(R.id.selector);
        textUser = (TextView) findViewById(R.id.textUser);

        listWorkoutHome = (RecyclerView) findViewById(R.id.listWorkoutHome);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        listWorkoutHome.setLayoutManager(layoutManager);
        listWorkoutHome.addItemDecoration(new HorizontalSpaceItemDecoration(20));

        workoutsDB = new ArrayList<>();
        HashMap<String, Object> test = new HashMap<>();
        test.put("name", "Run");
        Context context = getApplicationContext();
        int id = getResources().getIdentifier("pic00", "drawable", context.getPackageName());
        test.put("imagePath", id);
        workoutsDB.add(test);
        workoutsDB.add(test);
        workoutsDB.add(test);
        workoutsDB.add(test);
        workoutsDB.add(test);
        workoutsDB.add(test);
        workoutsDB.add(test);
        workoutsDB.add(test);
        workoutsDB.add(test);
        workoutsDB.add(test);
        listAdapter = new WorkoutHomeAdapter(workoutsDB, this);
        listWorkoutHome.setAdapter(listAdapter);

        currentScreen = 0;

        updateUI();

        ImageView imageView = (ImageView) findViewById(R.id.swipeDetection);
        imageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
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
        });
    }

    public void updateUI(){

        pageL1.setTranslationX(-widthScreen);
        pageR1.setTranslationX(widthScreen);

        textUser.setText((String) userDB.get("username"));

        appBar.getLayoutParams().height = (int) widthScreen*40/180;
        progressIcon.getLayoutParams().height = (int) widthScreen*40/360;
        homeIcon.getLayoutParams().height = (int) widthScreen*40/360;
        workoutIcon.getLayoutParams().height = (int) widthScreen*40/360;
        selector.getLayoutParams().width = (int) widthScreen*40/360;
        progressIcon.setTranslationY(-(widthScreen*40/180 - widthScreen*40/360)/2);
        homeIcon.setTranslationY(-(widthScreen*40/180 - widthScreen*40/360)/2);
        workoutIcon.setTranslationY(-(widthScreen*40/180 - widthScreen*40/360)/2);
        selector.setTranslationY(-(widthScreen*40/180 - widthScreen*40/360)/4);
        progressIcon.setTranslationX(widthScreen*40/1480);
        workoutIcon.setTranslationX(-widthScreen*40/1480);

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
}