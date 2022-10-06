package com.example.hbd.Appointment;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbd.Home.DonorHome;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Profile.DonorProfileFragment;
import com.example.hbd.R;
import com.example.hbd.Signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AppointmentStep4Fragment extends Fragment {

    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    @BindView(R.id.uname)
    TextView usersname;
    @BindView(R.id.utime)
    TextView usertime;
    @BindView(R.id.udate)
    TextView userdate;
    @BindView(R.id.uhos)
    TextView userhosp;
    @BindView(R.id.ublood)
    TextView userblood;




    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference,databaseReference1;
    FirebaseDatabase firebaseDatabase;
    String uname, userID,ublood;


    @BindView(R.id.saveapp)
    Button saveappoint;




    BroadcastReceiver confirmAppointment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    public AppointmentStep4Fragment() {

    }

    private void setData() {



        String userID = firebaseUser.getUid();


        DocumentReference documentReference = firebaseFirestore.collection("User").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                usersname.setText(value.getString("uname"));
                userblood.setText(value.getString("ublood"));

            }
        });



        usertime.setText(Common.currentdate.getTime());
        userdate.setText(Common.currentdate.getDate());
        userhosp.setText(Common.appointmenthospital);

    }






    @OnClick(R.id.saveapp)
    void savebookingdata(){

        userID = firebaseUser.getUid();

        AppointmentModel appointmentModel = new AppointmentModel();

        firebaseUser = fAuth.getCurrentUser();

        Common.appointments = Common.generateString(6);

       // appointmentModel.setDone(false);
        appointmentModel.setDataappointment(Common.currentdate.getDate());
        appointmentModel.setTimeappointment(Common.currentdate.getTime());
        appointmentModel.setHospappointment(Common.appointmenthospital);
        appointmentModel.setStatusappointment("Awaiting");
        appointmentModel.setUsernameappoint(usersname.getText().toString());
        appointmentModel.setAppointmentid(Common.appointments);
        appointmentModel.setUseridappointment(userID);
        appointmentModel.setAppslotid(Common.currentdate.getSlotid());
        appointmentModel.setApploc(Common.city);
        appointmentModel.setUserblood(userblood.getText().toString());



        CollectionReference documentReference1 =firebaseFirestore.collection("State").document(Common.city)
                .collection("Branch").document(Common.appointmenthospital)
                .collection("Date").document(Common.appointmentdate)
                .collection("Slot");


         long value = Long.parseLong(Common.currentdate.getCapacity());

         Map<String,Object> map = new HashMap<>();
         map.put("capacity" , String.valueOf(value-1));

          documentReference1.document(Common.currentdate.getSlotid()).update(map);

          appointmentModel.setBalslot(String.valueOf(value-1));


        DocumentReference documentReference = firebaseFirestore.collection("Appointment")
                .document(Common.appointments);


        documentReference.set(appointmentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"Appointment Created" + firebaseUser.getUid());
                resetDataStatic();
                getActivity().finish();
            }
        });

        Toast.makeText(getActivity(), "Appointment added successfully", Toast.LENGTH_SHORT).show();

    }

    private void resetDataStatic() {

        Common.step = 0;
        Common.appointmentdate = null;
        Common.appointmenthospital = null;
    }


    static AppointmentStep4Fragment instance;

    public static AppointmentStep4Fragment getInstance(){

        if(instance == null)
            instance = new AppointmentStep4Fragment();
        return instance;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmAppointment, new IntentFilter(Common.KEY_CONFIRM_APPOINTMENT));

        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase.getReference().child("Appointment");


    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmAppointment);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_appointment_step4, container, false);

        unbinder = ButterKnife.bind(this,v);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return v;
    }
}