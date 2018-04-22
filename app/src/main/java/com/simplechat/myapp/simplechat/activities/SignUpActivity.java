package com.simplechat.myapp.simplechat.activities;

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
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.config.FirebaseConfiguration;
import com.simplechat.myapp.simplechat.model.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText signupNameInput, signupEmailInput, signupPasswordInput;
    private Button signupButton, returnButton;
    private User user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupNameInput = findViewById(R.id.signupNameInputId);
        signupEmailInput = findViewById(R.id.signupEmailInputId);
        signupPasswordInput = findViewById(R.id.signupPasswordInputId);
        signupButton = findViewById(R.id.signupButtonId);
        returnButton = findViewById(R.id.returnButtonId);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();
                user.setUserName(signupNameInput.getText().toString());
                user.setUserEmail(signupEmailInput.getText().toString());
                user.setUserPassword(signupPasswordInput.getText().toString());
                signupUser();
            }
        });
    }

    private void signupUser() {
        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getUserEmail(),
                user.getUserPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Sign up successful. Returning to login page.", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 3000);
                } else {
                    Toast.makeText(SignUpActivity.this, "Sign up was not successful.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
