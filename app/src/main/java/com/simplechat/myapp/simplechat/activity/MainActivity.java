package com.simplechat.myapp.simplechat.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.simplechat.myapp.simplechat.R;
import com.simplechat.myapp.simplechat.adapter.TabAdapter;
import com.simplechat.myapp.simplechat.configuration.FirebaseConfiguration;
import com.simplechat.myapp.simplechat.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

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

    public void signOut() {
        firebaseAuth.signOut();
        Intent mainActivityToLogIn = new Intent(this, LogInActivity.class);
        startActivity(mainActivityToLogIn);
        finish();
    }
}
