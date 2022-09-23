package com.example.hbd.Slot;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.News.NewsfeedFragment;
import com.example.hbd.Others.Common;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddSlotFragment extends Fragment {

    EditText slotdates, slottimes,slottimes1,slotcap;
    DatePickerDialog datePicker;
    TimePickerDialog timePickerDialog;
    Button addslot;
    ImageButton backbtn;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;
    CollectionReference documentReference, documentReference1;

    DatabaseReference databaseReference;
    TextView select;
    String userID, uhos,ustate,ostate;
    Date date = null, date2 =null;
    Timestamp ts = null;
    SimpleDateFormat formatter;
    SimpleDateFormat simpleDateFormat1;


    View v;

    public AddSlotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_add_slot, container, false);


        slotdates = v.findViewById(R.id.slotdate);
        slottimes = v.findViewById(R.id.slottime);
        slotcap = v.findViewById(R.id.slotcapacity);
        slottimes1 = v.findViewById(R.id.slottime1);
        addslot = v.findViewById(R.id.addslot);
        backbtn = v.findViewById(R.id.backBtn1);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment slot = new SlotFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,slot).commit();
            }
        });




        slotdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String cur = (month+1) + "-" + dayOfMonth + "-" + year ;

                        simpleDateFormat1 = new SimpleDateFormat("MM-dd-yyyy");


                        try {
                            date = simpleDateFormat1.parse(cur);
                            ts =new Timestamp(date.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        formatter = new SimpleDateFormat("yyyyMMdd");
                        slotdates.setText(simpleDateFormat1.format(date));
                       // slottimes1.setText(formatter.format(ts));


                    }
                },year,month,day);
                datePicker.show();
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());


            }
        });


        slottimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar1 = Calendar.getInstance();
                int hour= calendar1.get(Calendar.HOUR_OF_DAY);
                int minute = calendar1.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



                        slottimes.setText( String.format("%02d%02d",hourOfDay,minute));
                        slottimes1.setText(formatter.format(ts)+String.format("%02d%02d",hourOfDay,minute));


                    }
                },hour,minute,true);

                timePickerDialog.show();

            }
        });




        addslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addslots();
            }
        });





        return v;
    }



    private void addslots() {


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    uhos = userModel.getUhos();
                    ustate = userModel.getUcstate();
                    ostate = userModel.getOstate();

                    String slotd= slotdates.getText().toString();
                    String slott = slottimes.getText().toString();
                    String slottimestamp = slottimes1.getText().toString();
                    String slotc = slotcap.getText().toString();

                    Common.appointments = Common.generateString(6);


                    Map<String,Object> map = new HashMap<>();
                    map.put("slotid" , Common.appointments);
                    map.put("date" , slotd);
                    map.put("time", slott);
                    map.put("timestamp",slottimestamp);
                    map.put("capacity", slotc);


                    documentReference = FirebaseFirestore.getInstance()
                            .collection("State").document(ostate)
                            .collection("Branch").document(uhos)
                            .collection("Date").document(slotd)
                            .collection("Slot");

                    documentReference.document(Common.appointments).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG,"User Slot Created");
                        }
                    });

                    Map<String,Object> map1 = new HashMap<>();
                    map1.put("date" , slotd);
                    map1.put("timestamp",formatter.format(ts));

                    documentReference1 = FirebaseFirestore.getInstance()
                            .collection("State").document(ostate)
                            .collection("Branch").document(uhos)
                            .collection("Date");

                    documentReference1.document(slotd).set(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG,"User Slot Created");
                        }
                    });

                }

                Toast.makeText(getActivity(), "Slot added successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}