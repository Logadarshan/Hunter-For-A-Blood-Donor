package com.example.hbd.Selftest;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.Appointment.StaffAppointmentFragment;
import com.example.hbd.Model.TestModel;
import com.example.hbd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


public class ViewResultFragment extends Fragment {

    TextView resultq1,resultq2,resultq3,resultq4,resultq5,resultq6,resultq7,resultq8,resultq9,resultq10,overallresult;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;
    Activity context;
    Button backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.fragment_view_result, container, false);

        context = getActivity();
        resultq1 = v.findViewById(R.id.resultansquestion1);
        resultq2 = v.findViewById(R.id.resultansquestion2);
        resultq3 = v.findViewById(R.id.resultansquestion3);
        resultq4 = v.findViewById(R.id.resultansquestion4);
        resultq5 = v.findViewById(R.id.resultansquestion5);
        resultq6 = v.findViewById(R.id.resultansquestion6);
        resultq7 = v.findViewById(R.id.resultansquestion7);
        resultq8 = v.findViewById(R.id.resultansquestion8);
        resultq9 = v.findViewById(R.id.resultansquestion9);
        resultq10 = v.findViewById(R.id.resultansquestion10);
        overallresult = v.findViewById(R.id.overallresult);
        backBtn = v.findViewById(R.id.backBtn3);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        showresult(firebaseUser);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment test = new TestQuestionFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,test).commit();
            }
        });




        return  v;
    }

    // display test results
    private void showresult(FirebaseUser firebaseUser) {



        String userID = firebaseUser.getUid();


        DocumentReference documentReference = firebaseFirestore.collection("User").document(userID).collection("Test").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                resultq1.setText(value.getString("dquestion1"));
                resultq2.setText(value.getString("dquestion2"));
                resultq3.setText(value.getString("dquestion3"));
                resultq4.setText(value.getString("dquestion4"));
                resultq5.setText(value.getString("dquestion5"));
                resultq6.setText(value.getString("dquestion6"));
                resultq7.setText(value.getString("dquestion7"));
                resultq8.setText(value.getString("dquestion8"));
                resultq9.setText(value.getString("dquestion9"));
                resultq10.setText(value.getString("dquestion10"));
                overallresult.setText(value.getString("resultans"));


            }
        });

    }
}