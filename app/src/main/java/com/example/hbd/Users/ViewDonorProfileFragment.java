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


public class ViewDonorProfileFragment extends Fragment {


    String Uname;
    String Ublood;
    String Uage;
    String Ugender;
    String Uhandphone;
    String Uemail;
    String Uaddress;
    String Uhos;
    String Uoccupation;

    public ViewDonorProfileFragment() {

    }

    public ViewDonorProfileFragment(String Uname, String Ublood, String Uage ,String Ugender,String Uhandphone ,String Uemail,
                                   String Uaddress ,String Uhos, String Uoccupation) {

        this.Uname = Uname;
        this.Ublood = Ublood;
        this.Uage = Uage;
        this.Ugender = Ugender;
        this.Uhandphone = Uhandphone;
        this.Uemail = Uemail;
        this.Uaddress = Uaddress;
        this.Uhos = Uhos;
        this.Uoccupation = Uoccupation;
    }


    // Display donor details
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_view_donor_profile, container, false);


        TextView dnameuser = v.findViewById(R.id.donorusersname);
        TextView dblooduser = v.findViewById(R.id.donorusersblood);
        TextView dageuser = v.findViewById(R.id.donorusersage);
        TextView dgenderuser = v.findViewById(R.id.donorusersgender);
        TextView dphoneuser = v.findViewById(R.id.donorusersphone);
        TextView demailuser = v.findViewById(R.id.donorusersemail);
        TextView dadduser = v.findViewById(R.id.donorusersadd);
        TextView dhosuser = v.findViewById(R.id.donorusershos);
        TextView doccupationuser = v.findViewById(R.id.donorusersoccupation);


        Button back2 = v.findViewById(R.id.backdonorBtn);

        dnameuser.setText(Uname);
        dblooduser.setText(Ublood);
        dageuser.setText(Uage);
        dgenderuser.setText(Ugender);
        dphoneuser.setText(Uhandphone);
        demailuser.setText(Uemail);
        dadduser.setText(Uaddress);
        dhosuser.setText(Uhos);
        doccupationuser.setText(Uoccupation);


        // Navigate user interface
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment duser = new DonorUserFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,duser).commit();


            }
        });
        return v;
    }
}