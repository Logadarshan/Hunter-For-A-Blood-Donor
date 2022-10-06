package com.example.hbd.Camp;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbd.Others.Common;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddCampDetailsFragment extends Fragment {

    View v;
    TextInputLayout camporg1, campname1, camptime1, campdate1;
    TextView campstate1;
    Button addcamp1;
    DocumentReference documentReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;
    String userid;
    CollectionReference collectionReference, collectionReference1;
    TextInputEditText camporgdate1;
    DatePickerDialog datePicker;

    public AddCampDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_camp_details, container, false);


        camporg1 = v.findViewById(R.id.camporg1);
        campname1 = v.findViewById(R.id.campname1);
        camptime1 = v.findViewById(R.id.camptime1);
        campdate1 = v.findViewById(R.id.campdate1);
        addcamp1 = v.findViewById(R.id.addcampBtn1);
        campstate1 = v.findViewById(R.id.campstate1);
        camporgdate1 = v.findViewById(R.id.camporgdate1);


        // authenticate user
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userid = firebaseUser.getUid();


        camporgdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        campdate1.getEditText().setText(dayOfMonth + "/" + (month+1) + "/" + year);


                    }
                },year,month,day);
                datePicker.show();

            }
        });


        documentReference = firebaseFirestore.collection("User").document(userid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                campstate1.setText(value.getString("ostate"));
                camporg1.getEditText().setText(value.getString("uhos"));


            }
        });

        addcamp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCampDetails();
            }
        });




        return v;
    }

    private void AddCampDetails() {


        Map<String,Object> map1 = new HashMap<>();
        map1.put("hosname" , camporg1.getEditText().getText().toString());
        map1.put("campdate" , campdate1.getEditText().getText().toString());
        map1.put("camptime" , camptime1.getEditText().getText().toString());
        map1.put("campname" , campname1.getEditText().getText().toString());


        collectionReference = FirebaseFirestore.getInstance()
                .collection("State").document(campstate1.getText().toString())
                .collection("Branch").document(camporg1.getEditText().getText().toString())
                .collection("Camp");

        collectionReference.document(campname1.getEditText().getText().toString())
                .set(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"Camp Details  Created");
                Toast.makeText(getActivity(), "Added successfully", Toast.LENGTH_SHORT).show();
                Fragment camp= new AddCampFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,camp).commit();

            }
        });

    }
}