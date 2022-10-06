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


public class ViewUserProfileFragment extends Fragment {

    ImageView img1;
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
    String userprofimage;

    public ViewUserProfileFragment() {

    }

    public ViewUserProfileFragment(String Uname, String Ublood, String Uage ,String Ugender,String Uhandphone ,String Uemail,
            String Uaddress ,String Uhos,  String userprofimage) {

        this.Uname = Uname;
        this.Ublood = Ublood;
        this.Uage = Uage;
        this.Ugender = Ugender;
        this.Uhandphone = Uhandphone;
        this.Uemail = Uemail;
        this.Uaddress = Uaddress;
        this.Uhos = Uhos;
        this.userprofimage = userprofimage;
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
        img1 = v.findViewById(R.id.adminviewuserprofileimg);





        nameuser.setText(Uname);
        blooduser.setText(Ublood);
        ageuser.setText(Uage);
        genderuser.setText(Ugender);
        phoneuser.setText(Uhandphone);
        emailuser.setText(Uemail);
        adduser.setText(Uaddress);
        hosuser.setText(Uhos);
        Picasso.get().load(userprofimage).into(img1);



        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Code && resultCode == RESULT_OK)
        {
            imageUrl = data.getData();
            img1.setImageURI(imageUrl);
        }


    }



}