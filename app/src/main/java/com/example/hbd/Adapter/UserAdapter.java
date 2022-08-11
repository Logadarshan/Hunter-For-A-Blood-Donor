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
import com.example.hbd.Users.UserFragment;
import com.example.hbd.Users.ViewUserProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    UserFragment context;
    List<UserModel> userModelList;


    public UserAdapter(UserFragment context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist,parent,false);

        return new UserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // display staff and admin details

        UserModel userModel = userModelList.get(position);
        holder.username.setText(userModel.getUname());
        holder.userblood.setText(userModel.getUblood());
        holder.useremail.setText(userModel.getUemail());


        // display staff and admin profile picture

        Glide.with(holder.uprofile.getContext()).load(userModel.getUserprofimage()).into(holder.uprofile);

        String imageUri = null;
        imageUri = userModel.getUserprofimage();
        Picasso.get().load(imageUri).into(holder.uprofile);


        // view staff and admin profile picture
        // pass staff and admin details
        holder.viewprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new ViewUserProfileFragment(userModel.getUname(),userModel.getUblood(),userModel.getUage(),userModel.getUgender(),
                        userModel.getUemail(), userModel.getUhandphone(), userModel.getUcaddress(), userModel.getUhos())).addToBackStack(null).commit();


            }
        });




    }

    @Override
    public int getItemCount()  {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView username, userblood, useremail;
        Button deleteuserbtn,viewprofilebtn;
        ImageView uprofile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.nametext);
            userblood = itemView.findViewById(R.id.bloodgrouptext);
            useremail = itemView.findViewById(R.id.emailtext);
            uprofile = itemView.findViewById(R.id.userprofilepicture);


            viewprofilebtn = itemView.findViewById(R.id.viewprofileBtn);
            deleteuserbtn  = itemView.findViewById(R.id.deleteBtn);




        }
    }
}
