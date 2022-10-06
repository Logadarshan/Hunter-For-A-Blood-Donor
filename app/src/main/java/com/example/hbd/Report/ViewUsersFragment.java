package com.example.hbd.Report;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hbd.Adapter.UserAdapter;
import com.example.hbd.Adapter.UserAdminViewAdapter;
import com.example.hbd.Interface.IAllHosLoadListener;
import com.example.hbd.Interface.IAllStateLoadListener;
import com.example.hbd.Model.UserModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.R;
import com.example.hbd.Slot.SlotFragment;
import com.example.hbd.Users.UserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewUsersFragment extends Fragment implements IAllStateLoadListener, IAllHosLoadListener {

    View v;
    @BindView(R.id.spinnerstate1)
    MaterialSpinner statespinner;
    @BindView(R.id.spinnerhos1)
    MaterialSpinner hosspinner;
    @BindView(R.id.recyeleruser)
    RecyclerView userrecycle;


    Spinner userspinner;
    CollectionReference allcity,allbranch;
    Unbinder unbinder;

    IAllStateLoadListener iAllStateLoadListener;
    IAllHosLoadListener iAllHosLoadListener;

    UserAdminViewAdapter userAdminViewAdapter;
    List<UserModel> userModelList;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser1;
    FirebaseAuth fAuth1;
    String userID;

    Button searchBtn, refreshBtn;


    public ViewUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allcity = FirebaseFirestore.getInstance().collection("State");
        iAllStateLoadListener = this;
        iAllHosLoadListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       v =  inflater.inflate(R.layout.fragment_view_users, container, false);
       unbinder = ButterKnife.bind(this,v);

       userspinner = v.findViewById(R.id.spinneruser1);
       searchBtn = v.findViewById(R.id.searchBtn);
       refreshBtn = v.findViewById(R.id.refreshBtn);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.user,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        userspinner.setAdapter(adapter);


        userModelList = new ArrayList<UserModel>();
        userAdminViewAdapter = new UserAdminViewAdapter(ViewUsersFragment.this,userModelList);

        userrecycle.setAdapter(userAdminViewAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth1 = FirebaseAuth.getInstance();
        firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();

        userID = firebaseUser1.getUid();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             EventList();


            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment refresh = new ViewUsersFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,refresh).commit();
            }
        });




        initView();
       loadAllCity();


       return v;
    }



    private void EventList() {

        firebaseFirestore.collection("User")
                .whereEqualTo("uhos", hosspinner.getText().toString())
                .whereEqualTo("usertype", userspinner.getSelectedItem().toString())
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
                            userAdminViewAdapter.notifyDataSetChanged();




                        }


                    }
                });





    }


    private void initView() {

        userrecycle.setHasFixedSize(true);
        userrecycle.setLayoutManager(new GridLayoutManager(getActivity(),1));
        userrecycle.addItemDecoration(new SpacesItemDecoration(4));


    }

    private void loadAllCity() {
        allcity.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    List<String> list = new ArrayList<>();
                    list.add("Please Choose State");
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                        list.add(documentSnapshot.getId());
                    iAllStateLoadListener.onAllStateLoadSuccess(list);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllStateLoadListener.onAllStateLoadFailed(e.getMessage());
            }
        });



    }




    @Override
    public void onAllStateLoadSuccess(List<String> stateList) {
        statespinner.setItems(stateList);
        statespinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0){
                    loadBranch(item.toString());
                }
            }
        });
    }

    private void loadBranch(String city) {
        Common.city= city;

        allbranch= FirebaseFirestore.getInstance()
                .collection("State").document(city)
                .collection("Branch");


        allbranch.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> list = new ArrayList<>();
                list.add("Please Choose Hospital");
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                        list.add(documentSnapshot.getId());
                    iAllHosLoadListener.onAllHosLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllHosLoadListener.onAllHosLoadFailed(e.getMessage());
            }
        });



    }

    @Override
    public void onAllStateLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAllHosLoadSuccess(List<String> hosList) {
        hosspinner.setItems(hosList);
    }

    @Override
    public void onAllHosLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }
}