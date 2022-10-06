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
import com.example.hbd.Report.ViewAppointmentsFragment;

import java.util.List;

public class AppointmentAdminViewAdapter extends RecyclerView.Adapter<AppointmentAdminViewAdapter.ViewHolder>{

    ViewAppointmentsFragment context;
    List<AppointmentModel> appointmentModelList;

    public AppointmentAdminViewAdapter(ViewAppointmentsFragment context, List<AppointmentModel> appointmentModelList) {
        this.context = context;
        this.appointmentModelList = appointmentModelList;
    }



    @NonNull
    @Override
    public AppointmentAdminViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_appointmentsadminview,parent,false);

        return new AppointmentAdminViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdminViewAdapter.ViewHolder holder, int position) {

        AppointmentModel appointmentModel = appointmentModelList.get(position);
        holder.donorname.setText(appointmentModel.getUsernameappoint());
        holder.donortime.setText(appointmentModel.getTimeappointment());
        holder.donorblood.setText(appointmentModel.getUserblood());
        holder.donorstatus.setText(appointmentModel.getStatusappointment());



    }

    @Override
    public int getItemCount() {
        return appointmentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView donorname, donortime, donorstatus, donorblood;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            donorname = itemView.findViewById(R.id.nameofdonor);
            donortime = itemView.findViewById(R.id.timeofdonor);
            donorstatus = itemView.findViewById(R.id.statusofdonor);
            donorblood= itemView.findViewById(R.id.bloodofdonor);

        }
    }
}
