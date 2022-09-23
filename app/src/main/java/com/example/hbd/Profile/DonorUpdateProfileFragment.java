package com.example.hbd.Profile;

import static android.app.Activity.RESULT_OK;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.ChangePasswordFragment;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.example.hbd.Signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DonorUpdateProfileFragment extends Fragment {

    TextInputLayout ic,age,dob,occupation,phonenum,housenum,fax,
            address,postcode,state,password,repassword,blooooood,orgg;
    TextView name, email,user1,changepass, gen, rac, sta;
    Button updatebtn;
    ZoomageView profilepic, image;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
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
    TextInputEditText dob1;


    String uname,uic,udob,uage,ugender,urace,umarriage,uoccupation,uemail,upassword,urepassword,
            uhomephone,uhandphone,ufax,ucaddress,ucpost,ucstate,usertype,ublood,uhos,utype,userimages,userid,ostate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_donor_update_profile, container, false);

        name = v.findViewById(R.id.donorname1);
        email = v.findViewById(R.id.donormail1);

        user1 = v.findViewById(R.id.usertypetext1);
        profilepic = v.findViewById(R.id.dprofileimg12);
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

        gen = v.findViewById(R.id.donorgender12);
        rac = v.findViewById(R.id.donorrace12);
        sta = v.findViewById(R.id.donormaritial12);
        dob1 = v.findViewById(R.id.dob1);

        // authenticate user
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");
        Uri uri = firebaseUser.getPhotoUrl();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // display user profile picture
        Picasso.get().load(uri).into(profilepic);


        showUserProfile(firebaseUser);




        dob1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob.getEditText().setText(dayOfMonth + "/" + (month+1) + "/" + year);


                    }
                },year,month,day);
                datePicker.show();

            }
        });


        // update user profile details
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(firebaseUser);
                uploadimagefile();
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

                                    DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseUser.getUid());

                                    Map<String,Object> user = new HashMap<>();
                                    user.put("userprofimage", task.getResult().toString());

                                    documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG,"User Profile Created" + firebaseUser.getUid());
                                        }
                                    });

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
        userid = firebaseUser.getUid();


        // Update in the database
        UserModel userModel = new UserModel(uname,uic,udob,uage,ugender,urace,umarriage,uoccupation,uemail,upassword,urepassword,
                uhomephone,uhandphone,ufax,ucaddress,ucpost,ucstate,usertype,ublood,uhos,userimages,userid,ostate);



        DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseUser.getUid());

        Map<String,Object> map = new HashMap<>();
        map.put("uname" , uname);
        map.put("uemail", uemail);
        map.put("uic" , uic);
        map.put("udob" , udob);
        map.put("uage" , uage);
        map.put("ugender", ugender);
        map.put("urace", urace);
        map.put("umarriage", umarriage);
        map.put("uoccupation", uoccupation);
        map.put("uhomephone", uhomephone);
        map.put("uhandphone", uhandphone);
        map.put("ufax", ufax);
        map.put("ucaddress", ucaddress);
        map.put("ucpost", ucpost);
        map.put("ucstate", ucstate);
        map.put("ublood", ublood);
        map.put("uhos", uhos);
        map.put("usertype", usertype);
        map.put("userid", userid);


        documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"User Profile Created" + firebaseUser.getUid());

                Fragment donorprofile = new DonorProfileFragment();
                FragmentTransaction dprofile = getActivity().getSupportFragmentManager().beginTransaction();
                dprofile.replace(R.id.container,donorprofile).commit();
                Toast.makeText(getActivity(), "Successfully updated", Toast.LENGTH_SHORT).show();


            }
        });


    }


    // display user profile details
    private  void showUserProfile(FirebaseUser firebaseUser){



        String userID = firebaseUser.getUid();


        DocumentReference documentReference = firebaseFirestore.collection("User").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {




                name.setText(value.getString("uname"));
                email.setText(value.getString("uemail"));
                ic.getEditText().setText(value.getString("uic"));
                dob.getEditText().setText(value.getString("udob"));
                age.getEditText().setText(value.getString("uage"));
                occupation.getEditText().setText(value.getString("uoccupation"));
                housenum.getEditText().setText(value.getString("uhomephone"));
                phonenum.getEditText().setText(value.getString("uhandphone"));
                fax.getEditText().setText(value.getString("ufax"));
                address.getEditText().setText(value.getString("ucaddress"));
                postcode.getEditText().setText(value.getString("ucpost"));
                state.getEditText().setText(value.getString("ucstate"));
                blooooood.getEditText().setText(value.getString("ublood"));
                orgg.getEditText().setText(value.getString("uhos"));
                user1.setText(value.getString("usertype"));

                gen.setText(value.getString("ugender"));
                rac.setText(value.getString("urace"));
                sta.setText(value.getString("umarriage"));


                String gen1 = gen.getText().toString();
                String rac1 = rac.getText().toString();
                String sta1 = sta.getText().toString();


                if(gen1.contains("Male")){
                    radioButtonUpdateGender = v.findViewById(R.id.radio_male1);
                }else {
                    radioButtonUpdateGender = v.findViewById(R.id.radio_female1);
                }
                radioButtonUpdateGender.setChecked(true);


                if(rac1.contains("Malay")){
                    radioButtonUpdateEthnicity = v.findViewById(R.id.radio_malay1);
                }else if(rac1.contains("Chinese")){
                    radioButtonUpdateEthnicity = v.findViewById(R.id.radio_chinese1);
                }else if(rac1.contains("Indian")){
                    radioButtonUpdateEthnicity = v.findViewById(R.id.radio_indian1);
                }else {
                    radioButtonUpdateEthnicity = v.findViewById(R.id.radio_others1);
                }
                radioButtonUpdateEthnicity.setChecked(true);

                if(sta1.contains("Single")){
                    radioButtonUpdateMarriage = v.findViewById(R.id.radio_single1);
                }else if(sta1.contains("Married")){
                    radioButtonUpdateMarriage = v.findViewById(R.id.radio_married1);
                }else{
                    radioButtonUpdateMarriage = v.findViewById(R.id.radio_divorceorwidow1);
                }
                radioButtonUpdateMarriage.setChecked(true);


            }
        });



    }

}