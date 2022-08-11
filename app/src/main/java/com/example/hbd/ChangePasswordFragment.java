package com.example.hbd;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.Profile.DonorProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePasswordFragment extends Fragment {



    View v;
    TextInputLayout oldpass,newpass,renewpass;
    Button authBtn,updatepassBtn;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;
    String userPwdCurr;
    int counter =0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_change_password, container, false);

        oldpass = v.findViewById(R.id.confirmoldpass);
        newpass = v.findViewById(R.id.confirmnewpass);
        renewpass = v.findViewById(R.id.reconfirmnewpass);
        authBtn = v.findViewById(R.id.authenticateBtn);
        updatepassBtn = v.findViewById(R.id.updatepasswordBtn);


        newpass.setEnabled(false);
        renewpass.setEnabled(false);
        updatepassBtn.setEnabled(false);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();

       reAuthenticateUser(firebaseUser);

        return v;
    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {


        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwdCurr = oldpass.getEditText().getText().toString();

                if(TextUtils.isEmpty(userPwdCurr)){
                    oldpass.setError("Please enter your current password to authenticate");
                    oldpass.requestFocus();
                }else {
                    Toast.makeText(getActivity(), "Now can update new password", Toast.LENGTH_SHORT).show();
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPwdCurr);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                oldpass.setEnabled(false);
                                newpass.setEnabled(true);
                                renewpass.setEnabled(true);


                                authBtn.setEnabled(false);
                                updatepassBtn.setEnabled(true);


                                updatepassBtn.setBackgroundTintList(ContextCompat.getColorStateList(
                                        getContext(),R.color.purple_500));


                                updatepassBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePassword(firebaseUser);
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
    }

    private void changePassword(FirebaseUser firebaseUser) {

        String userNewPass = newpass.getEditText().getText().toString();
        String userReNewPass = renewpass.getEditText().getText().toString();

        if(TextUtils.isEmpty(userNewPass)){
            newpass.setError("Please enter your new password");
            newpass.requestFocus();
        }else if(TextUtils.isEmpty(userReNewPass)){
            renewpass.setError("Please re-enter your new password");
            renewpass.requestFocus();
        }else if(!userNewPass.matches(userReNewPass)){
            renewpass.setError("Please re-enter same password");
            renewpass.requestFocus();
        }else if(userPwdCurr.matches(userNewPass)){
            newpass.setError("Please enter a new password");
            newpass.requestFocus();
        }else {

            firebaseUser.updatePassword(userNewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();

                        Fragment donorprofile = new DonorProfileFragment();
                        FragmentTransaction dprofile = getActivity().getSupportFragmentManager().beginTransaction();
                        dprofile.replace(R.id.container,donorprofile).commit();

                    }else {
                        try{
                           throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });

        }

    }

}