package com.example.hbd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Camp.ViewCampFragment;
import com.example.hbd.Model.CampModel;
import com.example.hbd.Model.HospitalModel;
import com.example.hbd.R;

import java.util.List;

public class CampAdapter extends RecyclerView.Adapter<CampAdapter.ViewHolder>{

    ViewCampFragment context;
    List<CampModel> campModelList;

    public CampAdapter(ViewCampFragment context, List<CampModel> campModelList) {
        this.context = context;
        this.campModelList = campModelList;
    }

    @NonNull
    @Override
    public CampAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_campdonor, parent, false);


        return new CampAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CampAdapter.ViewHolder holder, int position) {

        CampModel campModel = campModelList.get(position);
        holder.campdname.setText(campModelList.get(position).getCampdonorname());
        holder.campdage.setText(campModelList.get(position).getCampdonorage());
        holder.campdblood.setText(campModelList.get(position).getCampdonorblood());
        holder.campdnum.setText(campModelList.get(position).getCampdonornum());



    }

    @Override
    public int getItemCount() {
        return campModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView campdname, campdage, campdblood, campdnum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            campdname  = itemView.findViewById(R.id.campdonorname1);
            campdage   = itemView.findViewById(R.id.campdonorage1);
            campdnum   = itemView.findViewById(R.id.campdonorcontact1);
            campdblood = itemView.findViewById(R.id.campdonorblood1);



        }
    }
}
