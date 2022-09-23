package com.example.hbd.Others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hbd.Appointment.AppointmentFragment;
import com.example.hbd.Login;
import com.example.hbd.MainActivity;
import com.example.hbd.R;
import com.example.hbd.Selftest.SelftestFragment;
import com.google.firebase.auth.FirebaseAuth;

public class DeletedAccount extends AppCompatActivity {


    Dialog dialog;
    Button deleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_account);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DeletedAccount.this, Login.class));
                finish();
            }
        },5000);





    }






}