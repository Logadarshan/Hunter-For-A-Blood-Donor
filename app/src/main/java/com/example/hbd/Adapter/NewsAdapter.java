package com.example.hbd.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.example.hbd.Model.NewsModel;
import com.example.hbd.News.NewsfeedFragment;
import com.example.hbd.News.UpdateNewsFragment;
import com.example.hbd.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    NewsfeedFragment context;
    List<NewsModel> newsModelList;
    Dialog dialog;

    public NewsAdapter(NewsfeedFragment context, List<NewsModel> newsModelList) {
        this.context = context;
        this.newsModelList = newsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsviewmenu,parent,false);

        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {

        // display news data for staff

        NewsModel newsModel = newsModelList.get(position);
        holder.ntitle.setText(newsModel.getTitle());
        holder.ndesc.setText(newsModel.getDes());
        holder.nid.setText(newsModel.getId());
        holder.nlink.setText(newsModel.getLink());
        Glide.with(holder.newimg.getContext()).load(newsModel.getImage()).into(holder.newimg);


        holder.nlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(holder.nlink.getText().toString()));
                context.startActivity(openLink);
            }
        });

        //display news images

        String imageUri = null;
        imageUri = newsModel.getImage();
        Picasso.get().load(imageUri).into(holder.newimg);

        Zoomy.Builder builder = new Zoomy.Builder((Activity) holder.newimg.getContext()).target(holder.newimg)
                .animateZooming(false).enableImmersiveMode(false)
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {

                    }
                });


        // update news details
        // pass news data

        holder.nupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new UpdateNewsFragment(newsModel.getTitle(),newsModel.getDes(), newsModel.getId(),newsModel.getImage(),newsModel.getLink())).addToBackStack(null).commit();


            }
        });


        // delete news data

        holder.ndeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.nid.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be Undo.");

                // if successfully deleted
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseFirestore.getInstance().collection("News").document(newsModel.getId()).delete();
                        Toast.makeText(holder.nid.getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                });

                // failed to delete

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(holder.nid.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.show();



            }
        });

    }


    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ZoomageView  newimg;
        TextView ntitle, ndesc,nid,nlink;
        ImageButton ndeletebtn,nupdatebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            newimg = itemView.findViewById(R.id.newsimage);
            ntitle = itemView.findViewById(R.id.titlefornews);
            ndesc = itemView.findViewById(R.id.newsdescription);
            nid = itemView.findViewById(R.id.newsid);
            nlink = itemView.findViewById(R.id.newslink);
            ndeletebtn = itemView.findViewById(R.id.newsdeletebtn);
            nupdatebtn = itemView.findViewById(R.id.newseditbtn);



        }

    }
}
