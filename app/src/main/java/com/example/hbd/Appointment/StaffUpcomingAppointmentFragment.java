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

import com.example.hbd.Adapter.SUpcomingAdapter;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class StaffUpcomingAppointmentFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    RecyclerView recyclerView;
    List<AppointmentModel> appointmentModelList;
    SUpcomingAdapter sUpcomingAdapter;
    TextView  noap;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference1;
    String userID, uhos;

    View v;


    static StaffUpcomingAppointmentFragment instance;

    public static StaffUpcomingAppointmentFragment getInstance(){

        if(instance == null)
            instance = new StaffUpcomingAppointmentFragment();
        return instance;

    }



    public StaffUpcomingAppointmentFragment() {
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
        v = inflater.inflate(R.layout.fragment_staff_upcoming_appointment, container, false);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        firebaseStorage   = FirebaseStorage.getInstance();
        noap = v.findViewById(R.id.noapp);
        recyclerView = v.findViewById(R.id.recyelerupcomingapp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        firebaseFirestore = FirebaseFirestore.getInstance();
        appointmentModelList = new ArrayList<AppointmentModel>();
        sUpcomingAdapter = new SUpcomingAdapter(StaffUpcomingAppointmentFragment.this,appointmentModelList);

        recyclerView.setAdapter(sUpcomingAdapter);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userID = firebaseUser.getUid();

        EventChange();

        return v;
    }

    private void EventChange() {

        databaseReference1 = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference1.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos = userModel.getUhos();

                    firebaseFirestore.collection("Appointment")
                            .whereEqualTo("hospappointment", uhos)
                            .whereEqualTo("statusappointment", "Awaiting")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    if(error != null){
                                        Log.e("Error", error.getMessage());
                                    }
                                    for (DocumentChange documentChange : value.getDocumentChanges()){

                                        if(documentChange.getType() == DocumentChange.Type.ADDED){
                                            recyclerView.setVisibility(View.VISIBLE);
                                            noap.setVisibility(View.INVISIBLE);
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