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

import com.example.hbd.Adapter.SlotAdapter;
import com.example.hbd.Model.SlotModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AppointmentStep2Fragment extends Fragment {


    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;


    @BindView(R.id.recycledate)
    RecyclerView daterecycle;



    private BroadcastReceiver DateDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<SlotModel> slot = intent.getParcelableArrayListExtra(Common.KEY_DATE_LOAD_DONE);
            SlotAdapter slotAdapter = new SlotAdapter(getContext(), slot);
            daterecycle.setAdapter(slotAdapter);
        }
    };



    static AppointmentStep2Fragment instance;

    public static AppointmentStep2Fragment getInstance(){

        if(instance == null)
            instance = new AppointmentStep2Fragment();
        return instance;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(DateDoneReceiver, new IntentFilter(Common.KEY_DATE_LOAD_DONE));

    }


    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(DateDoneReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v =inflater.inflate(R.layout.fragment_appointment_step2, container, false);

       unbinder = ButterKnife.bind(this,v);


       initView();


       return v;
    }

    private void initView() {
        daterecycle.setHasFixedSize(true);
        daterecycle.setLayoutManager(new GridLayoutManager(getActivity(),2));
        daterecycle.addItemDecoration(new SpacesItemDecoration(4));



    }
}