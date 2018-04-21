package com.simplechat.myapp.simplechat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.helpers.Preferences;

import java.util.HashMap;

public class ValidationActivity extends AppCompatActivity {

    private EditText validateInput;
    private Button validateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        validateInput = findViewById(R.id.validateInputId);
        validateButton = findViewById(R.id.validateButtonId);

        SimpleMaskFormatter simpleMaskValidateInput = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskValidateInput = new MaskTextWatcher(validateInput, simpleMaskValidateInput);


        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences preferences = new Preferences( ValidationActivity.this );
                HashMap<String, String> user = preferences.getUserData();

                String token = user.get("token");
                String typedToken = validateInput.getText().toString();

                if ( typedToken.equals(token)){

                } else {
                    Toast.makeText(ValidationActivity.this, "The number you typed is not the token generated.", Toast.LENGTH_SHORT).show();
                }

            }
        });





    }
}
