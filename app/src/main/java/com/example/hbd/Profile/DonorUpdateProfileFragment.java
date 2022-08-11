package com.example.hbd.Profile;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.ChangePasswordFragment;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DonorUpdateProfileFragment extends Fragment {

    TextInputLayout ic,age,dob,occupation,phonenum,housenum,fax,
            address,postcode,state,password,repassword,blooooood,orgg;
    TextView name, email,user1,changepass;
    Button updatebtn;
    ImageView profilepic, image;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    ProgressDialog progressDialog;
    private static final int Gallery_Code=1;
    Uri imageUrl=null;
    DatePickerDialog datePicker;
    RadioGroup radioUpdateGroupGender,radioUpdateGroupEthnicity, radioUpdateGroupMarriage;
    RadioButton radioButtonUpdateGender,radioButtonUpdateEthnicity, radioButtonUpdateMarriage;
    View v;


    String uname,uic,udob,uage,ugender,urace,umarriage,uoccupation,uemail,upassword,urepassword,
            uhomephone,uhandphone,ufax,ucaddress,ucpost,ucstate,usertype,ublood,uhos,utype,userimages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_donor_update_profile, container, false);

        name = v.findViewById(R.id.donorname1);
        email = v.findViewById(R.id.donormail1);

        user1 = v.findViewById(R.id.usertypetext1);
        profilepic = v.findViewById(R.id.dprofileimg);
        ic = v.findViewById(R.id.donoric1);
        blooooood = v.findViewById(R.id.donorblood1);
        age = v.findViewById(R.id.donorage1);
        dob = v.findViewById(R.id.donordob1);
        radioUpdateGroupGender = v.findViewById(R.id.donorgender1);
        radioUpdateGroupEthnicity = v.findViewById(R.id.donorethnicity1);
        radioUpdateGroupMarriage = v.findViewById(R.id.donorstatus1);
        occupation = v.findViewById(R.id.donoroccupation1);
        phonenum = v.findViewById(R.id.donorphoennum1);
        housenum = v.findViewById(R.id.donorhousenum1);
        orgg = v.findViewById(R.id.donororg1);
        fax = v.findViewById(R.id.donorfax1);
        address = v.findViewById(R.id.donoraddress1);
        postcode = v.findViewById(R.id.donorpostcode1);
        state = v.findViewById(R.id.donorstate1);
        updatebtn = v.findViewById(R.id.updateprofileBtn);

        changepass = v.findViewById(R.id.changepassword);

        // authenticate user
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");
        Uri uri = firebaseUser.getPhotoUrl();

        // display user profile picture
        Picasso.get().load(uri).into(profilepic);


        showUserProfile(firebaseUser);


        // update user profile details
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(firebaseUser);
            }
        });

        // profile picture details
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               openimagefile();
            }
        });

        // navigate to change password page
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment change = new ChangePasswordFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,change).commit();
            }
        });

       return v;
    }

    // open image in the gallery
    private void openimagefile() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/url*");
        startActivityForResult(intent,Gallery_Code);

    }

    // upload image in the gallery
    private void uploadimagefile(){

        if(imageUrl != null){

            // Upload image in firebase storage
            StorageReference filepath = storageReference.child(fAuth.getCurrentUser().getUid() + "."
                    + getFileExtension(imageUrl));

            // Upload image in realtime database
            filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Uri downloadUri = uri;
                            firebaseUser = fAuth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    String t = task.getResult().toString();


                                    String uid1 = firebaseUser.getUid();

                                    databaseReference.child(uid1).child("userprofimage").setValue(task.getResult().toString());

                                }
                            });
                        }
                    });
                }
            });
        }
    }


    // select image from the gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Code && resultCode == RESULT_OK)
        {
            imageUrl = data.getData();
            profilepic.setImageURI(imageUrl);
        }


    }

    // extension file for image
    private String getFileExtension(Uri imageUrl) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUrl));

    }


    // Update user profile details
    private void updateUserProfile(FirebaseUser firebaseUser) {

        // display data
        // edit data
        int selectedGenderId = radioUpdateGroupGender.getCheckedRadioButtonId();
        radioButtonUpdateGender = v.findViewById(selectedGenderId);

        int selectedEthnicity = radioUpdateGroupEthnicity.getCheckedRadioButtonId();
        radioButtonUpdateEthnicity = v.findViewById(selectedEthnicity);
        int selectedMarriageId = radioUpdateGroupMarriage.getCheckedRadioButtonId();
        radioButtonUpdateMarriage = v.findViewById(selectedMarriageId);

        uname = name.getText().toString();
        usertype = user1.getText().toString();
        uic = ic.getEditText().getText().toString();
        ublood = blooooood.getEditText().getText().toString();
        udob = dob.getEditText().getText().toString();
        uage = age.getEditText().getText().toString();
        ugender = radioButtonUpdateGender.getText().toString();
        urace = radioButtonUpdateEthnicity.getText().toString();
        umarriage = radioButtonUpdateMarriage.getText().toString();
        uoccupation = occupation.getEditText().getText().toString();
        uhomephone = housenum.getEditText().getText().toString();
        uhandphone = phonenum.getEditText().getText().toString();
        uhos = orgg.getEditText().getText().toString();
        ufax = fax.getEditText().getText().toString();
        ucaddress = address.getEditText().getText().toString();
        ucpost = postcode.getEditText().getText().toString();
        ucstate = state.getEditText().getText().toString();
        uemail = email.getText().toString();
        upassword = "";
        urepassword = "";



        // Update in the database
        UserModel userModel = new UserModel(uname,uic,udob,uage,ugender,urace,umarriage,uoccupation,uemail,upassword,urepassword,
                uhomephone,uhandphone,ufax,ucaddress,ucpost,ucstate,usertype,ublood,uhos,userimages);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        String userID = firebaseUser.getUid();

        databaseReference.child(userID).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    uploadimagefile();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(uname).build();
                    firebaseUser.updateProfile(profileChangeRequest);


                    // Navigate to View Profile Page
                    Fragment donorprofile = new DonorProfileFragment();
                    FragmentTransaction dprofile = getActivity().getSupportFragmentManager().beginTransaction();
                    dprofile.replace(R.id.container,donorprofile).commit();


                }

            }
        });

    }


    // display user profile details
    private  void showUserProfile(FirebaseUser firebaseUser){

        String userID = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){

                    uemail = userModel.getUemail();
                    uname = userModel.getUname();
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
                    usertype = userModel.getUsertype();



                    name.setText(uname);
                    email.setText(uemail);
                    ic.getEditText().setText(uic);
                    dob.getEditText().setText(udob);
                    age.getEditText().setText(uage);

                    occupation.getEditText().setText(uoccupation);
                    housenum.getEditText().setText(uhomephone);
                    phonenum.getEditText().setText(uhandphone);
                    fax.getEditText().setText(ufax);
                    address.getEditText().setText(ucaddress);
                    postcode.getEditText().setText(ucpost);
                    state.getEditText().setText(ucstate);
                    blooooood.getEditText().setText(ublood);
                    orgg.getEditText().setText(uhos);
                    user1.setText(usertype);

                    if(ugender.equals("Male")){
                        radioButtonUpdateGender = v.findViewById(R.id.radio_male1);
                    }else {
                        radioButtonUpdateGender = v.findViewById(R.id.radio_female1);
                    }
                    radioButtonUpdateGender.setChecked(true);

                    if(urace.equals("Malay")){
                        radioButtonUpdateEthnicity = v.findViewById(R.id.radio_malay1);
                    }else if(urace.equals("Chinese")){
                        radioButtonUpdateEthnicity = v.findViewById(R.id.radio_chinese1);
                    }else if(urace.equals("Indian")){
                        radioButtonUpdateEthnicity = v.findViewById(R.id.radio_indian1);
                    }else {
                        radioButtonUpdateEthnicity = v.findViewById(R.id.radio_others1);
                    }
                    radioButtonUpdateEthnicity.setChecked(true);

                    if(umarriage.equals("Single")){
                        radioButtonUpdateMarriage = v.findViewById(R.id.radio_single1);
                    }else if(umarriage.equals("Married")){
                        radioButtonUpdateMarriage = v.findViewById(R.id.radio_married1);
                    }else{
                        radioButtonUpdateMarriage = v.findViewById(R.id.radio_divorceorwidow1);
                    }
                    radioButtonUpdateMarriage.setChecked(true);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}