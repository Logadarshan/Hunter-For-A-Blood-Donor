package com.example.hbd.Appointment;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Profile.DonorUpdateProfileFragment;
import com.example.hbd.Profile.ViewHistoryFragment;
import com.example.hbd.R;
import com.example.hbd.Selftest.SelftestFragment;
import com.example.hbd.Signup;
import com.example.hbd.TrackNearByActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppointmentFragment extends Fragment {

    ImageButton appappointmentBtn, viewhistory,locationBtn;
    CardView cardView;
    Button deleteappBtn, changeappBtn;
    TextView hosptime,hosploc, hospdate,hospstatus,hospslot,hospcity,sbalslot,hosnum1;
    Activity context;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;
    List<AppointmentModel> appointmentModelList;
    Dialog dialog;

    View v;

    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_appointment, container, false);

        cardView = v.findViewById(R.id.cardupcomingapp);
        hosptime = v.findViewById(R.id.hostimeapp);
        hosploc = v.findViewById(R.id.hoslocation);
        hospdate = v.findViewById(R.id.hosdateapp);
        hospstatus = v.findViewById(R.id.hosstatus);
        hospcity = v.findViewById(R.id.hoscity);
        hospslot = v.findViewById(R.id.hosslotid);
        sbalslot = v.findViewById(R.id.hosbalslot);
        hosnum1  = v.findViewById(R.id.hosconnum1);
        appappointmentBtn = v.findViewById(R.id.addappointBtn);
        viewhistory = v.findViewById(R.id.viewhistoryBtn);
        locationBtn = v.findViewById(R.id.tracklocationBtn);
        deleteappBtn = v.findViewById(R.id.appdeletebtn);
        changeappBtn = v.findViewById(R.id.appupdateBtn);
        dialog = new Dialog(this.getContext());

        // authenticate user
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        context = getActivity();


        loadUserAppointment();





        appappointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = firebaseUser.getUid();

                firebaseFirestore.collection("User")
                        .document(userID).collection("Test")
                        .whereEqualTo("usertestid", userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        String data2 = document.getString("resultans");

                                        if (data2.contains("You are eligible to donate blood"))
                                        {
                                            startActivity(new Intent(getActivity(), AppointmentActivity.class));
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                        }else if (data2.contains("not")){
                                            failDialog();

                                        }else{
                                            reminderDialog();
                                        }


                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });




            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), TrackNearByActivity.class));
            }
        });

        viewhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment viewhis = new ViewHistoryFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,viewhis).commit();

            }
        });



        deleteappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAppointment();
            }
        });

        changeappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppointment();
            }
        });


    return v;
    }

    private void reminderDialog() {

        dialog.setContentView(R.layout.selftestreminder);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btncon1 = dialog.findViewById(R.id.continueappBtn);

        // Navigate to selftest Page
        btncon1.setOnClickListener(new View.OnClickListener() {
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

    private void changeAppointment() {
        androidx.appcompat.app.AlertDialog.Builder confirmDialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        confirmDialog.setTitle("Are you Sure?");
        confirmDialog.setMessage("Do you really wish to change appointment details?Once you clicked the previous appointment information is not available. If you're okay click confirm ");
        // failed to delete

        confirmDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteAppointment();
                startActivity(new Intent(getActivity(), AppointmentActivity.class));
            }
        });
        // if successfully deleted
        confirmDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        confirmDialog.show();

    }

    private void deleteAppointment() {

        String userID = firebaseUser.getUid();



        CollectionReference documentReference1 =firebaseFirestore.collection("State").document(hospcity.getText().toString())
                .collection("Branch").document(hosploc.getText().toString())
                .collection("Date").document(hospdate.getText().toString())
                .collection("Slot");


        long value = Long.parseLong(sbalslot.getText().toString());

        Map<String,Object> map = new HashMap<>();
        map.put("capacity" , String.valueOf(value+1));

        documentReference1.document(hospslot.getText().toString()).update(map);


        firebaseFirestore.collection("Appointment")
                .whereEqualTo("useridappointment", userID)
                .limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    String data1 = documentSnapshot.getString("statusappointment");

                    if (data1.contains("A"))
                    {
                        firebaseFirestore.collection("Appointment")
                                .document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Successfully deleted. You can make another appointment", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }

            }
        });
    }

    private void loadUserAppointment() {

        String userID1 = firebaseUser.getUid();

        firebaseFirestore.collection("Appointment")
                .whereEqualTo("useridappointment", userID1)
                  .get()
                  .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String data1 = document.getString("statusappointment");

                               if (data1.contains("A")){
                                    cardView.setVisibility(View.VISIBLE);
                                    appappointmentBtn.setEnabled(false);
                                    hosptime.setText(document.getString("timeappointment"));
                                    hospstatus.setText(document.getString("statusappointment"));
                                    hospdate.setText(document.getString("dataappointment"));
                                    hosploc.setText(document.getString("hospappointment"));
                                    hospcity.setText(document.getString("apploc"));
                                    hospslot.setText(document.getString("appslotid"));
                                    sbalslot.setText(document.getString("balslot"));
                                    loadnum();
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                              }



                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void loadnum() {




        DocumentReference documentReference = firebaseFirestore.collection("State").document(hospcity.getText().toString())
                .collection("Branch")
                .document(hosploc.getText().toString());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                hosnum1.setText(value.getString("num"));

            }
        });


    }


}