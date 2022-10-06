package com.example.hbd.Users;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hbd.Login;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddUserFragment extends Fragment {


    EditText adminstaffname,adminstaffemail,adminstaffpassword,adminstafforg,adminstaffostate;
    Button addadminstaff;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth,fAuth1;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    View V;

    Unbinder unbinder;
    String adduser;
    Dialog dialog;
    DatabaseReference databaseReference2;
    String userID;

    String  uhos,uemail, upassword, ostate;

    String uhos1, ostate1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.fragment_add_user, container, false);
        unbinder = ButterKnife.bind(this,V);

        adminstaffname = V.findViewById(R.id.uusername);
        adminstaffemail = V.findViewById(R.id.useremail);
        adminstaffpassword = V.findViewById(R.id.userpassword);
        adminstaffostate = V.findViewById(R.id.userostate12);
        adminstafforg = V.findViewById(R.id.useruhos12);
        addadminstaff = V.findViewById(R.id.adduserdataBtn);
        dialog = new Dialog(this.getContext());
        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userID = firebaseUser.getUid();
        setCity();
        // add admin and staff details
        addadminstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkEmail();
                 uemail = adminstaffemail.getText().toString();
                 upassword = adminstaffpassword.getText().toString();
                 uhos1 = adminstafforg.getText().toString();
                 ostate1 =adminstaffostate.getText().toString();


                adddetails(uemail,upassword,uhos1,ostate1);

            }
        });



        return V;
    }

    private void setCity() {

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos = userModel.getUhos();
                    ostate = userModel.getOstate();

                    adminstaffostate.setText(ostate);
                    adminstafforg.setText(uhos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void adddetails(String uemail, String upassword, String uhos1, String ostate1) {

        FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    firebaseUser = fAuth.getCurrentUser();
                    adduser = firebaseUser.getUid();
                    firebaseDatabase = FirebaseDatabase.getInstance();


                    Map<String,Object> user = new HashMap<>();
                    user.put("ostate",ostate1);
                    user.put("usertype","Admin");
                    user.put("uname",adminstaffname.getText().toString());
                    user.put("uhos",uhos1);
                    user.put("uemail",uemail);
                    user.put("upassword",upassword);
                    user.put("userid",adduser);

                    user.put("uage","-");
                    user.put("ublood","-");
                    user.put("ucaddress","-");
                    user.put("ucpost","-");
                    user.put("ucstate","-");
                    user.put("udob","-");
                    user.put("ufax","-");
                    user.put("ugender","-");
                    user.put("uhandphone","-");
                    user.put("uhomephone","-");
                    user.put("uic","-");
                    user.put("umarriage","-");
                    user.put("uoccupation","-");
                    user.put("urace","-");
                    user.put("urepassword","-");
                    user.put("userprofimage","-");


                    databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(adduser).setValue(user);

                    DocumentReference documentReference = firebaseFirestore.collection("User").document(adduser);
                    documentReference.set(user);

                    dialog.setContentView(R.layout.verify);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button btncon1 = dialog.findViewById(R.id.continueappBtn1);

                    // Navigate to selftest Page
                    btncon1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fAuth.signOut();
                            startActivity(new Intent(getContext(), Login.class));
                            dialog.dismiss();

                        }
                    });

                    dialog.show();




                }








            }
        });



    }

    private void checkEmail(){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.fetchSignInMethodsForEmail(adminstaffemail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            Log.e("TAG", "Is New User!");
                        } else {
                            adminstaffemail.setError("User already exists");
                        }

                    }
                });
    }



}