package com.example.hbd.Users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Adapter.UserAdapter;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;
    UserModel userModel;

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton2;
    UserAdapter userAdapter;
    List<UserModel> userModelList;
    DatabaseReference databaseReference;
    CollectionReference citiesRef;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v=  inflater.inflate(R.layout.fragment_user, container, false);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        firebaseStorage   = FirebaseStorage.getInstance();
        floatingActionButton2 = v.findViewById(R.id.adduserfloatbutton);
        recyclerView = v.findViewById(R.id.rvview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment adduser = new AddUserFragment();
                FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
                addappoint.replace(R.id.container,adduser).commit();
            }
        });


        userModelList = new ArrayList<UserModel>();
        userAdapter = new UserAdapter(UserFragment.this,userModelList);

        recyclerView.setAdapter(userAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Select usertype equals to Staff
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("usertype").equalTo("Staff");

       // Display Staff details
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                userModelList.add(userModel);
                userAdapter.notifyDataSetChanged();

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