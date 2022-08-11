package com.example.hbd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.example.hbd.Selftest.DonorViewResultFragment;
import com.example.hbd.Users.DonorUserFragment;
import com.example.hbd.Users.ViewDonorProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DonorUserAdapter extends RecyclerView.Adapter<DonorUserAdapter.ViewHolder>{

    DonorUserFragment context;
    List<UserModel> userModelList;

    public DonorUserAdapter(DonorUserFragment context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }


    @NonNull
    @Override
    public DonorUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.donorlist,parent,false);

        return new DonorUserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //display user data

        UserModel userModel = userModelList.get(position);
        holder.donorusername.setText(userModel.getUname());
        holder.donoruserblood.setText(userModel.getUblood());
        holder.donoruseremail.setText(userModel.getUemail());

        Glide.with(holder.dprofile.getContext()).load(userModel.getUserprofimage()).into(holder.dprofile);

        String imageUri = null;
        imageUri = userModel.getUserprofimage();
        Picasso.get().load(imageUri).into(holder.dprofile);


        // view donor's details
        //pass donor's details

        holder.viewdonorprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new ViewDonorProfileFragment(userModel.getUname(),userModel.getUblood(),userModel.getUage(),userModel.getUgender(),
                        userModel.getUemail(), userModel.getUhandphone(), userModel.getUcaddress(), userModel.getUhos(), userModel.getUoccupation())).addToBackStack(null).commit();


            }
        });

        // view donor's result

        holder.resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new DonorViewResultFragment()).addToBackStack(null).commit();


            }
        });



    }


    @Override
    public int getItemCount()  {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView donorusername, donoruserblood, donoruseremail,donoregresult;
        Button viewdonorprofilebtn,resultBtn;
        ImageView dprofile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            donorusername = itemView.findViewById(R.id.donornametext);
            donoruserblood = itemView.findViewById(R.id.donorbloodgrouptext);
            donoruseremail = itemView.findViewById(R.id.donoremailtext);




            dprofile = itemView.findViewById(R.id.donorprofilepicture);
            resultBtn = itemView.findViewById(R.id.resultBtn);

            viewdonorprofilebtn = itemView.findViewById(R.id.viewdonorprofileBtn);




        }
    }
}
