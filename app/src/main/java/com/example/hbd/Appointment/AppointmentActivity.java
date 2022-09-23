package com.example.hbd.Appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.hbd.Adapter.PageAdapter;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.SlotModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.NonSwipeViewPager;
import com.example.hbd.R;
import com.example.hbd.Signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointmentActivity extends AppCompatActivity {

    @BindView(R.id.btn_previous_next)
    Button btnnext;
    @BindView(R.id.btn_previous_step)
    Button btnpre;
    @BindView(R.id.stepview)
    StepView stepView;
    @BindView(R.id.idviewpager)
    NonSwipeViewPager viewPager;

    LocalBroadcastManager localBroadcastManager;

    CollectionReference alldate,alltime;
    FirebaseFirestore firebaseFirestore;
    String[] currenttimedate;

    @OnClick(R.id.btn_previous_step)
    void preClick(){
        if(Common.step == 3 || Common.step > 0) {

            Common.step--;
            viewPager.setCurrentItem(Common.step);
            if(Common.step <3){
                btnnext.setEnabled(true);
                setColorButton();
            }
        }
    }



    @OnClick(R.id.btn_previous_next)
    void nextClick(){

        if(Common.step < 3 || Common.step == 0) {

            Common.step++;
            if(Common.step == 1){
                if(Common.currentHosp!= null){
                    loadDate(Common.currentHosp.getHospitalid());
                }
            }
            else if (Common.step == 2){
                if (Common.currentdate!= null){
                    loadTime(Common.currentdate.getSlotid());
                }
            }
            else if (Common.step == 3){
                if (Common.currentdate!= null){
                    ComfirmAppointment();
                }
            }
            viewPager.setCurrentItem(Common.step);

        }
    }

    private void ComfirmAppointment() {

        Intent intent = new Intent(Common.KEY_CONFIRM_APPOINTMENT);
        localBroadcastManager.sendBroadcast(intent);


    }


    private void loadTime(String slotid) {

        Query query;
        Common.appointmentdate = slotid ;
        String simpleDateFormat3 = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());

        if(!TextUtils.isEmpty(Common.appointmenthospital)){

            alltime = FirebaseFirestore.getInstance()
                    .collection("State").document(Common.city)
                    .collection("Branch").document(Common.appointmenthospital)
                    .collection("Date").document(slotid)
                    .collection("Slot");



                query = alltime
                        .whereGreaterThanOrEqualTo("timestamp", simpleDateFormat3);



            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<SlotModel> time = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                        SlotModel slotModel = queryDocumentSnapshot.toObject(SlotModel.class);
                        slotModel.setSlotid(queryDocumentSnapshot.getId());

                        time.add(slotModel);
                    }

                    Intent intent = new Intent(Common.KEY_TIME_LOAD_DONE);
                    intent.putParcelableArrayListExtra(Common.KEY_TIME_LOAD_DONE, time);
                    localBroadcastManager.sendBroadcast(intent);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }



    }

    private void loadDate(String hospitalid) {


        Common.appointmenthospital = hospitalid;

        String simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());



        if(!TextUtils.isEmpty(Common.city)){

            alldate = (CollectionReference) FirebaseFirestore.getInstance()
                    .collection("State").document(Common.city)
                    .collection("Branch").document(hospitalid)
                    .collection("Date");

            Query query =alldate
                    .whereGreaterThan("timestamp", simpleDateFormat2)
                    .limit(7);


            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<SlotModel> date = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        SlotModel slotModel = queryDocumentSnapshot.toObject(SlotModel.class);
                        slotModel.setSlotid(queryDocumentSnapshot.getId());

                        date.add(slotModel);
                    }

                    Intent intent = new Intent(Common.KEY_DATE_LOAD_DONE);
                    intent.putParcelableArrayListExtra(Common.KEY_DATE_LOAD_DONE, date);
                    localBroadcastManager.sendBroadcast(intent);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }




    }


    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP,0 );

            if (step ==1 ){
                Common.currentHosp = intent.getParcelableExtra(Common.KEY_HOSPITAL_STORE);
            }
            else if (step == 2){
                Common.currentdate = intent.getParcelableExtra(Common.KEY_DATE_SELECTD);
            }
            else if (step == 3){
                Common.currentdate = intent.getParcelableExtra(Common.KEY_TIME_SELECTD);
            }



            Common.currentHosp = intent.getParcelableExtra(Common.KEY_HOSPITAL_STORE);
            btnnext.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        ButterKnife.bind(AppointmentActivity.this);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_NEXT));


        setupStepView();
        setColorButton();


        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                stepView.go(position,true);
                if(position == 0){
                    btnpre.setEnabled(false);
                }

                else
                {
                    btnpre.setEnabled(true);
                }

                btnnext.setEnabled(false);
                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }



    private void setColorButton() {

        if(btnnext.isEnabled())
        {
            btnnext.setBackgroundResource(R.color.purple_500);
        }
        else
        {
            btnnext.setBackgroundResource(R.color.teal_200);
        }
        if(btnpre.isEnabled())
        {
            btnpre.setBackgroundResource(R.color.purple_500);
        }
        else
        {
            btnpre.setBackgroundResource(R.color.teal_200);
        }

    }



    private void setupStepView() {
        List<String> steplist = new ArrayList<>();
        steplist.add("Location");
        steplist.add("Date");
        steplist.add("Time");
        steplist.add("Confirm");
        stepView.setSteps(steplist);

    }

}