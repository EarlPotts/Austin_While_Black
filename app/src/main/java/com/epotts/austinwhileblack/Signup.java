package com.epotts.austinwhileblack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    private EditText email, pass;
    private FirebaseAuth auth;
    private String TAG;
    private FirebaseUser user;
    private Switch registerSwitch;
    private DatabaseReference database;
    private EditText fnBox;
    private EditText lnBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = findViewById(R.id.userEmail);
        pass = findViewById(R.id.userPass);
        auth = FirebaseAuth.getInstance();
        TAG = "Signup";
        registerSwitch = findViewById(R.id.signOrRegister);
        database = FirebaseDatabase.getInstance().getReference();
        fnBox = findViewById(R.id.firstNameBox);
        lnBox = findViewById(R.id.lastNameBox);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String address = email.getText().toString();
                String pw = pass.getText().toString();

                boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(address).matches();

                //user wants to regsiter a new account
                if(registerSwitch.isChecked()) {
                    createUser(address, pw);
                }
                //user wants to sign into an existing account
                else {
                    signIn(address, pw);
                }
            }
        });


        registerSwitch.setOnClickListener(new View.OnClickListener (){
            public void onClick(View v) {
                if(registerSwitch.isChecked()){
                    fnBox.setVisibility(View.VISIBLE);
                    lnBox.setVisibility(View.VISIBLE);
                }else{
                    fnBox.setVisibility(View.GONE);
                    lnBox.setVisibility(View.GONE);
                }
            }
        });
    }

    private void signIn(String address, String pw) {
        auth.signInWithEmailAndPassword(address, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signin:success");
                    FirebaseUser user = auth.getCurrentUser();
                    Toast.makeText(Signup.this, "Signed In!",
                            Toast.LENGTH_SHORT).show();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Signup.this, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                // ...
            }
        });
    }

    public void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        updateUI(user);
    }

    public void signOut(){
        auth.signOut();
    }
    private void createUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //add user info into the database
                            DatabaseReference userData = database.child(user.getUid());
                            userData.child("first_name").setValue(fnBox.getText().toString().trim());
                            userData.child("last_name").setValue(fnBox.getText().toString().trim());


                            Toast.makeText(Signup.this, "Registered!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        //already logged in
        if(user != null){
            startActivity(new Intent(Signup.this, MainActivity.class));
        }
    }

}
