package com.simplechat.myapp.simplechat.activity;

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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.configuration.FirebaseConfiguration;
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
// layout and java objects link
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

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);
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
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    user.setUserId( firebaseUser.getUid());
                    user.save();
                    firebaseAuth.signOut();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 3000);
                } else {
// sign up error treatment
                    String errorException = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        errorException = "Password not strong enough.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        errorException = "Invalid e-mail.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        errorException = "E-mail already in use on this application.";
                    } catch (Exception e) {
                        errorException = "Sign up was not successful.";
                        e.printStackTrace();
                    }
                    Toast.makeText(SignUpActivity.this, errorException, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
