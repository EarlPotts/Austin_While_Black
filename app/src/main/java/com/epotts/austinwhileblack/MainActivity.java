package com.epotts.austinwhileblack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button signOutButt;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOutButt = findViewById(R.id.signOutButton);
        auth = FirebaseAuth.getInstance();

        signOutButt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "Signed out!", Toast.LENGTH_LONG).show();
                auth.signOut();
                startActivity(new Intent(MainActivity.this, Signup.class));
            }
        });
    }
}
