package com.simplechat.myapp.simplechat.activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.config.FirebaseConfiguration;

public class LogInActivity extends AppCompatActivity {

    private DatabaseReference firebaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        firebaseReference = FirebaseConfiguration.getFirebase();


    }

    public void goToSignUpActivity( View view ){
        Intent logInToSignUp = new Intent(this, SignUpActivity.class);
        startActivity(logInToSignUp);
    }
}
