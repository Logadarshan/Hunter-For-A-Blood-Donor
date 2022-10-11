package com.example.hbd;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.hbd.Home.AdminHome;
import com.example.hbd.Home.DonorHome;
import com.example.hbd.Home.StaffHome;
import com.example.hbd.Others.DeletedAccount;
import com.example.hbd.Others.preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;

public class Login extends AppCompatActivity {



    TextInputLayout memail;
    TextInputLayout mpassword;
    TextView forgotpass;
    Button loginbtn;
    TextView textsignup;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    // validate email
    private Boolean validateEmail(){
        String demail = memail.getEditText().getText().toString();

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
        String dpassword = mpassword.getEditText().getText().toString();

        if(dpassword.isEmpty()){
            mpassword.setError("Password field cannot be empty");
            return false;
        }
        else {
            mpassword.setError(null);
            return true;
        }
    }

    // Keep the user login
    @Override
    protected void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser() != null){
            if(preferences.getDataLogin(this)){
                if(preferences.getDataAs(this).equals("Donor")) {
                    startActivity(new Intent( Login.this, DonorHome.class));
                    finish();
                }else if(preferences.getDataAs(this).equals("Staff")){
                    startActivity(new Intent( Login.this, StaffHome.class));
                    finish();
                }else if(preferences.getDataAs(this).equals("Admin")){
                    startActivity(new Intent( Login.this, AdminHome.class));
                    finish();
                }else if(preferences.getDataAs(this).equals("Not")){
                    startActivity(new Intent( Login.this, DeletedAccount.class));
                    finish();
                }

            }

        }
        else {
            Toast.makeText(Login.this , "You can login!", Toast.LENGTH_SHORT).show();

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HwAds.init(this);

        BannerView bannerView = findViewById(R.id.hw_banner_view12);
        bannerView.setAdId("testw6vs28auh3");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);


        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        forgotpass = findViewById(R.id.forgotpass);
        loginbtn = findViewById(R.id.loginbtn);
        textsignup = findViewById(R.id.textsignup);

        fAuth = FirebaseAuth.getInstance();

        // Navigate to Signup Page
        textsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Signup.class));
            }
        });

        // login
        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String demail = memail.getEditText().getText().toString().trim();
                String dpassword = mpassword.getEditText().getText().toString().trim();

                if(!validateEmail()){
                    return;

                }
                if(!validatePassword()){
                    return;
                }

                fAuth.signInWithEmailAndPassword(demail,dpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // login based on the usertype
                            String uid = task.getResult().getUser().getUid();

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            firebaseDatabase.getReference().child("Users").child(uid).child("usertype").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                  String usertype = snapshot.getValue(String.class);

                                  if(usertype.equals("Donor")){

                                      preferences.setDataLogin(Login.this,true);
                                      preferences.setDataAs(Login.this, "Donor");

                                      Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                       startActivity(new Intent(getApplicationContext(),DonorHome.class));

                                  }

                                    if(usertype.equals("Admin")){

                                        preferences.setDataLogin(Login.this,true);
                                        preferences.setDataAs(Login.this, "Admin");

                                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), AdminHome.class));

                                    }

                                    if(usertype.equals("Staff")){

                                        preferences.setDataLogin(Login.this,true);
                                        preferences.setDataAs(Login.this, "Staff");

                                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), StaffHome.class));

                                    }


                                    if(usertype.equals("Not")){

                                        preferences.setDataLogin(Login.this,true);
                                        preferences.setDataAs(Login.this, "Not");

                                        Toast.makeText(Login.this, "Your Account Not Available", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), DeletedAccount.class));

                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }else{
                            Toast.makeText(Login.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


        // forget password
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);


                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });

    }
}