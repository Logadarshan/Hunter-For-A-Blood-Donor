package com.example.hbd.Users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Adapter.UserAdapter;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton2;
    UserAdapter userAdapter;
    List<UserModel> userModelList;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser1;
    FirebaseAuth fAuth1;
    DatabaseReference databaseReference2;
    String userID, uhos2;


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
                            .whereEqualTo("usertype", "Admin")
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
                                        userAdapter.notifyDataSetChanged();

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