package com.simplechat.myapp.simplechat.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.simplechat.myapp.simplechat.Manifest;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.helpers.Permissions;
import com.simplechat.myapp.simplechat.helpers.Preferences;

import java.util.HashMap;
import java.util.Random;

public class LogInActivity extends AppCompatActivity {

    private EditText usernameInput, phoneInput;
    private Button sendButton;
    private String[] permissionsRequired = new String[]{
            android.Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

// checking for permissions
        Permissions.permissionsValidate(1,this, permissionsRequired);


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
                boolean sentSMS = sendSMS("+" + phoneNumberUnformatted, tokenMessage);

                if (sentSMS){
                    Intent intent = new Intent(LogInActivity.this, ValidationActivity.class);
                    startActivity( intent );
                } else {
                    Toast.makeText(LogInActivity.this, "SMS could not be sent.", Toast.LENGTH_SHORT).show();
                }

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

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int results : grantResults){
            if (results == PackageManager.PERMISSION_DENIED){
                permissionIsEssentialAlert();
            }
        }
    }
    private void permissionIsEssentialAlert(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder( this );
        alertBuilder.setTitle("Pemissions denied");
        alertBuilder.setMessage("You must grant access to all permissions to use this application.");
        alertBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();

    }
}
