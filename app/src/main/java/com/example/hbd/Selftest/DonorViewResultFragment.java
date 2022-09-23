package com.example.hbd.Selftest;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hbd.Model.TestModel;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class DonorViewResultFragment extends Fragment {

    TextView resultq1,resultq2,resultq3,resultq4,resultq5,resultq6,resultq7,resultq8,resultq9,resultq10,overallresult;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    Activity context;
    TestModel testModel;
    long tid =0 ;
    String userid;
    FirebaseFirestore firebaseFirestore;

    String dquestion1, dquestion2,  dquestion3, dquestion4, dquestion5,
            dquestion6,  dquestion7,  dquestion8, dquestion9,  dquestion10, resultans,usertestid;

    public DonorViewResultFragment(String userid) {
        this.userid = userid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_donor_view_result, container, false);


        context = getActivity();
        resultq1 = v.findViewById(R.id.resultansquestion11);
        resultq2 = v.findViewById(R.id.resultansquestion21);
        resultq3 = v.findViewById(R.id.resultansquestion31);
        resultq4 = v.findViewById(R.id.resultansquestion41);
        resultq5 = v.findViewById(R.id.resultansquestion51);
        resultq6 = v.findViewById(R.id.resultansquestion61);
        resultq7 = v.findViewById(R.id.resultansquestion71);
        resultq8 = v.findViewById(R.id.resultansquestion81);
        resultq9 = v.findViewById(R.id.resultansquestion91);
        resultq10 = v.findViewById(R.id.resultansquestion101);
        overallresult = v.findViewById(R.id.overallresult1);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();


        testresult();




        return v;
    }


    // display donor's result
    private void testresult(){

        firebaseFirestore.collection("User").document(userid)
                .collection("Test").whereEqualTo("usertestid", userid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                            resultq1.setText(document.getString("dquestion1"));
                            resultq2.setText(document.getString("dquestion2"));
                            resultq3.setText(document.getString("dquestion3"));
                            resultq4.setText(document.getString("dquestion4"));
                            resultq5.setText(document.getString("dquestion5"));
                            resultq6.setText(document.getString("dquestion6"));
                            resultq7.setText(document.getString("dquestion7"));
                            resultq8.setText(document.getString("dquestion8"));
                            resultq9.setText(document.getString("dquestion9"));
                            resultq10.setText(document.getString("dquestion10"));
                            overallresult.setText(document.getString("resultans"));
                            Log.d(TAG, document.getId() + " => " + document.getData());




                    }
                }





            }
        });


    }


}