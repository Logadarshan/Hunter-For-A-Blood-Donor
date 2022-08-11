package com.example.hbd.Home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hbd.Appointment.AppointmentFragment;
import com.example.hbd.News.DonorNewsViewFragment;
import com.example.hbd.Profile.DonorProfileFragment;
import com.example.hbd.R;
import com.example.hbd.Selftest.SelftestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DonorHome extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    AppointmentFragment appointmentFragment = new AppointmentFragment();
    DonorNewsViewFragment donorNewsViewFragment = new DonorNewsViewFragment();
    SelftestFragment selfTestFragment = new SelftestFragment();
    DonorProfileFragment profileFragment = new DonorProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home);



        bottomNavigationView = findViewById(R.id.bottom);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.Profile:
                        // View Profile Page
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                        return true;
                    case R.id.Appointment:
                        // Appointment Page for Donor
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,appointmentFragment).commit();
                        return true;
                    case R.id.NewsFeed:
                        // News Feed Page for Donor
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,donorNewsViewFragment).commit();
                        return true;
                    case R.id.SelfTest:
                        // Self Test Page for Donor
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,selfTestFragment).commit();
                        return true;

                }

                return false;
            }
        });






    }
}