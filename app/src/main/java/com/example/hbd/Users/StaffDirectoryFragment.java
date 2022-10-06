package com.example.hbd.Users;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hbd.Adapter.StaffuserAdapter;
import com.example.hbd.Adapter.UserAdapter;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class StaffDirectoryFragment extends Fragment {


    View v;

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton2;
    StaffuserAdapter staffuserAdapter;
    List<UserModel> userModelList;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser1;
    FirebaseAuth fAuth1;
    DatabaseReference databaseReference2;
    String userID, uhos2;


    public StaffDirectoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.fragment_staff_directory, container, false);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        firebaseStorage   = FirebaseStorage.getInstance();
        floatingActionButton2 = v.findViewById(R.id.adduserfloatbutton);
        recyclerView = v.findViewById(R.id.rvview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment adduser = new AddStaffFragment();
                FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
                addappoint.replace(R.id.container,adduser).commit();
            }
        });


        userModelList = new ArrayList<UserModel>();
        staffuserAdapter = new StaffuserAdapter(StaffDirectoryFragment.this,userModelList);

        recyclerView.setAdapter(staffuserAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth1 = FirebaseAuth.getInstance();
        firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();

        userID = firebaseUser1.getUid();
        EventLis();

        return v;
    }

    private void EventLis() {

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos2 = userModel.getUhos();

                    firebaseFirestore.collection("User")
                            .whereEqualTo("usertype", "Staff")
                            .whereEqualTo("uhos", uhos2)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    if(error != null){
                                        Log.e("Error", error.getMessage());
                                    }
                                    for (DocumentChange documentChange : value.getDocumentChanges()){

                                        if(documentChange.getType() == DocumentChange.Type.ADDED){
                                            userModelList.add(documentChange.getDocument().toObject(UserModel.class));
                                        }
                                        staffuserAdapter.notifyDataSetChanged();

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