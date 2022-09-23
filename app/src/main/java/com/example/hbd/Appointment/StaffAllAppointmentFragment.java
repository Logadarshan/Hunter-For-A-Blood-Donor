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

import com.example.hbd.Adapter.AppointmentAdapter;
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

public class StaffAllAppointmentFragment extends Fragment {


    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    List<AppointmentModel> appointmentModelList;
    AppointmentAdapter appointmentAdapter;
    TextView noap1;
    View v ;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    TextView select;
    String userID, uhos;

    public StaffAllAppointmentFragment() {
        // Required empty public constructor
    }


    static StaffAllAppointmentFragment instance;

    public static StaffAllAppointmentFragment getInstance(){

        if(instance == null)
            instance = new StaffAllAppointmentFragment();
        return instance;

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_staff_all_appointment, container, false);




        noap1 = v.findViewById(R.id.noapp12);
        recyclerView = v.findViewById(R.id.recyelerallapp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        select = v.findViewById(R.id.selectcompleted);


        firebaseFirestore = FirebaseFirestore.getInstance();
        appointmentModelList = new ArrayList<AppointmentModel>();
        appointmentAdapter = new AppointmentAdapter(StaffAllAppointmentFragment.this,appointmentModelList);

        recyclerView.setAdapter(appointmentAdapter);


        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userID = firebaseUser.getUid();

        EventChange();

        return v;
    }

    private void EventChange() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos = userModel.getUhos();

                    firebaseFirestore.collection("Appointment")
                            .whereEqualTo("hospappointment", uhos)
                            .whereEqualTo("statusappointment" , "Completed")
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
                                        appointmentAdapter.notifyDataSetChanged();

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