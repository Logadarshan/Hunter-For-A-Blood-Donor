package com.example.hbd.Profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hbd.Adapter.HistoryAdapter;
import com.example.hbd.Adapter.UserAdapter;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.R;
import com.example.hbd.Users.AddUserFragment;
import com.example.hbd.Users.UserFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ViewHistoryFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;
    AppointmentModel appointmentModel;

    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    List<AppointmentModel> appointmentModelList;
    DatabaseReference databaseReference;



    View v;

    public ViewHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       v = inflater.inflate(R.layout.fragment_view_history, container, false);



        firebaseDatabase  = FirebaseDatabase.getInstance();
        recyclerView = v.findViewById(R.id.recyclerhistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));



        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        appointmentModelList = new ArrayList<AppointmentModel>();
        historyAdapter = new HistoryAdapter(ViewHistoryFragment.this,appointmentModelList);

        recyclerView.setAdapter(historyAdapter);

        initView();
        EventChangeListner();

       return v;
    }

    private void initView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));

    }

    private void EventChangeListner() {

        String userid = firebaseUser.getUid();

        firebaseFirestore.collection("Appointment")
                .whereEqualTo("useridappointment", userid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Error", error.getMessage());
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){

                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                appointmentModelList.add(documentChange.getDocument().toObject(AppointmentModel.class));
                            }
                            historyAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

}
