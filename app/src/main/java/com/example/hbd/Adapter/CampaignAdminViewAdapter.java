package com.example.hbd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Model.AppointmentModel;
import com.example.hbd.Model.CampModel;
import com.example.hbd.R;
import com.example.hbd.Report.ViewAppointmentsFragment;
import com.example.hbd.Report.ViewCampaignFragment;

import java.util.List;

public class CampaignAdminViewAdapter extends RecyclerView.Adapter<CampaignAdminViewAdapter.ViewHolder>{

    ViewCampaignFragment context;
    List<CampModel> campModelList;

    public CampaignAdminViewAdapter(ViewCampaignFragment context, List<CampModel> campModelList) {
        this.context = context;
        this.campModelList = campModelList;
    }

    @NonNull
    @Override
    public CampaignAdminViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_campdonor,parent,false);

        return new CampaignAdminViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignAdminViewAdapter.ViewHolder holder, int position) {

        CampModel campModel = campModelList.get(position);

        holder.donorname.setText(campModel.getCampdonorname());
        holder.donorage.setText(campModel.getCampdonorage());
        holder.donornum.setText(campModel.getCampdonornum());
        holder.donorblood.setText(campModel.getCampdonorblood());

    }

    @Override
    public int getItemCount() {
        return campModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView donorname, donorage, donornum, donorblood;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            donorname = itemView.findViewById(R.id.campdonorname1);
            donorage = itemView.findViewById(R.id.campdonorage1);
            donornum = itemView.findViewById(R.id.campdonorcontact1);
            donorblood= itemView.findViewById(R.id.campdonorblood1);


        }
    }
}
