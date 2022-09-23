package com.example.hbd.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Model.SlotModel;
import com.example.hbd.R;
import com.example.hbd.Slot.SlotDetailsFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StaffSlotViewAdapter extends RecyclerView.Adapter<StaffSlotViewAdapter.MyViewHolder>{

    Context context;
    List<SlotModel> slotModelList;
    List<CardView> cardViews;
    FirebaseFirestore firebaseFirestore;


    public StaffSlotViewAdapter(Context context) {
        this.context = context;
        this.slotModelList = slotModelList;

    }

    public StaffSlotViewAdapter(Context context, List<SlotModel> slotModelList) {
        this.context = context;
        this.slotModelList = slotModelList;
        cardViews = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public StaffSlotViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_slots, parent, false);

        return new StaffSlotViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffSlotViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        SlotModel slotModel = slotModelList.get(position);
        holder.hosslottime.setText(slotModelList.get(position).getTime());
        holder.hosslotcap.setText(slotModelList.get(position).getCapacity());
        holder.hosslotid.setText(slotModelList.get(position).getSlotid());

        holder.sdetailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new SlotDetailsFragment(slotModel.getTime(), slotModel.getCapacity(),slotModel.getSlotid(),slotModel.getTimestamp(),slotModel.getDate())).addToBackStack(null).commit();

            }
        });




    }




    @Override
    public int getItemCount() {
        return slotModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView hosslottime, hosslotcap,hosslotid;
        CardView cardslot;
        ImageButton sdetailbtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hosslottime = itemView.findViewById(R.id.hosslottime);
            hosslotcap = itemView.findViewById(R.id.hosslotcapacity);
            hosslotid = itemView.findViewById(R.id.hosslotid);
            cardslot = itemView.findViewById(R.id.cardslotviewtime);
            sdetailbtn = itemView.findViewById(R.id.slotdetailsBtn);



        }


    }
}
