package com.example.hbd.Selftest;

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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.Appointment.AppointmentFragment;
import com.example.hbd.Home.DonorHome;
import com.example.hbd.Model.TestModel;
import com.example.hbd.Others.Common;
import com.example.hbd.R;
import com.example.hbd.Signup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestQuestionFragment extends Fragment {


    RadioGroup radioGroupQuestion1,radioGroupQuestion2,radioGroupQuestion3,radioGroupQuestion4,
               radioGroupQuestion5,radioGroupQuestion6,radioGroupQuestion7,radioGroupQuestion8,
               radioGroupQuestion9,radioGroupQuestion10;

    RadioButton radioButtonQuestion1,radioButtonQuestion2,radioButtonQuestion3,radioButtonQuestion4,
                radioButtonQuestion5,radioButtonQuestion6,radioButtonQuestion7,radioButtonQuestion8,
                radioButtonQuestion9,radioButtonQuestion10;
    Button testagree;
    ImageView testresult;

    public static final String TAG = "TAG";
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    String userID;
    Dialog dialog;


    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         v = inflater.inflate(R.layout.fragment_test_question, container, false);


        radioGroupQuestion1  = v.findViewById(R.id.answerquestion1);
        radioGroupQuestion1.clearCheck();
        radioGroupQuestion2  = v.findViewById(R.id.answerquestion2);
        radioGroupQuestion2.clearCheck();
        radioGroupQuestion3  = v.findViewById(R.id.answerquestion3);
        radioGroupQuestion3.clearCheck();
        radioGroupQuestion4  = v.findViewById(R.id.answerquestion4);
        radioGroupQuestion4.clearCheck();
        radioGroupQuestion5  = v.findViewById(R.id.answerquestion5);
        radioGroupQuestion5.clearCheck();
        radioGroupQuestion6  = v.findViewById(R.id.answerquestion6);
        radioGroupQuestion6.clearCheck();
        radioGroupQuestion7  = v.findViewById(R.id.answerquestion7);
        radioGroupQuestion7.clearCheck();
        radioGroupQuestion8  = v.findViewById(R.id.answerquestion8);
        radioGroupQuestion8.clearCheck();
        radioGroupQuestion9  = v.findViewById(R.id.answerquestion9);
        radioGroupQuestion9.clearCheck();
        radioGroupQuestion10 = v.findViewById(R.id.answerquestion10);
        radioGroupQuestion10.clearCheck();

        testresult = v.findViewById(R.id.resultBtn);

        testagree = v.findViewById(R.id.testagreeBtn);

        dialog = new Dialog(this.getContext());


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Test");
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // insert test data into database
        testagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                inserttestdata();

            }
        });

        // View result page
        testresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment test = new ViewResultFragment();
                FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
                addappoint.replace(R.id.container,test).commit();
            }
        });

        return v;
    }

    // insert test result into the database
    private void inserttestdata() {


        int selectedQuestion1 = radioGroupQuestion1.getCheckedRadioButtonId();
        radioButtonQuestion1 = v.findViewById(selectedQuestion1);
        int selectedQuestion2 = radioGroupQuestion2.getCheckedRadioButtonId();
        radioButtonQuestion2 = v.findViewById(selectedQuestion2);
        int selectedQuestion3 = radioGroupQuestion3.getCheckedRadioButtonId();
        radioButtonQuestion3 = v.findViewById(selectedQuestion3);
        int selectedQuestion4 = radioGroupQuestion4.getCheckedRadioButtonId();
        radioButtonQuestion4 = v.findViewById(selectedQuestion4);
        int selectedQuestion5 = radioGroupQuestion5.getCheckedRadioButtonId();
        radioButtonQuestion5 = v.findViewById(selectedQuestion5);
        int selectedQuestion6 = radioGroupQuestion6.getCheckedRadioButtonId();
        radioButtonQuestion6 = v.findViewById(selectedQuestion6);
        int selectedQuestion7 = radioGroupQuestion7.getCheckedRadioButtonId();
        radioButtonQuestion7 = v.findViewById(selectedQuestion7);
        int selectedQuestion8 = radioGroupQuestion8.getCheckedRadioButtonId();
        radioButtonQuestion8 = v.findViewById(selectedQuestion8);
        int selectedQuestion9 = radioGroupQuestion9.getCheckedRadioButtonId();
        radioButtonQuestion9 = v.findViewById(selectedQuestion9);
        int selectedQuestion10 = radioGroupQuestion10.getCheckedRadioButtonId();
        radioButtonQuestion10 = v.findViewById(selectedQuestion10);


        RadioButton q1a = v.findViewById(R.id.radio_yes1);
        RadioButton q1b = v.findViewById(R.id.radio_no1);
        RadioButton q2a = v.findViewById(R.id.radio_yes2);
        RadioButton q2b = v.findViewById(R.id.radio_no2);
        RadioButton q3a = v.findViewById(R.id.radio_yes3);
        RadioButton q3b = v.findViewById(R.id.radio_no3);
        RadioButton q4a = v.findViewById(R.id.radio_yes4);
        RadioButton q4b = v.findViewById(R.id.radio_no4);
        RadioButton q5a = v.findViewById(R.id.radio_yes5);
        RadioButton q5b = v.findViewById(R.id.radio_no5);
        RadioButton q6a = v.findViewById(R.id.radio_yes6);
        RadioButton q6b = v.findViewById(R.id.radio_no6);
        RadioButton q7a = v.findViewById(R.id.radio_yes7);
        RadioButton q7b = v.findViewById(R.id.radio_no7);
        RadioButton q8a = v.findViewById(R.id.radio_yes8);
        RadioButton q8b = v.findViewById(R.id.radio_no8);
        RadioButton q9a = v.findViewById(R.id.radio_yes9);
        RadioButton q9b = v.findViewById(R.id.radio_no9);
        RadioButton q10a = v.findViewById(R.id.radio_yes10);
        RadioButton q10b = v.findViewById(R.id.radio_no10);



        String dquestion1  = radioButtonQuestion1.getText().toString();
        String dquestion2  = radioButtonQuestion2.getText().toString();
        String dquestion3  = radioButtonQuestion3.getText().toString();
        String dquestion4  = radioButtonQuestion4.getText().toString();
        String dquestion5  = radioButtonQuestion5.getText().toString();
        String dquestion6  = radioButtonQuestion6.getText().toString();
        String dquestion7  = radioButtonQuestion7.getText().toString();
        String dquestion8  = radioButtonQuestion8.getText().toString();
        String dquestion9  = radioButtonQuestion9.getText().toString();
        String dquestion10 = radioButtonQuestion10.getText().toString();
        String resultans;
        String testid = Common.generateString(6);
        firebaseUser = fAuth.getCurrentUser();
        String usertestid = firebaseUser.getUid();


        // questions requirement
        if(q1b.isChecked() ||  q4a.isChecked() || q5a.isChecked()
        || q6a.isChecked() || q7a.isChecked() || q8a.isChecked() || q9a.isChecked() || q10a.isChecked()){


             resultans = "You are not eligible to donate blood ";
            failDialog();

        }
        else{

            resultans = "You are eligible to donate blood ";
            sucessDialog();

        }

        // insert into the database
        TestModel testModel = new TestModel(dquestion1, dquestion2,  dquestion3, dquestion4, dquestion5,
                dquestion6,  dquestion7,  dquestion8, dquestion9,  dquestion10, resultans,usertestid, testid);




       // databaseReference.child(usertestid).setValue(testModel);
        DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Test").document(firebaseUser.getUid());
        documentReference.set(testModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Test recorded successfully", Toast.LENGTH_SHORT).show();
            }
        });




    }

    // if failed selftest
    private void failDialog() {

        dialog.setContentView(R.layout.failmessage);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btncon2 = dialog.findViewById(R.id.contapp2Btn);

        // Navigate to Selftest Page
        btncon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment cancel = new SelftestFragment();
                FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
                addappoint.replace(R.id.container,cancel).commit();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // if passed selftest
    private void sucessDialog() {

        dialog.setContentView(R.layout.successmessage);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btncon = dialog.findViewById(R.id.contappBtn);
        Button btncan = dialog.findViewById(R.id.cancelappBtn);


        // Navigate to make appointment Page
        btncon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment app = new AppointmentFragment();
                FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
                addappoint.replace(R.id.container,app).commit();
                dialog.dismiss();
            }
        });

        // Navigate to selftest Page
        btncan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment cancel = new SelftestFragment();
                FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
                addappoint.replace(R.id.container,cancel).commit();
                dialog.dismiss();

            }
        });

        dialog.show();


    }


}