package com.example.hbd.Home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hbd.Profile.DonorProfileFragment;
import com.example.hbd.R;
import com.example.hbd.Users.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminHome extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    DonorProfileFragment donorProfileFragment = new DonorProfileFragment();
    UserFragment userFragment = new UserFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        bottomNavigationView = findViewById(R.id.bottom2);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,donorProfileFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.Profile2:
                        // View Profile Page
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,donorProfileFragment).commit();
                        return true;
                    case R.id.Users2:
                        // View User list Page
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,userFragment).commit();
                        return true;

                }

                return false;
            }
        });






    }







}