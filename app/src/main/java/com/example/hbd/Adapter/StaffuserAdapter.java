package com.example.hbd.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.example.hbd.Users.StaffDirectoryFragment;
import com.example.hbd.Users.UserFragment;
import com.example.hbd.Users.ViewUserProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffuserAdapter extends RecyclerView.Adapter<StaffuserAdapter.ViewHolder>{

    StaffDirectoryFragment context;
    List<UserModel> userModelList;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;

    public StaffuserAdapter(StaffDirectoryFragment context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    @NonNull
    @Override
    public StaffuserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist,parent,false);

        return new StaffuserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffuserAdapter.ViewHolder holder, int position) {


        // display staff and admin details

        UserModel userModel = userModelList.get(position);
        holder.username.setText(userModel.getUname());
        holder.userblood.setText(userModel.getUblood());
        holder.useremail.setText(userModel.getUemail());
        holder.userid.setText(userModel.getUserid());



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
                        userModel.getUemail(), userModel.getUhandphone(), userModel.getUcaddress(), userModel.getUhos(), userModel.getUserprofimage())).addToBackStack(null).commit();


            }
        });



        holder.deleteuserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.userid.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be Undo.");

                // if successfully deleted
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Map<String,Object> map = new HashMap<>();
                        map.put("uemail" , userModel.getUemail());
                        map.put("ostate" , userModel.getOstate());
                        map.put("uhos" , userModel.getUhos());
                        map.put("uname" , userModel.getUname());
                        map.put("userid" , userModel.getUserid());
                        map.put("usertype" , "Not");

                        String userid = userModel.getUserid();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                        databaseReference.child(holder.userid.getText().toString()).updateChildren(map);

                        FirebaseFirestore.getInstance().collection("User").whereEqualTo("userid", userid)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentID = documentSnapshot.getId();

                                    firebaseFirestore.collection("User")
                                            .document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                                }

                            }
                        });



                    }
                });

                // failed to delete

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(holder.userid.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.show();




            }
        });







    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView username, userblood, useremail,userid;
        Button deleteuserbtn,viewprofilebtn;
        ImageView uprofile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.nametext);
            userblood = itemView.findViewById(R.id.bloodgrouptext);
            useremail = itemView.findViewById(R.id.emailtext);
            uprofile = itemView.findViewById(R.id.userprofilepicture);
            userid = itemView.findViewById(R.id.usersusertype);


            viewprofilebtn = itemView.findViewById(R.id.viewprofileBtn);
            deleteuserbtn  = itemView.findViewById(R.id.deleteBtn);

        }
    }
}
