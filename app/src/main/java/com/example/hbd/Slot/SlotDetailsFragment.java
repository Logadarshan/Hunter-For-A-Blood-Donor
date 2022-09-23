package com.example.hbd.Slot;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbd.Model.NewsModel;
import com.example.hbd.Model.SlotModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.Others.Common;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SlotDetailsFragment extends Fragment {

    String time,capacity,slotid,timestamp,date;
    SlotModel slotModel;
    Button updateBtn, deleteBtn;

    EditText stime,scap,sdate;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    CollectionReference documentReference;
    String userID, uhos,ustate,ostate;
    DatabaseReference databaseReference;

    public SlotDetailsFragment() {
        // Required empty public constructor
    }


    public SlotDetailsFragment(String time, String capacity, String slotid, String timestamp,String date) {
        this.time = time;
        this.capacity = capacity;
        this.slotid = slotid;
        this.timestamp = timestamp;
        this.date = date;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slot_details, container, false);


        stime = v.findViewById(R.id.slottime9);
        scap = v.findViewById(R.id.slotcapacity9);
        sdate = v.findViewById(R.id.slotdate9);
        updateBtn = v.findViewById(R.id.updateslotBtn1);
        deleteBtn = v.findViewById(R.id.deleteslotBtn);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();



        sdate.setText(date);
        stime.setText(time);
        scap.setText(capacity);



        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateslot();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteslot();
            }
        });




        return v;
    }

    private void deleteslot() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos = userModel.getUhos();
                    ustate = userModel.getUcstate();
                    ostate = userModel.getOstate();

                    String slotdate1= sdate.getText().toString();

                    documentReference= FirebaseFirestore.getInstance().
                            collection("State").document(ostate)
                            .collection("Branch").document(uhos)
                            .collection("Date").document(slotdate1)
                            .collection("Slot");

                    documentReference.document(slotid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Fragment viewslot = new SlotFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.container,viewslot).commit();
                            Toast.makeText(getActivity(), "Slot deleted successfully", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"User Slot Deleted");
                        }
                    });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateslot() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos = userModel.getUhos();
                    ustate = userModel.getUcstate();
                    ostate = userModel.getOstate();

                    String slotcap= scap.getText().toString();
                    String slotdate1= sdate.getText().toString();

                    Map<String,Object> map1 = new HashMap<>();
                    map1.put("capacity", slotcap);
                    documentReference= FirebaseFirestore.getInstance().
                            collection("State").document(ostate)
                            .collection("Branch").document(uhos)
                            .collection("Date").document(slotdate1)
                            .collection("Slot");

                    documentReference.document(slotid).update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Fragment viewslot = new SlotFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.container,viewslot).commit();
                            Toast.makeText(getActivity(), "Slot updated successfully", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"User Slot Updated");
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