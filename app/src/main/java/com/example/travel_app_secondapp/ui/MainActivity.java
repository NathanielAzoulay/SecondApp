package com.example.travel_app_secondapp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.example.travel_app_secondapp.R;
import com.example.travel_app_secondapp.databinding.NavHeaderMainBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    protected String userEmail;
    protected String companyName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userEmail = bundle.getString("userEmail");
            // takes only the name of the email address (example: monalisa@gmail.com -> monalisa)
            companyName = userEmail.replaceAll("@[a-z]+\\.+[a-z]+", "");
        }
        View viewHeader = navigationView.getHeaderView(0);
        NavHeaderMainBinding navViewHeaderBinding = NavHeaderMainBinding.bind(viewHeader);
        navViewHeaderBinding.setUserEmail(userEmail);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_registered, R.id.nav_company, R.id.nav_history)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

}