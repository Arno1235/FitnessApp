package com.ArnoVanEetvelde.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private int RC_SIGN_IN;
    private EditText textEmail, textPassword, textUsername;
    private Button butConfirm, butSwitch;
    private boolean boolLoging;
    private HashMap<String, Object> userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        RC_SIGN_IN = 9001;
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
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
        butConfirm = (Button) findViewById(R.id.butConfirm);
        butSwitch = (Button) findViewById(R.id.butSwitch);
    }

    @Override
    public void onStart() {
        super.onStart();
        textUsername.setVisibility(View.GONE);
        boolLoging = true;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            getUser(user.getEmail().toString());
        }
    }

    public void goToMain(){
        if (mAuth.getCurrentUser() != null && userDB != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", userDB);
            startActivity(intent);
        }
    }

    public void switchMode(View caller){
        if (boolLoging) {
            textUsername.setVisibility(View.VISIBLE);
            butConfirm.setText("Sign up");
            butSwitch.setText("Login");
            boolLoging = false;
        } else {
            textUsername.setVisibility(View.GONE);
            butConfirm.setText("Login");
            butSwitch.setText("Sign up");
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
        String password = textPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUser(user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
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
                            getUser(mAuth.getCurrentUser().getEmail().toString());
                        } else {
                            // If sign in fails, display a message to the user.
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

    public void getUser(String emailAddress) {
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
                                goToMain();
                                break;
                            }
                        } else {
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
                                addUser(mAuth.getCurrentUser().getEmail());
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    userDB = new HashMap<>();
                                    userDB.put("email", document.get("email"));
                                    userDB.put("username", document.get("username"));
                                    userDB.put("goal", document.get("goal"));
                                    goToMain();
                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addUser(String emailAddress){

        Map<String, Object> user = new HashMap<>();
        user.put("email", emailAddress);
        user.put("username", textUsername.getText().toString());
        user.put("goal", 0);

        db.collection("User")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Toast.makeText(getApplicationContext(), "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                        getUser(emailAddress);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error adding document" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void logout(View caller){
        mAuth.signOut();
    }

    public void forgotPassword(View caller){
        String email = textEmail.getText().toString();
        mAuth.sendPasswordResetEmail(email);
    }

    public void deleteDB(String doc){
        db.collection("User").document(doc)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "DocumentSnapshot successfully deleted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error deleting document" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void removeUser(View caller){
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(getApplicationContext(), document.getId() + " => " + document.getData(), Toast.LENGTH_SHORT).show();
                                deleteDB(document.getId());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getDB(View caller) {
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(getApplicationContext(), document.getId() + " => " + document.getData(), Toast.LENGTH_SHORT).show();
                                //deleteDB(document.getId());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error getting documents." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}