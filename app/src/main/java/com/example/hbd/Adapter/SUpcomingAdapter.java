package com.example.hbd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Appointment.StaffUpcomingAppointmentFragment;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.SlotModel;
import com.example.hbd.Others.Common;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SUpcomingAdapter extends RecyclerView.Adapter<SUpcomingAdapter.ViewHolder> {

    StaffUpcomingAppointmentFragment context;
    List<AppointmentModel> appointmentModelList;
    


    public SUpcomingAdapter(StaffUpcomingAppointmentFragment context, List<AppointmentModel> appointmentModelList) {
        this.context = context;
        this.appointmentModelList = appointmentModelList;
    }


    @NonNull
    @Override
    public SUpcomingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_upcomingappointments,parent,false);

        return new SUpcomingAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SUpcomingAdapter.ViewHolder holder, int position) {

        AppointmentModel appointmentModel = appointmentModelList.get(position);
        holder.appname.setText(appointmentModel.getUsernameappoint());
        holder.appdate.setText(appointmentModel.getDataappointment());
        holder.apptime.setText(appointmentModel.getTimeappointment());
        holder.apploc.setText(appointmentModel.getHospappointment());
        holder.appstat.setText(appointmentModel.getStatusappointment());
        holder.appcity.setText(appointmentModel.getApploc());
        holder.appslotid.setText(appointmentModel.getAppslotid());
        holder.appbal.setText(appointmentModel.getBalslot());


        FirebaseFirestore firebaseFirestore;
        firebaseFirestore = FirebaseFirestore.getInstance();
        
        holder.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<>();
                map.put("statusappointment", "Approved");


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
                                                    Toast.makeText(holder.appslotid.getContext(), "Approved", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            }
                        });
            }
        });

        SlotModel slotModel = null;

        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map1 = new HashMap<>();
                map1.put("statusappointment", "Rejected");


                firebaseFirestore.collection("Appointment")
                        .whereEqualTo("appointmentid", appointmentModel.getAppointmentid())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){


                            CollectionReference documentReference1 =firebaseFirestore
                                    .collection("State").document(holder.appcity.getText().toString())
                                    .collection("Branch").document(holder.apploc.getText().toString())
                                    .collection("Date").document(holder.appdate.getText().toString())
                                    .collection("Slot");


                            long value = Long.parseLong(holder.appbal.getText().toString());

                            Map<String,Object> map = new HashMap<>();
                            map.put("capacity" , String.valueOf(value+1));

                            documentReference1.document(holder.appslotid.getText().toString()).update(map);


                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();

                            firebaseFirestore.collection("Appointment")
                                    .document(documentID).update(map1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.appslotid.getContext(), "Rejected", Toast.LENGTH_SHORT).show();
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


        TextView appname, apptime, appdate, apploc, appstat;
        Button approveBtn, rejectBtn ;
        TextView appcity,appslotid,appbal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            appname = itemView.findViewById(R.id.staffhosnameapp1);
            apptime = itemView.findViewById(R.id.staffhostimeapp1);
            appdate = itemView.findViewById(R.id.staffhosdateapp1);
            apploc = itemView.findViewById(R.id.staffhoslocation11);
            appstat = itemView.findViewById(R.id.staffhosstatus11);
            appcity = itemView.findViewById(R.id.staffhoscity11);
            appslotid = itemView.findViewById(R.id.staffhosslotid11);
            appbal = itemView.findViewById(R.id.staffhosbal11);


            approveBtn = itemView.findViewById(R.id.staffapproveBtn1);
            rejectBtn = itemView.findViewById(R.id.staffrejectBtn1);


        }
    }
}
