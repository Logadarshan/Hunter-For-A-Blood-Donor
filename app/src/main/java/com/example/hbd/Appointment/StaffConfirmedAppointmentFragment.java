package com.example.hbd.Appointment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbd.Adapter.SConfirmAdapter;
import com.example.hbd.Adapter.SUpcomingAdapter;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StaffConfirmedAppointmentFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    RecyclerView recyclerView;
    List<AppointmentModel> appointmentModelList;
    SConfirmAdapter sUpcomingAdapter;
    TextView noap1;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference2;
    String userID, uhos;


    View v;

    static StaffConfirmedAppointmentFragment instance;

    public static StaffConfirmedAppointmentFragment getInstance(){

        if(instance == null)
            instance = new StaffConfirmedAppointmentFragment();
        return instance;

    }



    public StaffConfirmedAppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_staff_confirmed_appointment, container, false);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        firebaseStorage   = FirebaseStorage.getInstance();
        recyclerView = v.findViewById(R.id.recyelerupcomingapp11);
        noap1 = v.findViewById(R.id.noapp1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        firebaseFirestore = FirebaseFirestore.getInstance();
        appointmentModelList = new ArrayList<AppointmentModel>();
        sUpcomingAdapter = new SConfirmAdapter(StaffConfirmedAppointmentFragment.this,appointmentModelList);

        recyclerView.setAdapter(sUpcomingAdapter);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userID = firebaseUser.getUid();

        EventChange();

        return v;
    }


    private void EventChange() {

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos = userModel.getUhos();

                    String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy, HHmm ", Locale.getDefault()).format(new Date());
                    String[] currenttimedate = simpleDateFormat.split(",");

                    firebaseFirestore.collection("Appointment")
                            .whereEqualTo("hospappointment", uhos)
                            .whereEqualTo("statusappointment", "Approved")
                            .whereEqualTo("dataappointment", currenttimedate[0])
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    if(error != null){
                                        Log.e("Error", error.getMessage());
                                    }
                                    for (DocumentChange documentChange : value.getDocumentChanges()){


                                        if(documentChange.getType() == DocumentChange.Type.ADDED){
                                            recyclerView.setVisibility(View.VISIBLE);
                                            noap1.setVisibility(View.INVISIBLE);
                                            appointmentModelList.add(documentChange.getDocument().toObject(AppointmentModel.class));
                                        }
                                        sUpcomingAdapter.notifyDataSetChanged();

                                    }


                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


}