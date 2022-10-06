package com.example.hbd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Profile.ViewHistoryFragment;
import com.example.hbd.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    ViewHistoryFragment context;
    List<AppointmentModel> appointmentModelList;


    public HistoryAdapter(ViewHistoryFragment context, List<AppointmentModel> appointmentModelList) {
        this.context = context;
        this.appointmentModelList = appointmentModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history,parent,false);

        return new HistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppointmentModel appointmentModel = appointmentModelList.get(position);
        holder.appname.setText(appointmentModel.getUsernameappoint());
        holder.appdate.setText(appointmentModel.getDataappointment());
        holder.apptime.setText(appointmentModel.getTimeappointment());
        holder.appstatus.setText(appointmentModel.getStatusappointment());
        holder.apploc.setText(appointmentModel.getHospappointment());





    }

    @Override
    public int getItemCount() {
        return appointmentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appname,apptime,appdate,appstatus,apploc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appname = itemView.findViewById(R.id.hosnameapp1);
            apptime = itemView.findViewById(R.id.hostimeapp1);
            appdate = itemView.findViewById(R.id.hosdateapp1);
            appstatus = itemView.findViewById(R.id.hosstatus1);
            apploc = itemView.findViewById(R.id.hoslocation1);


        }
    }
}
