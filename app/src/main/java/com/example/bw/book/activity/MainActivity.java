package com.example.bw.book.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bw.book.R;
import com.example.bw.book.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mFirebaseDatabase;

    private EditText emailText, passwordText, nameText;
    private Button mRegister;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailText = (EditText) findViewById(R.id.Email);
        passwordText = (EditText) findViewById(R.id.Password);
        nameText = (EditText) findViewById(R.id.Name);
        mRegister = (Button) findViewById(R.id.button2);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in
                    mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("User");
                    mFirebaseDatabase.child(currentUser.getUid()).setValue(user);
                    Toast.makeText(getApplicationContext(), "Successfully signed in with: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                    if (user.getName().equalsIgnoreCase("Admin")) {
                        startActivity(new Intent(MainActivity.this, AdminWelcomePage.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, CustomerWelcome.class));
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String name = nameText.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter in a email address", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter in a password", Toast.LENGTH_SHORT).show();
                }else if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter in your name", Toast.LENGTH_SHORT).show();
                } else {

                }
                mAuth.createUserWithEmailAndPassword(email, password);
                user = new User(name, email, password);
                mRegister.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}