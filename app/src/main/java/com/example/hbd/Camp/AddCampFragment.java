package com.example.hbd.Camp;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.hbd.Interface.IAllCampLoadListener;
import com.example.hbd.Interface.IAllCityLoadListener;
import com.example.hbd.Model.HospitalModel;
import com.example.hbd.Others.Common;
import com.example.hbd.R;
import com.example.hbd.Signup;
import com.example.hbd.Slot.SlotFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AddCampFragment extends Fragment implements IAllCampLoadListener {

    View v;
    TextInputLayout camporg, campdonorname, campdate, campdonorblood, campdonorage, campdonornum;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;
    CollectionReference collectionReference, collectionReference1;
    String userid;
    TextView campstate;
    Button addcamp,addcampdetailsBtn,viewcamp;
    TextInputEditText campdate1;
    DatePickerDialog datePicker;
    @BindView(R.id.spinnercamp)
    MaterialSpinner campspinner;


    Unbinder unbinder;
    IAllCampLoadListener iAllCampLoadListener;
    CollectionReference allcamp;

    String uhos;

    public AddCampFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iAllCampLoadListener = this;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_camp, container, false);
        unbinder = ButterKnife.bind(this,v);

        camporg = v.findViewById(R.id.camporg);
        campdonorname = v.findViewById(R.id.campdonorname);
        campdate = v.findViewById(R.id.campdate);
        campdonorblood = v.findViewById(R.id.campdonorblood);
        campdonorage = v.findViewById(R.id.campdonorage);
        campdonornum = v.findViewById(R.id.campdonornum);
        campstate = v.findViewById(R.id.campstate);
        addcamp = v.findViewById(R.id.addcampBtn);
        campdate1 = v.findViewById(R.id.camporgdate);
        addcampdetailsBtn = v.findViewById(R.id.addcampdetailsBtn);
        viewcamp = v.findViewById(R.id.viewcampBtn);

        addcampdetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment camp= new AddCampDetailsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,camp).commit();
            }
        });

        viewcamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment camp= new ViewCampFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,camp).commit();
            }
        });


        // authenticate user
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userid = firebaseUser.getUid();

        DocumentReference documentReference = firebaseFirestore.collection("User").document(userid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                campstate.setText(value.getString("ostate"));
                camporg.getEditText().setText(value.getString("uhos"));

                allcamp = FirebaseFirestore.getInstance().collection("State").document(campstate.getText().toString())
                        .collection("Branch").document(camporg.getEditText().getText().toString())
                        .collection("Camp");

                allcamp.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            List<String> list = new ArrayList<>();
                            list.add("Please Choose Camp");
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllCampLoadListener.onAllCampLoadSuccess(list);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iAllCampLoadListener.onAllCampLoadFailed(e.getMessage());
                    }
                });


            }
        });

        campdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        campdate.getEditText().setText(dayOfMonth + "/" + (month+1) + "/" + year);


                    }
                },year,month,day);
                datePicker.show();

            }
        });


        addcamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCampDetails();
            }
        });


       // loadAllCity();
        return v;
    }


    private void AddCampDetails() {


        String id = Common.generateString(6);

        Map<String,Object> map = new HashMap<>();
        map.put("hosname" , camporg.getEditText().getText().toString());
        map.put("campname" , campspinner.getText().toString());
        map.put("campstate" , campstate.getText().toString());
        map.put("campdate" , campdate.getEditText().getText().toString());

        map.put("campdonorname" , campdonorname.getEditText().getText().toString());
        map.put("campdonorblood" , campdonorblood.getEditText().getText().toString());
        map.put("campdonorage" , campdonorage.getEditText().getText().toString());
        map.put("campdonornum" , campdonornum.getEditText().getText().toString());
        map.put("campid" , id);

        collectionReference1 = FirebaseFirestore.getInstance()
                .collection("State").document(campstate.getText().toString())
                .collection("Branch").document(camporg.getEditText().getText().toString())
                .collection("Camp").document(campspinner.getText().toString())
                .collection("Campaign");


        collectionReference1.document(id)
                .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"Camp Details  Created");
                Toast.makeText(getActivity(), "Added successfully", Toast.LENGTH_SHORT).show();
                campdonorname.getEditText().getText().clear();
                campdonorage.getEditText().getText().clear();
                campdonorblood.getEditText().getText().clear();
                campdonornum.getEditText().getText().clear();

            }
        });




    }

    @Override
    public void onAllCampLoadSuccess(List<String> campList) {
        campspinner.setItems(campList);
    }

    @Override
    public void onAllCampLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }
}