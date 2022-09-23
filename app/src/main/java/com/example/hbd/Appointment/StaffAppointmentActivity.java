package com.example.hbd.Appointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.hbd.Adapter.PageAdapter;
import com.example.hbd.Adapter.StaffAppointmentAdapter;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.NonSwipeViewPager;
import com.example.hbd.R;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffAppointmentActivity extends AppCompatActivity {


    @BindView(R.id.idstaffviewpager)
    ViewPager viewPager1;


    LocalBroadcastManager localBroadcastManager;





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_appointment);
        ButterKnife.bind(StaffAppointmentActivity.this);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);



        viewPager1.setAdapter(new StaffAppointmentAdapter(getSupportFragmentManager()));
        viewPager1.setOffscreenPageLimit(2);
        viewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }





}