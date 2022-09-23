package com.example.hbd;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hbd.Home.DonorHome;
import com.example.hbd.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    EditText name,ic,dob,age,occupation, memail,mpassword,
            repassword,homephone,handphone,officephone,fax,caddress,cstate,cpost,bloood,organization;
    RadioGroup radioGroupGender,radioGroupEthnicity, radioGroupMarriage;
    RadioButton radioButtonGender, radioButtonEthnicity, radioButtonMarriage;
    Button signupBtn;

    String uname, uic,udob, uage, uoccupation, urepassword, uhomephone, uhandphone,ufax, ucaddress,ucpost;
    String ucstate, uemail, upassword, ublood, uhos, ugender, urace, umarriage, usertype, userimages, ostate,userid;
    String uid;


    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore fStore;
    DatePickerDialog datePicker;
    Spinner hosspinner,statespinner,bloodspinner;

    // validate email
    private Boolean validateEmail(){
        String demail = memail.getText().toString();

        if(demail.isEmpty()){
            memail.setError("Email field cannot be empty");
            return false;
        }
        else {
            memail.setError(null);
            return true;
        }
    }

    // validate password
    private Boolean validatePassword(){
        String dpassword = mpassword.getText().toString();
        String drpassword = repassword.getText().toString();
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$).{4,}$";

        Pattern pattern= Pattern.compile(PASSWORD_PATTERN);


        if(dpassword.isEmpty()){
            mpassword.setError("Password field cannot be empty");
            return false;
        }else if (!pattern.matcher(dpassword).matches()){
            mpassword.setError("Password not valid.Password should consists capital,small letters, number and special characters");
            return false;
        }
        else if(drpassword.isEmpty()){
            repassword.setError("Password field cannot be empty");
            return false;
        } else if(!dpassword.matches(drpassword)){
            repassword.setError("Passwords not matching");
            return false;
        }
        else {
            mpassword.setError(null);
            return true;
        }
    }


    // validate data
    private boolean validdatas() {
        String dname = name.getText().toString();
        String dic = ic.getText().toString();
        String doccupation = occupation.getText().toString();
        String dhomephone = homephone.getText().toString();
        String dhandphone = handphone.getText().toString();
        String dofficephone = officephone.getText().toString();
        String dfax = fax.getText().toString();
        String dcaddress = caddress.getText().toString();
        String dcpost = cpost.getText().toString();

        if(dname.isEmpty()  | dic.isEmpty() |
                doccupation.isEmpty() |  dhomephone.isEmpty() | dhandphone.isEmpty() | dofficephone.isEmpty() |
                dfax.isEmpty() | dcaddress.isEmpty() | dcpost.isEmpty()  ){
            name.setError("Fields cannot be empty");
            ic.setError("Fields cannot be empty");
            occupation.setError("Fields cannot be empty");
            homephone.setError("Fields cannot be empty");
            handphone.setError("Fields cannot be empty");
            officephone.setError("Fields cannot be empty");
            fax.setError("Fields cannot be empty");
            caddress.setError("Fields cannot be empty");
            cpost.setError("Fields cannot be empty");

            return false;

        }
        else {
            name.setError(null);
            return true;
        }
    }

    private boolean validage(){


        String dage = age.getText().toString();
        Integer age1 = Integer.parseInt(dage);

        if (Integer.valueOf(dage) < 18){
            Toast.makeText(Signup.this, "Sorry You should 18 and above to register this account", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            age.setError(null);
            return true;
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        name = findViewById(R.id.name);
        ic = findViewById(R.id.ic);
        dob = findViewById(R.id.dob);
        age = findViewById(R.id.age);
        radioGroupGender = findViewById(R.id.gender);
        radioGroupGender.clearCheck();
        radioGroupEthnicity = findViewById(R.id.ethnicity);
        radioGroupEthnicity.clearCheck();
        radioGroupMarriage = findViewById(R.id.marriage);
        radioGroupMarriage.clearCheck();
        occupation = findViewById(R.id.occupation);
        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        homephone = findViewById(R.id.homephone);
        handphone = findViewById(R.id.handphone);
        officephone = findViewById(R.id.officephone);
        fax = findViewById(R.id.fax);
        caddress = findViewById(R.id.caddress);

        cpost = findViewById(R.id.cpost);

        organization = findViewById(R.id.hosorg);
        hosspinner = findViewById(R.id.hosspinner);
        statespinner = findViewById(R.id.statespinner);
        bloodspinner = findViewById(R.id.bloodspinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.hospital,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        hosspinner.setAdapter(adapter);
        hosspinner.setOnItemSelectedListener(this);


        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(
                this,
                R.array.state,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        statespinner.setAdapter(adapter1);
        statespinner.setOnItemSelectedListener(this);


        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.blood,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        bloodspinner.setAdapter(adapter2);
        bloodspinner.setOnItemSelectedListener(this);



        age.setEnabled(false);

        signupBtn = findViewById(R.id.signupBtn);

        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        fStore = FirebaseFirestore.getInstance();


        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),DonorHome.class));
            finish();
        }

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String simpleDateFormat2 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                Integer ageyear = Integer.valueOf(simpleDateFormat2);
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(Signup.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob.setText(dayOfMonth + "/" + (month+1) + "/" + year);

                        String realage1 = String.valueOf(ageyear-year);

                        age.setText(realage1);
                    }
                },year,month,day);
                datePicker.show();

            }
        });



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!validdatas()){
                    return;
                }
                if(!validage()){
                    return;
                }
                if(!validateEmail()){
                    return;

                }
                if(!validatePassword()){
                    return;
                }

                checkEmail();


                donorregister();


            }
        });


    }

    private void donorregister() {


        firebaseDatabase = FirebaseDatabase.getInstance();


        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        radioButtonGender = findViewById(selectedGenderId);
        int selectedEthnicity = radioGroupEthnicity.getCheckedRadioButtonId();
        radioButtonEthnicity = findViewById(selectedEthnicity);
        int selectedMarriageId = radioGroupMarriage.getCheckedRadioButtonId();
        radioButtonMarriage = findViewById(selectedMarriageId);

        uname = name.getText().toString();
        uic = ic.getText().toString();

        udob = dob.getText().toString();
        uage = age.getText().toString();
        uoccupation = occupation.getText().toString();
        urepassword = repassword.getText().toString();
        uhomephone = homephone.getText().toString();
        uhandphone = handphone.getText().toString();
        ufax = fax.getText().toString();
        ucaddress = caddress.getText().toString();
        ucpost = cpost.getText().toString();
        uemail = memail.getText().toString();
        upassword = mpassword.getText().toString();
        ugender = radioButtonGender.getText().toString();
        urace = radioButtonEthnicity.getText().toString();
        umarriage = radioButtonMarriage.getText().toString();
        uhos = hosspinner.getSelectedItem().toString();
        ucstate = statespinner.getSelectedItem().toString();
        ublood = bloodspinner.getSelectedItem().toString();

        FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(Signup.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            firebaseUser = fAuth.getCurrentUser();
                            uid = firebaseUser.getUid();

                            Map<String,Object> user = new HashMap<>();
                            user.put("ostate","-");
                            user.put("uage",uage);
                            user.put("ublood",ublood);
                            user.put("ucaddress",ucaddress);
                            user.put("ucpost",ucpost);
                            user.put("ucstate",ucstate);
                            user.put("udob",udob);
                            user.put("uemail",uemail);
                            user.put("ufax",ufax);
                            user.put("ugender",ugender);
                            user.put("uhandphone",uhandphone);
                            user.put("uhomephone",uhomephone);
                            user.put("uhos",uhos);
                            user.put("uic",uic);
                            user.put("umarriage",umarriage);
                            user.put("uname",uname);
                            user.put("uoccupation",uoccupation);
                            user.put("upassword",upassword);
                            user.put("urace",urace);
                            user.put("urepassword",urepassword);
                            user.put("userid",uid);
                            user.put("userprofimage","-");
                            user.put("usertype","Donor");

                            DocumentReference documentReference = firebaseFirestore.collection("User").document(uid);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"User Profile Created" + uid);
                                    startActivity(new Intent(Signup.this, DonorHome.class));
                                }
                            });

                            Map<String,Object> map = new HashMap<>();
                            map.put("resultans" , "-");
                            map.put("usertestid", uid);



                            DocumentReference documentReference1 = firebaseFirestore.collection("User").document(uid)
                                    .collection("Test").document(uid);
                            documentReference1.set(map);


                            databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.child(uid).setValue(user);



                        }
                    }
                });



    }


    private void checkEmail(){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



        firebaseAuth.fetchSignInMethodsForEmail(memail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            Log.e("TAG", "Is New User!");
                        } else {
                            memail.setError("User already exists");
                            Toast.makeText(Signup.this, "The User already exists", Toast.LENGTH_SHORT).show();
                        }

                    }
                });




    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}