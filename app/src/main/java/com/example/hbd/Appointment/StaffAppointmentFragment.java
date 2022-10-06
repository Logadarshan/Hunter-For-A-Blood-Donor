package com.example.hbd.Appointment;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.Model.HospitalModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Profile.ViewHistoryFragment;
import com.example.hbd.R;
import com.example.hbd.Slot.AddSlotFragment;
import com.example.hbd.Slot.SlotFragment;
import com.example.hbd.TrackNearByActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StaffAppointmentFragment extends Fragment {


    ImageButton appappointment1Btn, viewhistory1Btn,viewappointments,slotBtn, locationBtn;
    CardView cardView1;
    Button deleteapp1Btn1, changeapp1Btn1;
    TextView hosptime1,hosploc1, hospdate1,hospstatus1,hospslot1,hospcity1,sbalslot1,hossappid,hosnum;
    Activity context;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    FirebaseFirestore firebaseFirestore;

    View v;

    public StaffAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_staff_appointment, container, false);

        cardView1 = v.findViewById(R.id.cardupcomingapp1);
        hosptime1 = v.findViewById(R.id.hostimeapp1);
        hosploc1 = v.findViewById(R.id.hoslocation1);
        hospdate1 = v.findViewById(R.id.hosdateapp1);
        hospstatus1 = v.findViewById(R.id.hosstatus1);
        hospcity1 = v.findViewById(R.id.hoscity1);
        hospslot1 = v.findViewById(R.id.hosslotid1);
        sbalslot1 = v.findViewById(R.id.hosbalslot1);
        hossappid = v.findViewById(R.id.hosappid1);
        hosnum= v.findViewById(R.id.hosconnum);
        appappointment1Btn = v.findViewById(R.id.addappoint1Btn);
        viewhistory1Btn = v.findViewById(R.id.viewhistory1Btn);
        viewappointments = v.findViewById(R.id.viewdonorappointmentsBtn);
        slotBtn = v.findViewById(R.id.appointmentslotsBtn);
        locationBtn = v.findViewById(R.id.tracklocation1Btn);
        deleteapp1Btn1 = v.findViewById(R.id.appdeletebtn1);
        changeapp1Btn1 = v.findViewById(R.id.appupdateBtn1);



        // authenticate user
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        context = getActivity();


        firebaseFirestore = FirebaseFirestore.getInstance();





        loadUserAppointment();


        appappointment1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), AppointmentActivity.class));

            }
        });


        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), TrackNearByActivity.class));

            }
        });


        viewhistory1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment viewhis = new ViewHistoryFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,viewhis).commit();

            }
        });

        slotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment viewslot = new SlotFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,viewslot).commit();

            }
        });


        deleteapp1Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAppointment();
            }
        });

        changeapp1Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppointment();
            }
        });


        viewappointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), StaffAppointmentActivity.class));

            }
        });




        return v;

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



        CollectionReference documentReference1 =firebaseFirestore.collection("State").document(hospcity1.getText().toString())
                .collection("Branch").document(hosploc1.getText().toString())
                .collection("Date").document(hospdate1.getText().toString())
                .collection("Slot");


        long value = Long.parseLong(sbalslot1.getText().toString());

        Map<String,Object> map = new HashMap<>();
        map.put("capacity" , String.valueOf(value+1));

        documentReference1.document(hospslot1.getText().toString()).update(map);


        firebaseFirestore.collection("Appointment")
                .whereEqualTo("useridappointment", userID)
                .whereEqualTo("appointmentid", hossappid.getText().toString())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    String data1 = documentSnapshot.getString("statusappointment");


                        firebaseFirestore.collection("Appointment")
                                .document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Successfully deleted. You can make another appointment", Toast.LENGTH_SHORT).show();
                            }
                        });



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
                                    cardView1.setVisibility(View.VISIBLE);
                                    appappointment1Btn.setEnabled(false);
                                    hosptime1.setText(document.getString("timeappointment"));
                                    hospstatus1.setText(document.getString("statusappointment"));
                                    hospdate1.setText(document.getString("dataappointment"));
                                    hosploc1.setText(document.getString("hospappointment"));
                                    hospcity1.setText(document.getString("apploc"));
                                    hospslot1.setText(document.getString("appslotid"));
                                    sbalslot1.setText(document.getString("balslot"));
                                    hossappid.setText(document.getString("appointmentid"));

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




        DocumentReference documentReference = firebaseFirestore.collection("State").document(hospcity1.getText().toString())
                .collection("Branch")
                .document(hosploc1.getText().toString());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                hosnum.setText(value.getString("num"));

            }
        });


    }

}