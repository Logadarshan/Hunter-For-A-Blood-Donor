package com.example.hbd.Slot;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hbd.Adapter.HospitalAdapter;
import com.example.hbd.Adapter.StaffSlotViewAdapter;
import com.example.hbd.Appointment.StaffAppointmentFragment;
import com.example.hbd.Interface.IAllCityLoadListener;
import com.example.hbd.Interface.IAllSlotLoadListener;
import com.example.hbd.Model.HospitalModel;
import com.example.hbd.Model.SlotModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SlotFragment extends Fragment implements IAllSlotLoadListener {


    @BindView(R.id.spinnerslotdate)
    MaterialSpinner slotdatespinner;
    @BindView(R.id.recyelerslottime)
    RecyclerView slottimerecycle;
    FloatingActionButton floatingActionButtonslot;
    IAllSlotLoadListener iAllSlotLoadListener, iAllSlotTimeLoadListener;
    Unbinder unbinder;
    CollectionReference allslot,allslottime;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    String userID, uhos,ustate,ostate;
    DatabaseReference databaseReference;
    ImageButton backBtn;
    View v;

    public SlotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iAllSlotLoadListener = this;
        iAllSlotTimeLoadListener = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_slot, container, false);
        unbinder = ButterKnife.bind(this,v);


        backBtn = v.findViewById(R.id.backBtn2);
        floatingActionButtonslot = v.findViewById(R.id.addslotfloatbutton);
        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment app = new StaffAppointmentFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,app).commit();
            }
        });



        floatingActionButtonslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment addslot = new AddSlotFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addslot).commit();
            }
        });



        loadSlotdate();
        initView();


        return v;
    }


    private void initView() {

        slottimerecycle.setHasFixedSize(true);
        slottimerecycle.setLayoutManager(new GridLayoutManager(getActivity(),1));
        slottimerecycle.addItemDecoration(new SpacesItemDecoration(4));

    }

    private void loadSlotdate() {


        String simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos = userModel.getUhos();
                    ustate = userModel.getUcstate();
                    ostate = userModel.getOstate();

                    allslot = FirebaseFirestore.getInstance()
                            .collection("State").document(ostate)
                            .collection("Branch").document(uhos)
                            .collection("Date");


                    Query query1 = allslot.whereGreaterThan("timestamp", simpleDateFormat2).limit(7);


                    query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful()){

                                List<String> list = new ArrayList<>();
                                list.add("Please Choose Date");
                                for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                                    list.add(documentSnapshot.getId());
                                iAllSlotLoadListener.onAllSlotLoadSuccess(list);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            iAllSlotLoadListener.onAllSlotLoadFailed(e.getMessage());
                        }
                    });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onAllSlotLoadSuccess(List<String> slotlist) {
        slotdatespinner.setItems(slotlist);
        slotdatespinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0){
                   loadSlotTime(item.toString());
                }
            }
        });

    }

    private void loadSlotTime(String slottime) {

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos = userModel.getUhos();
                    ustate = userModel.getUcstate();
                    ostate = userModel.getOstate();
                    Common.appointmentdate = slottime;

                    allslottime = FirebaseFirestore.getInstance()
                            .collection("State").document(ostate)
                            .collection("Branch").document(uhos)
                            .collection("Date").document(slottime)
                            .collection("Slot");


                    Query query = allslottime.orderBy("timestamp", Query.Direction.ASCENDING);

                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<SlotModel> list = new ArrayList<>();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                                SlotModel slotModel = queryDocumentSnapshot.toObject(SlotModel.class);
                                slotModel.setSlotid(queryDocumentSnapshot.getId());
                                list.add(slotModel);
                            }

                            iAllSlotTimeLoadListener.onAllSlotTimeLoadSuccess(list);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            iAllSlotTimeLoadListener.onAllSlotTimeLoadFailed(e.getMessage());
                        }
                    });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    @Override
    public void onAllSlotLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAllSlotTimeLoadSuccess(List<SlotModel> slotModelList) {

       StaffSlotViewAdapter staffSlotViewAdapter = new StaffSlotViewAdapter(getActivity(), slotModelList);
       slottimerecycle.setAdapter(staffSlotViewAdapter);


    }

    @Override
    public void onAllSlotTimeLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }

}