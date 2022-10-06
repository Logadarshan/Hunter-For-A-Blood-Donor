package com.example.hbd.Report;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hbd.Adapter.AppointmentAdminViewAdapter;
import com.example.hbd.Adapter.UserAdminViewAdapter;
import com.example.hbd.Interface.IAllDateLoadListener;
import com.example.hbd.Interface.IAllHosLoadListener;
import com.example.hbd.Interface.IAllStateLoadListener;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ViewAppointmentsFragment extends Fragment implements IAllStateLoadListener, IAllHosLoadListener, IAllDateLoadListener {

    View v;
    @BindView(R.id.spinnerstate2)
    MaterialSpinner statespinner;
    @BindView(R.id.spinnerhos2)
    MaterialSpinner hosspinner;
    @BindView(R.id.spinnerdate2)
    MaterialSpinner datespinner;
    @BindView(R.id.recyelerapps)
    RecyclerView appsrecycle;

    CollectionReference allcity,allbranch,alldate;
    Unbinder unbinder;

    IAllStateLoadListener iAllStateLoadListener;
    IAllHosLoadListener iAllHosLoadListener;
    IAllDateLoadListener iAllDateLoadListener;

    AppointmentAdminViewAdapter appointmentAdminViewAdapter;
    List<AppointmentModel> appointmentModelList;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser1;
    FirebaseAuth fAuth1;
    String userID;

    Button searchBtn, refreshBtn;

    public ViewAppointmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allcity = FirebaseFirestore.getInstance().collection("State");
        iAllStateLoadListener = this;
        iAllHosLoadListener = this;
        iAllDateLoadListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_appointments, container, false);
        unbinder = ButterKnife.bind(this,v);


        searchBtn = v.findViewById(R.id.searchBtn1);
        refreshBtn = v.findViewById(R.id.refreshBtn1);



        appointmentModelList = new ArrayList<AppointmentModel>();
        appointmentAdminViewAdapter = new AppointmentAdminViewAdapter(ViewAppointmentsFragment.this,appointmentModelList);
        appsrecycle.setAdapter(appointmentAdminViewAdapter);


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
                Fragment refresh = new ViewAppointmentsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,refresh).commit();
            }
        });




        initView();
        loadAllCity();




        return v;
    }

    private void EventList() {

        firebaseFirestore.collection("Appointment")
                .whereEqualTo("hospappointment", hosspinner.getText().toString())
                .whereEqualTo("dataappointment", datespinner.getText().toString())
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
                            appointmentAdminViewAdapter.notifyDataSetChanged();




                        }


                    }
                });





    }


    private void initView() {

        appsrecycle.setHasFixedSize(true);
        appsrecycle.setLayoutManager(new GridLayoutManager(getActivity(),1));
        appsrecycle.addItemDecoration(new SpacesItemDecoration(4));


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
        hosspinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0){
                    loadDate(item.toString());
                }
            }
        });
    }

    private void loadDate(String hospitalid) {

        Common.appointmenthospital = hospitalid;

        alldate= FirebaseFirestore.getInstance()
                .collection("State").document(Common.city)
                .collection("Branch").document(hospitalid)
                .collection("Date");



        alldate.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> list = new ArrayList<>();
                list.add("Please Choose Date");
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                        list.add(documentSnapshot.getId());
                    iAllDateLoadListener.onAllDateLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllDateLoadListener.onAllDateLoadFailed(e.getMessage());
            }
        });



    }

    @Override
    public void onAllHosLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAllDateLoadSuccess(List<String> dateList) {
        datespinner.setItems(dateList);
    }

    @Override
    public void onAllDateLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }
}