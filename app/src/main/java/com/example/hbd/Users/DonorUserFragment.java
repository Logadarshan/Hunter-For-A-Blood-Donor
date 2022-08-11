package com.example.hbd.Users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Adapter.DonorUserAdapter;
import com.example.hbd.Model.TestModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class DonorUserFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    RecyclerView recyclerView;
    DonorUserAdapter donorUserAdapter;
    List<UserModel> userModelList;
    List<TestModel> testModelList;
    DatabaseReference databaseReference;
    View v;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_donor_user, container, false);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        firebaseStorage   = FirebaseStorage.getInstance();
        recyclerView = v.findViewById(R.id.rvview2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        userModelList = new ArrayList<UserModel>();
        donorUserAdapter = new DonorUserAdapter(DonorUserFragment.this,userModelList);

        recyclerView.setAdapter(donorUserAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Select usertype equals to donor
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("usertype").equalTo("Donor");

        // display user details
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                userModelList.add(userModel);
                donorUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return v;
    }






}