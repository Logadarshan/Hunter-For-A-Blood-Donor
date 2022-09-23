package com.example.hbd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hbd.Interface.IAllRecyclerLoadListener;
import com.example.hbd.Model.HospitalModel;
import com.example.hbd.Others.Common;
import com.example.hbd.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder> {


    Context context;
    List<HospitalModel> hospitalModelList;
    List<CardView> cardViews;
    LocalBroadcastManager localBroadcastManager;


    public HospitalAdapter(Context context, List<HospitalModel> hospitalModelList) {
        this.context = context;
        this.hospitalModelList = hospitalModelList;
        cardViews = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public HospitalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_hospital, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter.MyViewHolder holder, int position) {

        HospitalModel hospitalModel = hospitalModelList.get(position);
        holder.hosname.setText(hospitalModelList.get(position).getName());
        holder.hosadd.setText(hospitalModelList.get(position).getAddress());
        Glide.with(holder.hosimga.getContext()).load(hospitalModel.getImage()).into(holder.hosimga);

        //display news images

        String imageUri = null;
        imageUri = hospitalModel.getImage();
        Picasso.get().load(imageUri).into(holder.hosimga);


        if(!cardViews.contains(holder.card_hos))
            cardViews.add(holder.card_hos);

        holder.setiAllDateLoadListener(new IAllRecyclerLoadListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {

                for(CardView cardView:cardViews)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                holder.card_hos.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));


                Intent intent = new Intent(Common.KEY_ENABLE_NEXT);
                intent.putExtra(Common.KEY_HOSPITAL_STORE, hospitalModelList.get(pos));
                intent.putExtra(Common.KEY_STEP, 1);
                localBroadcastManager.sendBroadcast(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return hospitalModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hosname, hosadd;
        CardView card_hos;
        ZoomageView hosimga;

        IAllRecyclerLoadListener iAllRecyclerLoadListener;

        public void setiAllDateLoadListener(IAllRecyclerLoadListener iAllRecyclerLoadListener) {
            this.iAllRecyclerLoadListener = iAllRecyclerLoadListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hosname = itemView.findViewById(R.id.hosname);
            hosadd = itemView.findViewById(R.id.hosadd);
            card_hos = itemView.findViewById(R.id.cardview);
            hosimga = itemView.findViewById(R.id.hosimg);

            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
          iAllRecyclerLoadListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
