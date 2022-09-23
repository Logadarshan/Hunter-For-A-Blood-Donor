package com.example.hbd.Appointment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hbd.Adapter.TimeSlotAdapter;
import com.example.hbd.Model.SlotModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AppointmentStep3Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;


    @BindView(R.id.recyclertime)
    RecyclerView timerecycle;

    private BroadcastReceiver TimeDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<SlotModel> time = intent.getParcelableArrayListExtra(Common.KEY_TIME_LOAD_DONE);
            TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(getContext(), time);
            timerecycle.setAdapter(timeSlotAdapter);
        }
    };


    static AppointmentStep3Fragment instance;

    public static AppointmentStep3Fragment getInstance(){

        if(instance == null)
            instance = new AppointmentStep3Fragment();
        return instance;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(TimeDoneReceiver, new IntentFilter(Common.KEY_TIME_LOAD_DONE));


    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(TimeDoneReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_appointment_step3, container, false);

        unbinder = ButterKnife.bind(this,v);


        initView();


        return v;
    }

    private void initView() {
        timerecycle.setHasFixedSize(true);
        timerecycle.setLayoutManager(new GridLayoutManager(getActivity(),2));
        timerecycle.addItemDecoration(new SpacesItemDecoration(4));

    }
}