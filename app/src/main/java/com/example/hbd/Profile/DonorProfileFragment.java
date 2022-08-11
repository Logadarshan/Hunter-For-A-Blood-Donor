package com.example.hbd.Profile;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.MainActivity;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DonorProfileFragment extends Fragment {

    TextView ic,age,dob,gender,race,mstatus,occupation,phonenum,housenum,fax,
            address,postcode,state,password,repassword,blooooood,orgg;
    TextView name, email,user;
    ImageView editprofile, dimage;
    Activity context;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    private static final int Gallery_Code=1;
    Uri imageUrl=null;

    private String uname,uic,udob,uage,ugender,urace,umarriage,uoccupation,uemail,upassword,urepassword,
            uhomephone,uhandphone,ufax,ucaddress,ucpost,ucstate,usertype,ublood,uhos;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_donor_profile, container, false);

        context = getActivity();

        name = v.findViewById(R.id.donorname);
        email = v.findViewById(R.id.donormail);
        user =v.findViewById(R.id.usertypetext);
        ic = v.findViewById(R.id.donoric);
        blooooood = v.findViewById(R.id.donorblood);
        age = v.findViewById(R.id.donorage);
        dob = v.findViewById(R.id.donordob);
        gender = v.findViewById(R.id.donorgender);
        race = v.findViewById(R.id.donorethnicity);
        mstatus = v.findViewById(R.id.donorstatus);
        occupation = v.findViewById(R.id.donoroccupation);
        phonenum = v.findViewById(R.id.donorphoennum);
        housenum = v.findViewById(R.id.donorhousenum);
        orgg = v.findViewById(R.id.donororg);
        fax = v.findViewById(R.id.donorfax);
        address = v.findViewById(R.id.donoraddress);
        postcode = v.findViewById(R.id.donorpostcode);
        state = v.findViewById(R.id.donorstate);
        editprofile = v.findViewById(R.id.profileeditBtn);
        dimage = v.findViewById(R.id.dprofileimg);

        // Navigate to Update Profile Interface
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment updateprofile = new DonorUpdateProfileFragment();
                FragmentTransaction updatedonorprofile = getActivity().getSupportFragmentManager().beginTransaction();
                updatedonorprofile.replace(R.id.container,updateprofile).commit();
            }
        });

        // authenticate user
       fAuth = FirebaseAuth.getInstance();
       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Uri uri = firebaseUser.getPhotoUrl();

        // display user profile picture
        Picasso.get().load(uri).into(dimage);

        showUserProfile(firebaseUser);



        return v;
    }


    // pass profile picture details
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Code && resultCode == RESULT_OK)
        {
            imageUrl = data.getData();
            dimage.setImageURI(imageUrl);
        }


    }


    private  void showUserProfile(FirebaseUser firebaseUser){

        String userID = firebaseUser.getUid();

        // display user profile details
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){

                    uemail = userModel.getUemail();
                    uname = userModel.getUname();
                    usertype = userModel.getUsertype();
                    uic = userModel.getUic();
                    udob = userModel.getUdob();
                    uage = userModel.getUage();
                    ugender = userModel.getUgender();
                    urace = userModel.getUrace();
                    umarriage = userModel.getUmarriage();
                    uoccupation = userModel.getUoccupation();
                    uhandphone = userModel.getUhandphone();
                    uhomephone = userModel.getUhomephone();
                    ufax = userModel.getUfax();
                    ucaddress = userModel.getUcaddress();
                    ucpost = userModel.getUcpost();
                    ucstate = userModel.getUcstate();
                    ublood = userModel.getUblood();
                    uhos = userModel.getUhos();

                    name.setText(uname);
                    email.setText(uemail);
                    ic.setText(uic);
                    dob.setText(udob);
                    age.setText(uage);
                    gender.setText(ugender);
                    race.setText(urace);
                    mstatus.setText(umarriage);
                    occupation.setText(uoccupation);
                    housenum.setText(uhomephone);
                    phonenum.setText(uhandphone);
                    fax.setText(ufax);
                    address.setText(ucaddress);
                    postcode.setText(ucpost);
                    state.setText(ucstate);
                    blooooood.setText(ublood);
                    orgg.setText(uhos);
                    user.setText(usertype);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // logout from the system
    public void onStart(){
        super.onStart();
        ImageView logout = (ImageView) context.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}