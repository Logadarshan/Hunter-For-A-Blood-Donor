package com.example.hbd;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hbd.Home.DonorHome;
import com.example.hbd.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.regex.Pattern;


public class Signup extends AppCompatActivity {


    EditText name,ic,dob,age,occupation, memail,mpassword,
            repassword,homephone,handphone,officephone,fax,caddress,cstate,cpost,bloood,organization;
    RadioGroup radioGroupGender,radioGroupEthnicity, radioGroupMarriage;
    RadioButton radioButtonGender, radioButtonEthnicity, radioButtonMarriage;
    Button signupBtn;


    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore fStore;
    DatePickerDialog datePicker;


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile( "^" +
                    "(?= * [0-9])" +   // atleast 1 digit
                    "(?= * [A-Z])" +    // atleast 1 lowercase letter
                    "(?= * [a-z])" +    // atleast 1 uppercase letter
                    "(?= * [@#$%^&+=])" + // atleast 1 special character
                    "(?=\\S+$)" +          // no white spaces
                    ".{6,}" +             // at least 6 characters
                    "$"
            );

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@(.+)$"
            );


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
        cstate = findViewById(R.id.cstate);
        cpost = findViewById(R.id.cpost);
        bloood = findViewById(R.id.bloodgroup);
        organization = findViewById(R.id.hosorg);


        signupBtn = findViewById(R.id.signupBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),DonorHome.class));
            finish();
        }

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(Signup.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dob.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                },year,month,day);
                datePicker.show();

            }
        });



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase = FirebaseDatabase.getInstance();


                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                radioButtonGender = findViewById(selectedGenderId);
                int selectedEthnicity = radioGroupEthnicity.getCheckedRadioButtonId();
                radioButtonEthnicity = findViewById(selectedEthnicity);
                int selectedMarriageId = radioGroupMarriage.getCheckedRadioButtonId();
                radioButtonMarriage = findViewById(selectedMarriageId);

                String uname = name.getText().toString();
                String uic = ic.getText().toString();

                String udob = dob.getText().toString();
                String uage = age.getText().toString();
                String uoccupation = occupation.getText().toString();
                String urepassword = repassword.getText().toString();
                String uhomephone = homephone.getText().toString();
                String uhandphone = handphone.getText().toString();
                String ufax = fax.getText().toString();
                String ucaddress = caddress.getText().toString();
                String ucpost = cpost.getText().toString();
                String ucstate = cstate.getText().toString();
                String uemail = memail.getText().toString();
                String upassword = mpassword.getText().toString();
                String ublood = bloood.getText().toString();
                String uhos = organization.getText().toString();
                String ugender = radioButtonGender.getText().toString();
                String urace = radioButtonEthnicity.getText().toString();
                String umarriage = radioButtonMarriage.getText().toString();

                String usertype = "Donor";
                String userimages = "/";



                userregister(uname,uic,udob,uage,ugender,urace,umarriage,uoccupation,uemail,upassword,urepassword,
                        uhomephone,uhandphone,ufax,ucaddress,ucpost,ucstate,usertype,ublood,uhos,userimages);

               /* if( ! validatedata()){
                    return;

                }
                else{



                } */


            }
        });


    }

    // pass donor details
    private void userregister(String uname, String uic, String udob, String uage, String ugender, String urace,
                              String umarriage, String uoccupation, String uemail, String upassword, String urepassword,
                              String uhomephone, String uhandphone, String ufax, String ucaddress, String ucpost, String ucstate,
                              String usertype, String ublood, String uhos,String userimages) {

        FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(Signup.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // if task is successful
                        if(task.isSuccessful()){

                            // insert user details into database
                            UserModel userModel = new UserModel(uname,uic,udob,uage,ugender,urace,umarriage,uoccupation,uemail,upassword,urepassword,
                                    uhomephone,uhandphone,ufax,ucaddress,ucpost,ucstate,usertype,ublood,uhos,userimages);
                            firebaseUser = fAuth.getCurrentUser();

                            databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.child(firebaseUser.getUid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseUser.sendEmailVerification();
                                    Toast.makeText(Signup.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Signup.this, DonorHome.class));
                                }
                            });
                        }
                        else {
                            Toast.makeText(Signup.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // validate data
    private boolean validatedata() {
        String dname = name.getText().toString();
        String dic = ic.getText().toString();
        String ddob = dob.getText().toString();
        String dage = age.getText().toString();
        String dgender  ;
        String race ;
        String dmarriage ;
        String doccupation = occupation.getText().toString();
        String dhomephone = homephone.getText().toString();
        String dhandphone = handphone.getText().toString();
        String dofficephone = officephone.getText().toString();
        String dfax = fax.getText().toString();
        String dcaddress = caddress.getText().toString();
        String dcpost = cpost.getText().toString();
        String dcstate = cstate.getText().toString();

        if(dname.isEmpty() | ddob.isEmpty() | dic.isEmpty() | dage.isEmpty() |
           doccupation.isEmpty() |  dhomephone.isEmpty() | dhandphone.isEmpty() | dofficephone.isEmpty() |
           dfax.isEmpty() | dcaddress.isEmpty() | dcpost.isEmpty() | dcstate.isEmpty()){
            name.setError("Fields cannot be empty");
            ic.setError("Fields cannot be empty");
            dob.setError("Fields cannot be empty");
            age.setError("Fields cannot be empty");
            occupation.setError("Fields cannot be empty");
            homephone.setError("Fields cannot be empty");
            handphone.setError("Fields cannot be empty");
            officephone.setError("Fields cannot be empty");
            fax.setError("Fields cannot be empty");
            caddress.setError("Fields cannot be empty");
            cpost.setError("Fields cannot be empty");
            cstate.setError("Fields cannot be empty");

            return false;

        }
        else {
            name.setError(null);
            return true;
        }
    }

    // valid password
    private boolean validpassword() {

        String dpassword = mpassword.getText().toString();
        String drepassword = repassword.getText().toString();
        String passcriteria = "^" +
                "(?= * [0-9])" +   // atleast 1 digit
                "(?= * [A-Z])" +    // atleast 1 lowercase letter
                "(?= * [a-z])" +    // atleast 1 uppercase letter
                "(?= * [@#$%^&+=])" + // atleast 1 special character
                "(?=\\S+$)" +          // no white spaces
                ".{6,}" +             // at least 6 characters
                "$" ;

        if(dpassword.isEmpty())
        {
            mpassword.setError("Field is empty!");
            return false;
        }
        else if(dpassword.equals(drepassword)){
            repassword.setError("Passwords not matching");
            return false;
        }
        else if(!dpassword.matches(passcriteria))
        {
            mpassword.setError("Password too weak!Password need to contain Capital letter, small letter, numbers and special characters! Password should be no spaces");
            return false;
        }

        return false;
    }

    // valid email
    private boolean validmail() {

        String demail = memail.getText().toString();

        if(demail.isEmpty())
        {
            memail.setError("Field is empty!");
        }
        if(!EMAIL_PATTERN.matcher(demail).matches()){
            memail.setError("Email is not valid");
        }

        return false;
    }


}