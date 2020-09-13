package com.ArnoVanEetvelde.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 9001;
    private EditText textEmail, textPassword, textUsername;
    private TextView textButConfirm, textButSwitch;
    private CardView cardInformation, cardConfirm, cardSwitch;
    private boolean boolLoging;
    private HashMap<String, Object> userDB;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogin();
            }
        });

        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassword = (EditText) findViewById(R.id.textPassword);
        textUsername = (EditText) findViewById(R.id.textUsername);
        textButConfirm = (TextView) findViewById(R.id.textButConfirm);
        textButSwitch = (TextView) findViewById(R.id.textButSwitch);
        cardInformation = (CardView) findViewById(R.id.cardInformation);
        cardConfirm = (CardView) findViewById(R.id.cardConfirm);
        cardSwitch = (CardView) findViewById(R.id.cardSwitch);
    }

    @Override
    public void onStart() {
        super.onStart();
        textUsername.setVisibility(View.INVISIBLE);
        boolLoging = true;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            goToMainPage(user.getEmail().toString());
        }
    }

    public void switchMode(View caller){
        if (boolLoging) {

            int initialHeight = cardInformation.getHeight();
            int marginHeight = (int) dpToPx(32);
            int addHeight = textEmail.getHeight() + marginHeight;

            ValueAnimator anim2 = ValueAnimator.ofFloat(0, 1);
            anim2.setDuration(250);

            ValueAnimator anim = ValueAnimator.ofFloat(1, 0);
            anim.setDuration(250);
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    textButConfirm.setAlpha(value);
                    textButSwitch.setAlpha(value);
                }
            });
            anim.addListener(new ValueAnimator.AnimatorListener(){

                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    textButSwitch.setText("LOGIN");
                    textButConfirm.setText("SIGN UP");
                    anim2.start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    textButSwitch.setText("LOGIN");
                    textButConfirm.setText("SIGN UP");
                    anim2.start();
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    textButConfirm.setAlpha(value);
                    textButSwitch.setAlpha(value);
                }
            });

            ValueAnimator anim3 = ValueAnimator.ofFloat(0, 1);
            anim3.setDuration(500);
            anim3.start();

            anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    if (value*addHeight > marginHeight){
                        if (textUsername.getVisibility() != View.VISIBLE){
                            textUsername.setVisibility(View.VISIBLE);
                        }
                        textUsername.setAlpha(value);
                    }

                    cardInformation.getLayoutParams().height = initialHeight + (int) (value*addHeight);
                    cardInformation.requestLayout();
                }
            });

            boolLoging = false;
        } else {

            int initialHeight = cardInformation.getHeight();
            int minHeight = textEmail.getHeight() + (int) dpToPx(32);

            ValueAnimator anim2 = ValueAnimator.ofFloat(0, 1);
            anim2.setDuration(250);

            ValueAnimator anim = ValueAnimator.ofFloat(1, 0);
            anim.setDuration(250);
            anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    textButConfirm.setAlpha(value);
                    textButSwitch.setAlpha(value);
                }
            });
            anim.addListener(new ValueAnimator.AnimatorListener(){

                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    textButSwitch.setText("SIGN UP");
                    textButConfirm.setText("LOGIN");
                    anim2.start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    textButSwitch.setText("SIGN UP");
                    textButConfirm.setText("LOGIN");
                    anim2.start();
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    textButConfirm.setAlpha(value);
                    textButSwitch.setAlpha(value);
                }
            });

            ValueAnimator anim3 = ValueAnimator.ofFloat(0, 1);
            anim3.setDuration(500);
            anim3.start();

            anim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = (Float) animation.getAnimatedValue();
                    textUsername.setAlpha(1-value);
                    cardInformation.getLayoutParams().height = initialHeight - (int) (value*minHeight);
                    cardInformation.requestLayout();
                }
            });
            anim3.addListener(new ValueAnimator.AnimatorListener(){

                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    textUsername.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    textUsername.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            boolLoging = true;
        }
    }

    public void confirm(View caller){
        if (boolLoging) {
            login();
        } else {
            signup();
        }
    }

    public void signup(){
        String email = textEmail.getText().toString();
        String userName = textUsername.getText().toString();
        String password = textPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUser(user.getEmail(), userName);
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void login(){
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToMainPage(mAuth.getCurrentUser().getEmail().toString());
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            textPassword.setText("");
                        }
                    }
                });
    }

    public void googleLogin(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        //Toast.makeText(getApplicationContext(), requestCode, Toast.LENGTH_SHORT).show();
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "Google sign in failed" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserIfNotExisting();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "signInWithCredential:failure" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goToMainPage(String emailAddress){

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        db.collection("User")
                .whereEqualTo("email", emailAddress)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userDB = new HashMap<>();
                                userDB.put("email", document.get("email"));
                                userDB.put("username", document.get("username"));
                                userDB.put("goal", document.get("goal"));
                                userDB.put("ID", document.getId());

                                progressDialog.hide();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("user", userDB);
                                startActivity(intent);

                                break;
                            }
                        } else {
                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addUserIfNotExisting(){
        String emailAddress = mAuth.getCurrentUser().getEmail().toString();

        db.collection("User")
                .whereEqualTo("email", emailAddress)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0){
                                askUsername();
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    goToMainPage(emailAddress);
                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void askUsername(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!isFinishing()){

                    final EditText input = new EditText(LoginActivity.this);

                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Username:")
                            .setCancelable(true)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (input.getText().toString() != null && input.getText().toString().equals("") == false) {
                                        addUser(mAuth.getCurrentUser().getEmail(), input.getText().toString());
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Username is required.", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                    }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                        }
                    }).setView(input).show();
                }
            }
        });
    }

    public void addUser(String emailAddress, String userName){

        Map<String, Object> user = new HashMap<>();
        user.put("email", emailAddress);
        user.put("username", userName);
        user.put("goal", 0);

        db.collection("User")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        goToMainPage(emailAddress);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error adding user" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void forgotPassword(View caller){
        String email = textEmail.getText().toString();
        if (email.equals("") == false && email != null) {
            mAuth.sendPasswordResetEmail(email);
            Toast.makeText(getApplicationContext(), "An email was send to your email address.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Email is required.", Toast.LENGTH_SHORT).show();
        }
    }

    public float dpToPx(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}