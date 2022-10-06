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
import android.widget.Button;
import android.widget.Toast;

import com.example.hbd.Adapter.AppointmentAdminViewAdapter;
import com.example.hbd.Adapter.CampaignAdminViewAdapter;
import com.example.hbd.Interface.IAllCampLoadListener;
import com.example.hbd.Interface.IAllDateLoadListener;
import com.example.hbd.Interface.IAllHosLoadListener;
import com.example.hbd.Interface.IAllStateLoadListener;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.CampModel;
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

public class ViewCampaignFragment extends Fragment implements IAllStateLoadListener, IAllHosLoadListener, IAllCampLoadListener {

    View v;
    @BindView(R.id.spinnerstate6)
    MaterialSpinner statespinner;
    @BindView(R.id.spinnerhos6)
    MaterialSpinner hosspinner;
    @BindView(R.id.spinnercamp6)
    MaterialSpinner campspinner;
    @BindView(R.id.recyelerapps2)
    RecyclerView appsrecycle;

    Button searchBtn,refreshBtn;

    CollectionReference allcity,allbranch,alldate;
    Unbinder unbinder;

    IAllStateLoadListener iAllStateLoadListener;
    IAllHosLoadListener iAllHosLoadListener;
    IAllCampLoadListener iAllCampLoadListener;

    CampaignAdminViewAdapter campaignAdminViewAdapter;
    List<CampModel> campModelList;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser1;
    FirebaseAuth fAuth1;
    String userID;



    public ViewCampaignFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allcity = FirebaseFirestore.getInstance().collection("State");
        iAllStateLoadListener = this;
        iAllHosLoadListener = this;
        iAllCampLoadListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       v =  inflater.inflate(R.layout.fragment_view_campaign, container, false);
       unbinder = ButterKnife.bind(this,v);

        searchBtn = v.findViewById(R.id.searchBtn6);
        refreshBtn = v.findViewById(R.id.refreshBtn6);


        campModelList= new ArrayList<CampModel>();
        campaignAdminViewAdapter = new CampaignAdminViewAdapter(ViewCampaignFragment.this,campModelList);
        appsrecycle.setAdapter(campaignAdminViewAdapter);


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
                Fragment refresh = new ViewCampaignFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,refresh).commit();
            }
        });




        initView();
       loadAllCity();

       return v;
    }

    private void EventList() {

        firebaseFirestore.collection("State").document(statespinner.getText().toString())
                .collection("Branch").document(hosspinner.getText().toString())
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
                            campaignAdminViewAdapter.notifyDataSetChanged();




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
                .collection("Camp");



        alldate.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> list = new ArrayList<>();
                list.add("Please Choose Camp");
                if(task.isSuccessful()){
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

    @Override
    public void onAllHosLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
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