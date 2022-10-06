package com.example.hbd.Report;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hbd.Adapter.HospitalAdapter;
import com.example.hbd.Interface.IAllCityLoadListener;
import com.example.hbd.Model.HospitalModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.Profile.ViewHistoryFragment;
import com.example.hbd.R;
import com.example.hbd.Users.AddStaffFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HospitalFragment extends Fragment implements IAllCityLoadListener {

    CollectionReference allcity;
    CollectionReference allhos;

    IAllCityLoadListener iAllCityLoadListener,iAllHospLoadListener;


    @BindView(R.id.spinnerstate)
    MaterialSpinner statespinner;
    @BindView(R.id.recyelerhos)
    RecyclerView cityrecycle;
    FloatingActionButton floatingActionButton4;
    Unbinder unbinder;


    View v;

    public HospitalFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allcity = FirebaseFirestore.getInstance().collection("State");
        iAllCityLoadListener = this;
        iAllHospLoadListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_hospital, container, false);
        unbinder = ButterKnife.bind(this,v);

        floatingActionButton4 = v.findViewById(R.id.addfloatbutton3);

        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addhos = new AddHospitalFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addhos).commit();
            }
        });





        initView();
        loadAllCity();

        return v;
    }


    private void initView() {

        cityrecycle.setHasFixedSize(true);
        cityrecycle.setLayoutManager(new GridLayoutManager(getActivity(),1));
        cityrecycle.addItemDecoration(new SpacesItemDecoration(4));


    }

    private void loadAllCity() {
        allcity.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    List<String> list = new ArrayList<>();
                    list.add("Please Choose City");
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                        list.add(documentSnapshot.getId());
                    iAllCityLoadListener.onAllCityLoadSuccess(list);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllCityLoadListener.onAllCityLoadFailed(e.getMessage());
            }
        });



    }

    @Override
    public void onAllCityLoadSuccess(List<String> cityList) {
        statespinner.setItems(cityList);
        statespinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0){
                    loadHosp(item.toString());
                }
            }
        });
    }

    private void loadHosp(String cityname) {


        Common.city = cityname;

        allhos= FirebaseFirestore.getInstance()
                .collection("State").document(cityname)
                .collection("Branch");


        allhos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<HospitalModel> list = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                    {
                        HospitalModel hospitalModel = documentSnapshot.toObject(HospitalModel.class);
                        hospitalModel.setHospitalid(documentSnapshot.getId());
                        list.add(hospitalModel);
                    }
                    iAllHospLoadListener.onAllHospLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllHospLoadListener.onAllHospLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public void onAllCityLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAllHospLoadSuccess(List<HospitalModel> hospitalList) {

        HospitalAdapter hospitalAdapter = new HospitalAdapter(getActivity(), hospitalList);
        cityrecycle.setAdapter(hospitalAdapter);

    }

    @Override
    public void onAllHospLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }




}