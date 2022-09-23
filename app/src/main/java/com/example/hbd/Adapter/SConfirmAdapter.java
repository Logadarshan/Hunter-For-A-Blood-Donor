package com.example.hbd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Appointment.StaffConfirmedAppointmentFragment;
import com.example.hbd.Appointment.StaffUpcomingAppointmentFragment;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SConfirmAdapter extends RecyclerView.Adapter<SConfirmAdapter.ViewHolder> {

   StaffConfirmedAppointmentFragment context;
    List<AppointmentModel> appointmentModelList;



    public SConfirmAdapter(StaffConfirmedAppointmentFragment context, List<AppointmentModel> appointmentModelList) {
        this.context = context;
        this.appointmentModelList = appointmentModelList;
    }


    @NonNull
    @Override
    public SConfirmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_confirmedappointments,parent,false);

        return new SConfirmAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SConfirmAdapter.ViewHolder holder, int position) {

        AppointmentModel appointmentModel = appointmentModelList.get(position);
        holder.appname1.setText(appointmentModel.getUsernameappoint());
        holder.appdate1.setText(appointmentModel.getDataappointment());
        holder.apptime1.setText(appointmentModel.getTimeappointment());
        holder.apploc1.setText(appointmentModel.getHospappointment());
        holder.appstat1.setText(appointmentModel.getStatusappointment());


        FirebaseFirestore firebaseFirestore;
        firebaseFirestore = FirebaseFirestore.getInstance();

        holder.completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<>();
                map.put("statusappointment", "Completed");


                firebaseFirestore.collection("Appointment")
                        .whereEqualTo("appointmentid", appointmentModel.getAppointmentid())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();

                            firebaseFirestore.collection("Appointment")
                                    .document(documentID).update(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.appname1.getContext(), "Completed", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                    }
                });




            }
        });




        holder.incompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map1 = new HashMap<>();
                map1.put("statusappointment", "Incompleted");


                firebaseFirestore.collection("Appointment")
                        .whereEqualTo("appointmentid", appointmentModel.getAppointmentid())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();

                            firebaseFirestore.collection("Appointment")
                                    .document(documentID).update(map1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.appname1.getContext(), "Incomplete", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                    }
                });

            }
        });




    }

    @Override
    public int getItemCount() {
        return appointmentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView appname1, apptime1, appdate1, apploc1, appstat1;
        Button completeBtn, incompleteBtn ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            appname1 = itemView.findViewById(R.id.staffhosnameapp111);
            apptime1 = itemView.findViewById(R.id.staffhostimeapp111);
            appdate1 = itemView.findViewById(R.id.staffhosdateapp111);
            apploc1 = itemView.findViewById(R.id.staffhoslocation111);
            appstat1 = itemView.findViewById(R.id.staffhosstatus111);
            completeBtn = itemView.findViewById(R.id.staffcompleteBtn);
            incompleteBtn = itemView.findViewById(R.id.staffincompletetBtn);


        }
    }
}
