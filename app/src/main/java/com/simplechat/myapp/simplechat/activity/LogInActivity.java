package com.simplechat.myapp.simplechat.activity;


import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.configuration.FirebaseConfiguration;
import com.simplechat.myapp.simplechat.helper.Base64Converter;
import com.simplechat.myapp.simplechat.helper.Preferences;
import com.simplechat.myapp.simplechat.model.User;

public class LogInActivity extends AppCompatActivity {

    private EditText loginEmailInput, loginPasswordInput;
    private Button loginButton;
    private User user;
    private FirebaseAuth firebaseAuth;
    private ValueEventListener valueEventListenerUser;
    private DatabaseReference databaseReference;
    private String currentUserIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        checkLoggedInUser();

        loginEmailInput = findViewById(R.id.loginEmailInputId);
        loginPasswordInput = findViewById(R.id.loginPasswordInputId);
        loginButton = findViewById(R.id.loginButtonId);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();
                user.setUserEmail(loginEmailInput.getText().toString());
                user.setUserPassword(loginPasswordInput.getText().toString());
                validateLogin();
            }
        });
    }

    private void checkLoggedInUser() {
        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();
        if (firebaseAuth.getCurrentUser() != null){
            goToMainActivity();
        }
    }

    private void validateLogin(){
        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(
                user.getUserEmail(),
                user.getUserPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
// save user e-mail on preferences
                    currentUserIdentifier = Base64Converter.base64Encode(user.getUserEmail());

                    databaseReference = FirebaseConfiguration.getFirebase()
                            .child( "users" )
                            .child( currentUserIdentifier );

                    valueEventListenerUser = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User recoveredUser = dataSnapshot.getValue( User.class );

                            Preferences preferences = new Preferences(LogInActivity.this);
                            preferences.saveData( currentUserIdentifier, recoveredUser.getUserName());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    databaseReference.addListenerForSingleValueEvent(valueEventListenerUser);


// send user to main activity
                    Toast.makeText(LogInActivity.this, "Login was successful. Sending to main screen.", Toast.LENGTH_SHORT).show();
                    Handler delay = new Handler();
                    delay.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToMainActivity();
                        }
                    },3000);

                } else {
                    Toast.makeText(LogInActivity.this, "Login was not successful.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToMainActivity() {
        Intent logInToMainActivity = new Intent(this, MainActivity.class);
        startActivity(logInToMainActivity);
        finish();
    }

    public void goToSignUpActivity( View view ){
        Intent logInToSignUp = new Intent(this, SignUpActivity.class);
        startActivity(logInToSignUp);
    }
}
