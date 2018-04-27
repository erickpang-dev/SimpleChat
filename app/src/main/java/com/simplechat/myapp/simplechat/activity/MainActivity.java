package com.simplechat.myapp.simplechat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.adapter.TabAdapter;
import com.simplechat.myapp.simplechat.configuration.FirebaseConfiguration;
import com.simplechat.myapp.simplechat.helper.Base64Converter;
import com.simplechat.myapp.simplechat.helper.Preferences;
import com.simplechat.myapp.simplechat.helper.SlidingTabLayout;
import com.simplechat.myapp.simplechat.model.Contact;
import com.simplechat.myapp.simplechat.model.User;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String contactIdentifier;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();

        toolbar = findViewById(R.id.toolbarId);
        slidingTabLayout = findViewById(R.id.slidingTabLayoutId);
        viewPager = findViewById(R.id.viewPagerId);

// configure sliding tab layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));


// configure sliding tab adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter( tabAdapter );
        slidingTabLayout.setViewPager(viewPager);





// create custom toolbar
        toolbar.setTitle("Simple Chat");
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.searchAction:

                return true;
            case R.id.addContactAction:
                addContact();
                return true;
            case R.id.configurationAction:

                return true;
            case R.id.signOutAction:
                signOut();
                return true;

            default: super.onOptionsItemSelected(item);
        }

        Log.i("itemid", String.valueOf(item.getItemId()));

        return super.onOptionsItemSelected(item);
    }

    private void addContact() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Add contact");
        alertDialogBuilder.setMessage("Inform the contact e-mail you'd like to add");
        alertDialogBuilder.setCancelable(false);

// add EditText into AlertDialog
        final EditText contactEmailInput = new EditText(this);
        alertDialogBuilder.setView(contactEmailInput);


// positive button
        alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String contactEmail = contactEmailInput.getText().toString();
                if (contactEmail.isEmpty()){
                    Toast.makeText(MainActivity.this, "You must inform the contact e-mail", Toast.LENGTH_SHORT).show();
                } else {
// encode typed email for database search
                    contactIdentifier = Base64Converter.base64Encode(contactEmail);
// get firebase database
                    databaseReference = FirebaseConfiguration.getFirebase().child("users").child(contactIdentifier);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
// get contact data
                                User contactUser = dataSnapshot.getValue(User.class);
// get current user identifier
                                Preferences preferences = new Preferences(MainActivity.this);
                                String currentUserIdentifier = preferences.getCurrentUserIdentifier();
// add contact
                                databaseReference = FirebaseConfiguration.getFirebase();
                                databaseReference = databaseReference.child("contacts")
                                                                     .child(currentUserIdentifier)
                                                                     .child(contactIdentifier);
                                Contact contact = new Contact();
                                contact.setContactIdentifier( contactIdentifier );
                                contact.setContactEmail( contactUser.getUserEmail() );
                                contact.setContactName( contactUser.getUserName() );
                                databaseReference.setValue( contact );

                            } else {
                                Toast.makeText(MainActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

// negative button
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialogBuilder.create();
        alertDialogBuilder.show();



    }

    public void signOut() {
        firebaseAuth.signOut();
        Intent mainActivityToLogIn = new Intent(this, LogInActivity.class);
        startActivity(mainActivityToLogIn);
        finish();
    }
}
