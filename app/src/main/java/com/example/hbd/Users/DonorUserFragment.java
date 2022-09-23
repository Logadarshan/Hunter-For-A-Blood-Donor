package com.example.hbd.Users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Adapter.DonorUserAdapter;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.TestModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
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

public class DonorUserFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    RecyclerView recyclerView;
    DonorUserAdapter donorUserAdapter;
    List<UserModel> userModelList;
    List<TestModel> testModelList;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference2;
    String userID, uhos;
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
        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userID = firebaseUser.getUid();
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
                    uhos = userModel.getUhos();

                    firebaseFirestore.collection("User")
                            .whereEqualTo("usertype", "Donor")
                            .whereEqualTo("uhos", uhos)
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
                                        donorUserAdapter.notifyDataSetChanged();

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