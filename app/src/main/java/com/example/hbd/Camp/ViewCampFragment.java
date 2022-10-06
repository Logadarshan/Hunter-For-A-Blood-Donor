package com.example.hbd.Camp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.hbd.Adapter.AppointmentAdminViewAdapter;
import com.example.hbd.Adapter.CampAdapter;
import com.example.hbd.Adapter.HospitalAdapter;
import com.example.hbd.Interface.Camp;
import com.example.hbd.Interface.IAllCampLoadListener;
import com.example.hbd.Interface.IAllStateLoadListener;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.CampModel;
import com.example.hbd.Model.HospitalModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.R;
import com.example.hbd.Report.ViewAppointmentsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewCampFragment extends Fragment implements IAllCampLoadListener {

    View v;
    @BindView(R.id.spinnercamp2)
    MaterialSpinner campspinner;
    @BindView(R.id.recyelercamp)
    RecyclerView camprecycle;




    Button searchBtn, refreshBtn;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser1;
    FirebaseAuth fAuth1;
    String userid;
    TextInputLayout camporg,campstate;

    CampAdapter campAdapter;
    List<CampModel> campModelList;

    Unbinder unbinder;
    IAllCampLoadListener iAllCampLoadListener;
    CollectionReference allcamp;


    public ViewCampFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iAllCampLoadListener = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_camp, container, false);
        unbinder = ButterKnife.bind(this,v);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth1 = FirebaseAuth.getInstance();
        firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
        camporg = v.findViewById(R.id.camporg2);
        campstate = v.findViewById(R.id.campstate2);
        searchBtn = v.findViewById(R.id.searchBtn2);
        refreshBtn = v.findViewById(R.id.refreshBtn2);

        userid = firebaseUser1.getUid();


        loaddetails();

        campModelList = new ArrayList<CampModel>();
        campAdapter = new CampAdapter(ViewCampFragment.this,campModelList);
        camprecycle.setAdapter(campAdapter);




        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventList();

            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment refresh = new ViewCampFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,refresh).commit();
            }
        });
        initView();

        return v;
    }

    private void EventList() {
        firebaseFirestore.collection("State").document(campstate.getEditText().getText().toString())
                .collection("Branch").document(camporg.getEditText().getText().toString())
                .collection("Camp").document(campspinner.getText().toString())
                .collection("Campaign")
                .whereEqualTo("campname", campspinner.getText().toString())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Error", error.getMessage());
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){



                            if(documentChange.getType() == DocumentChange.Type.ADDED){

                                campModelList.add(documentChange.getDocument().toObject(CampModel.class));
                            }
                            campAdapter.notifyDataSetChanged();




                        }


                    }
                });




    }

    private void loaddetails() {

        // authenticate user
        fAuth1 = FirebaseAuth.getInstance();
        firebaseUser1 = fAuth1.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userid = firebaseUser1.getUid();

        DocumentReference documentReference = firebaseFirestore.collection("User").document(userid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                campstate.getEditText().setText(value.getString("ostate"));
                camporg.getEditText().setText(value.getString("uhos"));

                allcamp = FirebaseFirestore.getInstance().collection("State").document(campstate.getEditText().getText().toString())
                        .collection("Branch").document(camporg.getEditText().getText().toString())
                        .collection("Camp");

                allcamp.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            List<String> list = new ArrayList<>();
                            list.add("Please Choose Camp");
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllCampLoadListener.onAllCampLoadSuccess(list);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iAllCampLoadListener.onAllCampLoadFailed(e.getMessage());
                    }
                });


            }
        });



    }


    private void initView() {

        camprecycle.setHasFixedSize(true);
        camprecycle.setLayoutManager(new GridLayoutManager(getActivity(),1));
        camprecycle.addItemDecoration(new SpacesItemDecoration(4));

    }


    @Override
    public void onAllCampLoadSuccess(List<String> campList) {
        campspinner.setItems(campList);
    }

    @Override
    public void onAllCampLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }
}