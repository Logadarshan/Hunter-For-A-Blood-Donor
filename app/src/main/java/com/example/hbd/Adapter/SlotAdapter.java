package com.example.hbd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Interface.IAllRecyclerLoadListener;
import com.example.hbd.Model.SlotModel;
import com.example.hbd.Others.Common;
import com.example.hbd.R;

import java.util.ArrayList;
import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.MyViewHolder> {

    Context context;
    List<SlotModel> slotModelList;
    List<CardView> cardViews;
    LocalBroadcastManager localBroadcastManager;

    public SlotAdapter(Context context, List<SlotModel> slotModelList) {
        this.context = context;
        this.slotModelList = slotModelList;
        cardViews = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public SlotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_date, parent, false);


        return new SlotAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotAdapter.MyViewHolder holder, int position) {

        holder.date.setText(slotModelList.get(position).getDate());

        if(!cardViews.contains(holder.carddate))
            cardViews.add(holder.carddate);

        holder.setiAllDateLoadListener(new IAllRecyclerLoadListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {

                for(CardView cardView:cardViews)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                holder.carddate.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));


                Intent intent = new Intent(Common.KEY_ENABLE_NEXT);
                intent.putExtra(Common.KEY_DATE_SELECTD, slotModelList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return slotModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date,ava;
        CardView carddate;

        IAllRecyclerLoadListener iAllRecyclerLoadListener;

        public void setiAllDateLoadListener(IAllRecyclerLoadListener iAllRecyclerLoadListener) {
            this.iAllRecyclerLoadListener = iAllRecyclerLoadListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.hosdate);
            ava = itemView.findViewById(R.id.hoscap);
            carddate = itemView.findViewById(R.id.cardviewdate);
            itemView.setOnClickListener(this);



        }


        @Override
        public void onClick(View v) {
            iAllRecyclerLoadListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}