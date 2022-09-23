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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class DonorProfileFragment extends Fragment {

    TextView ic,age,dob,gender,race,mstatus,occupation,phonenum,housenum,fax,
            address,postcode,state,password,repassword,blooooood,orgg;
    TextView name, email,user,imagcap;
    ImageView editprofile;
    ZoomageView dimage;
    Activity context;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
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


        firebaseFirestore = FirebaseFirestore.getInstance();

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


        DocumentReference documentReference = firebaseFirestore.collection("User").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {






                name.setText(value.getString("uname"));
                email.setText(value.getString("uemail"));
                ic.setText(value.getString("uic"));
                dob.setText(value.getString("udob"));
                age.setText(value.getString("uage"));
                gender.setText(value.getString("ugender"));
                race.setText(value.getString("urace"));
                mstatus.setText(value.getString("umarriage"));
                occupation.setText(value.getString("uoccupation"));
                housenum.setText(value.getString("uhomephone"));
                phonenum.setText(value.getString("uhandphone"));
                fax.setText(value.getString("ufax"));
                address.setText(value.getString("ucaddress"));
                postcode.setText(value.getString("ucpost"));
                state.setText(value.getString("ucstate"));
                blooooood.setText(value.getString("ublood"));
                orgg.setText(value.getString("uhos"));
                user.setText(value.getString("usertype"));
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