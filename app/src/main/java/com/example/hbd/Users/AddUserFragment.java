package com.example.hbd.Users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddUserFragment extends Fragment {


    EditText adminstaffname,adminstaffemail,adminstaffpassword,adminstafforg;
    Button addadminstaff;
    RadioGroup radioGroupUser;
    RadioButton radioButtonUser;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore fStore;
    View V;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.fragment_add_user, container, false);

        adminstaffname = V.findViewById(R.id.uusername);
        adminstaffemail = V.findViewById(R.id.useremail);
        adminstaffpassword = V.findViewById(R.id.userpassword);
        adminstafforg = V.findViewById(R.id.userhos);
        addadminstaff = V.findViewById(R.id.adduserdataBtn);
        radioGroupUser = V.findViewById(R.id.user);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // add admin and staff details
        addadminstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase = FirebaseDatabase.getInstance();
                int selectedUserId = radioGroupUser.getCheckedRadioButtonId();
                radioButtonUser = V.findViewById(selectedUserId);

                String uname = adminstaffname.getText().toString();
                String uhos = adminstafforg.getText().toString();
                String uemail = adminstaffemail.getText().toString();
                String upassword = adminstaffpassword.getText().toString();
                String urepassword = "-";
                String uic = "-";
                String udob = "-";
                String uage = "-";
                String uhomephone = "-";
                String uhandphone = "-";
                String ufax = "-";
                String ucaddress = "-";
                String ucpost = "-";
                String ucstate = "-";
                String ugender = "-";
                String urace = "-";
                String umarriage = "-";
                String ublood = "-";
                String uoccupation = "-";
                String usertype = radioButtonUser.getText().toString();
                String userimages = "/";


                // staff details insert into database
                addstaffdata(uname,uic,udob,uage,ugender,urace,umarriage,uoccupation,uemail,upassword,urepassword,
                        uhomephone,uhandphone,ufax,ucaddress,ucpost,ucstate,usertype,ublood,uhos,userimages);


            }
        });



        return V;
    }

    // pass staff and admin details
    private void addstaffdata(String uname, String uic, String udob, String uage, String ugender, String urace,
                              String umarriage, String uoccupation, String uemail, String upassword, String urepassword,
                              String uhomephone, String uhandphone, String ufax, String ucaddress, String ucpost, String ucstate,
                              String usertype, String ublood, String uhos, String userimages) {


        FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // if task is successful
                if(task.isSuccessful()){
                    UserModel userModel = new UserModel(uname,uic,udob,uage,ugender,urace,umarriage,uoccupation,uemail,upassword,urepassword,
                            uhomephone,uhandphone,ufax,ucaddress,ucpost,ucstate,usertype,ublood,uhos,userimages);

                    firebaseUser = fAuth.getCurrentUser();
                    String userID = firebaseUser.getUid();

                    databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(userID).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            firebaseUser.sendEmailVerification();

                            // naviagte to user interface
                            Fragment user = new UserFragment();
                            FragmentTransaction fragmentTransactiont = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransactiont.replace(R.id.container,user).commit();
                            fAuth.signOut();
                            fAuth.signInAnonymously();
                        }
                    });
                }
            }
        });

    }
}