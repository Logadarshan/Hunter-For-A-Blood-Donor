package com.example.hbd.Home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hbd.Appointment.StaffAppointmentFragment;
import com.example.hbd.Camp.AddCampFragment;
import com.example.hbd.News.NewsfeedFragment;
import com.example.hbd.Profile.DonorProfileFragment;
import com.example.hbd.R;
import com.example.hbd.Users.DirectoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class StaffHome extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    NewsfeedFragment newsFeedFragment = new NewsfeedFragment();
    DonorProfileFragment donorProfileFragment = new DonorProfileFragment();
    StaffAppointmentFragment staffAppointmentFragment1 = new StaffAppointmentFragment();

    AddCampFragment addCampFragment = new AddCampFragment();

    DirectoryFragment directoryFragment = new DirectoryFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        bottomNavigationView = findViewById(R.id.bottom1);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,donorProfileFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.Profile1:
                        // View Profile Page
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,donorProfileFragment).commit();
                        return true;
                    case R.id.Appointment1:
                        // Appointment Page for Staff
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,staffAppointmentFragment1).commit();
                        return true;
                    case R.id.NewsFeed1:
                        // News Feed Page for Staff
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,newsFeedFragment).commit();
                        return true;
                    case R.id.Camp1:
                        // View Donor List Page
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,addCampFragment).commit();
                        return true;
                    case R.id.Users1:
                        // View Donor List Page
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,directoryFragment).commit();
                        return true;

                }

                return false;
            }
        });






    }
}