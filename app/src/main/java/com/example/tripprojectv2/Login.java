package com.example.tripprojectv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {

    private static final String TAG = "MainActivityTAG";

    private FirebaseAuth firebaseAuth;

    public static final int REQUEST_CODE = 1;

    //  FireStore instance
    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    //  email address input
    EditText loginEmail;

    //  password input
    EditText loginPassword;

    //  login button
    Button loginButton;

    //  sign up button
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpSubmitButton);

        firebaseAuth = FirebaseAuth.getInstance();

        Context ctx = getApplicationContext();

        SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        String userData = sharedPreferences.getString("userData", "");
        Log.d("demo", "User_local Data : " + userData);

        //  Clicking on Login button
        //  Validates, email and password
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                if (validateForm()) {
                    signIn(email, password);
                }
            }
        });

        //  clicking on SignUp button
        //  invokes an Intent to Sign Up Activity
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUpActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    private void signIn(String email, String password) {

        Log.d(TAG, "Email ID : " + email);

        if (!validateForm()) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.w(TAG, "Exception while signIn occured : " + task.getException());
                            Toast.makeText(Login.this, "Login Attempt Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //  Form validation
    //  Checking email and password fields
    private boolean validateForm() {

        boolean valid = true;

        String email = loginEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            loginEmail.setError("Please Enter Email ID");
            valid = false;
        } else {
            loginEmail.setError(null);
        }

        String password = loginPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            loginPassword.setError("Please Enter Password");
            valid = false;
        } else {
            loginPassword.setError(null);
        }

        return valid;
    }

}