package com.example.hbd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Appointment.StaffAllAppointmentFragment;
import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.R;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>{

    StaffAllAppointmentFragment context;
    List<AppointmentModel> appointmentModelList;

    public AppointmentAdapter(StaffAllAppointmentFragment context, List<AppointmentModel> appointmentModelList) {
        this.context = context;
        this.appointmentModelList = appointmentModelList;
    }

    @NonNull
    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_appointments,parent,false);

        return new AppointmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.ViewHolder holder, int position) {

        AppointmentModel appointmentModel = appointmentModelList.get(position);
        holder.appname12.setText(appointmentModel.getUsernameappoint());
        holder.appdate12.setText(appointmentModel.getDataappointment());
        holder.apptime12.setText(appointmentModel.getTimeappointment());
        holder.apploc12.setText(appointmentModel.getHospappointment());
        holder.appstat12.setText(appointmentModel.getStatusappointment());

    }

    @Override
    public int getItemCount() {
        return appointmentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appname12, apptime12, appdate12, apploc12, appstat12;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appname12 = itemView.findViewById(R.id.staffhosnameapp12);
            apptime12 = itemView.findViewById(R.id.staffhostimeapp12);
            appdate12 = itemView.findViewById(R.id.staffhosdateapp12);
            apploc12 = itemView.findViewById(R.id.staffhoslocation12);
            appstat12 = itemView.findViewById(R.id.staffhosstatus12);


        }
    }
}
