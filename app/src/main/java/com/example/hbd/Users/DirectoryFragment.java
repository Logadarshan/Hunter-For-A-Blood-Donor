package com.example.hbd.Users;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.hbd.R;


public class DirectoryFragment extends Fragment {

    View v;
    ImageButton viewdonor, viewstaff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_directory, container, false);

        viewdonor = v.findViewById(R.id.viewdonorBtn3);
        viewstaff = v.findViewById(R.id.viewstaffBtn3);


        viewdonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment user = new DonorDirectoryFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,user).commit();
            }
        });

        viewstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment user = new StaffDirectoryFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,user).commit();
            }
        });





        return v;
    }
}