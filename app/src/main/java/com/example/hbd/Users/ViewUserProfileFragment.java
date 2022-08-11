package com.example.hbd.Users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.R;


public class ViewUserProfileFragment extends Fragment {

    String Uname;
    String Ublood;
    String Uage;
    String Ugender;
    String Uhandphone;
    String Uemail;
    String Uaddress;
    String Uhos;

    public ViewUserProfileFragment() {

    }

    public ViewUserProfileFragment(String Uname, String Ublood, String Uage ,String Ugender,String Uhandphone ,String Uemail,
            String Uaddress ,String Uhos) {

        this.Uname = Uname;
        this.Ublood = Ublood;
        this.Uage = Uage;
        this.Ugender = Ugender;
        this.Uhandphone = Uhandphone;
        this.Uemail = Uemail;
        this.Uaddress = Uaddress;
        this.Uhos = Uhos;
    }

    // display user details
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_user_profile, container, false);


        TextView nameuser = v.findViewById(R.id.usersname);
        TextView blooduser = v.findViewById(R.id.usersblood);
        TextView ageuser = v.findViewById(R.id.usersage);
        TextView genderuser = v.findViewById(R.id.usersgender);
        TextView phoneuser = v.findViewById(R.id.usersphone);
        TextView emailuser = v.findViewById(R.id.usersemail);
        TextView adduser = v.findViewById(R.id.usersadd);
        TextView hosuser = v.findViewById(R.id.usershos);



        Button back = v.findViewById(R.id.backuserBtn);


        nameuser.setText(Uname);
        blooduser.setText(Ublood);
        ageuser.setText(Uage);
        genderuser.setText(Ugender);
        phoneuser.setText(Uhandphone);
        emailuser.setText(Uemail);
        adduser.setText(Uaddress);
        hosuser.setText(Uhos);


        // Navigate user interface
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment user = new UserFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,user).commit();


            }
        });



        return v;
    }
}