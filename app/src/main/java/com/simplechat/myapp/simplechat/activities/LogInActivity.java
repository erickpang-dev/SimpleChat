package com.simplechat.myapp.simplechat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.helpers.Preferences;

import java.util.HashMap;
import java.util.Random;

public class LogInActivity extends AppCompatActivity {

    private EditText usernameInput, phoneInput;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



// linking layout objects with class objects
        usernameInput = findViewById(R.id.usernameInputId);
        phoneInput = findViewById(R.id.phoneInputId);
        sendButton = findViewById(R.id.sendButtonId);

// defining phone mask
        SimpleMaskFormatter simpleMaskPhoneNumber = new SimpleMaskFormatter( "+NN (NN) NNNNN-NNNN" );
        MaskTextWatcher maskPhoneNumber = new MaskTextWatcher(phoneInput, simpleMaskPhoneNumber);
        phoneInput.addTextChangedListener( maskPhoneNumber );


// setting button event
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = usernameInput.getText().toString();
                String phoneNumber = phoneInput.getText().toString();

// removing phone number format
                String phoneNumberUnformatted = phoneNumber.replace("+","");
                phoneNumberUnformatted = phoneNumberUnformatted.replace("(","");
                phoneNumberUnformatted = phoneNumberUnformatted.replace(")","");
                phoneNumberUnformatted = phoneNumberUnformatted.replace("-","");
                phoneNumberUnformatted = phoneNumberUnformatted.replace(" ","");

// generating token (observation: for better security, tokens should be generated and validated by a webservice and not by the application itself)
                Random random = new Random();
                int randomNumber = random.nextInt(9999-1000 ) + 1000;
                String token = String.valueOf( randomNumber );
                String tokenMessage = "Your validation code is: " + token;



// saving data for validation
                Preferences preferences = new Preferences( getApplicationContext() );
                preferences.saveUserPreferences( userName, phoneNumberUnformatted, token );

// send sms
                phoneNumberUnformatted = "5554";
                sendSMS("+" + phoneNumberUnformatted, tokenMessage);

//                HashMap<String, String> user = preferences.getUserData();
//                Log.i("LogInActivity", user.get( "token" ));


            }
        });





    }
    private boolean sendSMS(String phoneNumber, String message){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
