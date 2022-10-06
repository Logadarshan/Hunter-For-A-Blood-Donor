package com.example.hbd.Users;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.R;
import com.squareup.picasso.Picasso;


public class ViewDonorProfileFragment extends Fragment {

    ImageView img;
    private static final int Gallery_Code=1;
    Uri imageUrl=null;
    String Uname;
    String Ublood;
    String Uage;
    String Ugender;
    String Uhandphone;
    String Uemail;
    String Uaddress;
    String Uhos;
    String Uoccupation;
    String userprofimage;

    public ViewDonorProfileFragment() {

    }

    public ViewDonorProfileFragment(String Uname, String Ublood, String Uage ,String Ugender,String Uhandphone ,String Uemail,
                                   String Uaddress ,String Uhos, String Uoccupation, String userprofimage) {

        this.Uname = Uname;
        this.Ublood = Ublood;
        this.Uage = Uage;
        this.Ugender = Ugender;
        this.Uhandphone = Uhandphone;
        this.Uemail = Uemail;
        this.Uaddress = Uaddress;
        this.Uhos = Uhos;
        this.Uoccupation = Uoccupation;
        this.userprofimage = userprofimage;
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
         img = v.findViewById(R.id.staffviewdonorprofileimg);


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

        Picasso.get().load(userprofimage).into(img);


        // Navigate user interface
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment duser = new DonorDirectoryFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,duser).commit();


            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Code && resultCode == RESULT_OK)
        {
            imageUrl = data.getData();
            img.setImageURI(imageUrl);
        }


    }



}