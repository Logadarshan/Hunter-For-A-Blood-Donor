package com.example.hbd.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder>{

    Context context;
    List<SlotModel> slotModelList;
    List<CardView> cardViews;
    LocalBroadcastManager localBroadcastManager;
    FirebaseFirestore firebaseFirestore;

    public TimeSlotAdapter(Context context) {
        this.context = context;
        this.slotModelList = slotModelList;
        cardViews = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public TimeSlotAdapter(Context context, List<SlotModel> slotModelList) {
        this.context = context;
        this.slotModelList = slotModelList;
        cardViews = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }





    @NonNull
    @Override
    public TimeSlotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_timeslot, parent, false);


        return new TimeSlotAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy,HHmm", Locale.getDefault()).format(new Date());
        String[] current = simpleDateFormat.split(",");


        holder.time.setText(slotModelList.get(position).getTime());
        holder.cap.setText(slotModelList.get(position).getCapacity());



        firebaseFirestore.collection("State").document(Common.city)
                .collection("Branch").document(Common.appointmenthospital)
                .collection("Date").document(Common.appointmentdate)
                .collection("Slot")
                .whereEqualTo("slotid", slotModelList.get(position).getSlotid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String data2 = document.getString("capacity");

                                if (data2.equals("0")){
                                    holder.availability.setText("Full");
                                    holder.cardtime.setEnabled(false);
                                    holder.cardtime.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
                                }
                                else {
                                    holder.availability.setText("Available");
                                    if(!cardViews.contains(holder.cardtime))
                                        cardViews.add(holder.cardtime);

                                    holder.setiAllDateLoadListener(new IAllRecyclerLoadListener() {
                                        @Override
                                        public void onItemSelectedListener(View view, int pos) {

                                            for(CardView cardView:cardViews)
                                                cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                                            holder.cardtime.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));


                                            Intent intent = new Intent(Common.KEY_ENABLE_NEXT);
                                            intent.putExtra(Common.KEY_TIME_SELECTD, slotModelList.get(pos));
                                            intent.putExtra(Common.KEY_STEP, 3);
                                            localBroadcastManager.sendBroadcast(intent);

                                        }
                                    });


                                }

                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return slotModelList.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView time,availability,cap;
        CardView cardtime;

        IAllRecyclerLoadListener iAllRecyclerLoadListener;

        public void setiAllDateLoadListener(IAllRecyclerLoadListener iAllRecyclerLoadListener) {
            this.iAllRecyclerLoadListener = iAllRecyclerLoadListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.hostime);
            availability = itemView.findViewById(R.id.hosava);
            cap = itemView.findViewById(R.id.hoscap);
            cardtime = itemView.findViewById(R.id.cardviewtime);
            itemView.setOnClickListener(this);



        }


        @Override
        public void onClick(View v) {
            iAllRecyclerLoadListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}