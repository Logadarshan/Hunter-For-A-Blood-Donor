package com.example.hbd.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.hbd.Appointment.AppointmentStep1Fragment;
import com.example.hbd.Appointment.AppointmentStep2Fragment;
import com.example.hbd.Appointment.AppointmentStep3Fragment;
import com.example.hbd.Appointment.AppointmentStep4Fragment;
import com.example.hbd.Appointment.StaffAllAppointmentFragment;
import com.example.hbd.Appointment.StaffConfirmedAppointmentFragment;
import com.example.hbd.Appointment.StaffUpcomingAppointmentFragment;

public class StaffAppointmentAdapter extends FragmentPagerAdapter {
    public StaffAppointmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {


        switch (position)
        {
            case 0 :
                return StaffUpcomingAppointmentFragment.getInstance();
            case 1 :
                return StaffConfirmedAppointmentFragment.getInstance();
            case 2:
                return StaffAllAppointmentFragment.getInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
