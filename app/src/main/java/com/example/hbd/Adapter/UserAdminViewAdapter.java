package com.example.hbd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.example.hbd.Report.ViewUsersFragment;
import com.example.hbd.Users.UserFragment;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdminViewAdapter extends RecyclerView.Adapter<UserAdminViewAdapter.ViewHolder> {

    ViewUsersFragment context;
    List<UserModel> userModelList;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;

    public UserAdminViewAdapter(ViewUsersFragment context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }




    @NonNull
    @Override
    public UserAdminViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlistadminview,parent,false);

        return new UserAdminViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdminViewAdapter.ViewHolder holder, int position) {

        UserModel userModel = userModelList.get(position);
        holder.username.setText(userModel.getUname());
        holder.userblood.setText(userModel.getUblood());
        holder.usernum.setText(userModel.getUhandphone());
        holder.userid.setText(userModel.getUserid());


        // display staff and admin profile picture

        Glide.with(holder.uprofile.getContext()).load(userModel.getUserprofimage()).into(holder.uprofile);

        String imageUri = null;
        imageUri = userModel.getUserprofimage();
        Picasso.get().load(imageUri).into(holder.uprofile);



    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView username, userblood, usernum,userid;
        ImageView uprofile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.adminnametext);
            userblood = itemView.findViewById(R.id.adminbloodgrouptext);
            usernum = itemView.findViewById(R.id.adminnumtext);
            uprofile = itemView.findViewById(R.id.adminuserprofilepicture);
            userid = itemView.findViewById(R.id.adminusertype);


        }
    }
}
