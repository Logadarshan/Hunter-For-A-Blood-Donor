package com.example.hbd.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.hbd.Appointment.AppointmentStep1Fragment;
import com.example.hbd.Appointment.AppointmentStep2Fragment;
import com.example.hbd.Appointment.AppointmentStep3Fragment;
import com.example.hbd.Appointment.AppointmentStep4Fragment;

public class PageAdapter extends FragmentPagerAdapter {
    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {


        switch (position)
        {
            case 0 :
                return AppointmentStep1Fragment.getInstance();
            case 1 :
                return AppointmentStep2Fragment.getInstance();
            case 2 :
                return AppointmentStep3Fragment.getInstance();
            case 3 :
                return AppointmentStep4Fragment.getInstance();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
