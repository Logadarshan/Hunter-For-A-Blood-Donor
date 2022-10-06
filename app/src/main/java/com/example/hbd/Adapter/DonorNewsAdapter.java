package com.example.hbd.Adapter;


import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hbd.Model.NewsModel;
import com.example.hbd.News.DonorNewsViewFragment;
import com.example.hbd.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DonorNewsAdapter extends RecyclerView.Adapter<DonorNewsAdapter.ViewHolder> {

    DonorNewsViewFragment context;
    List<NewsModel> newsModelList;

    public DonorNewsAdapter(DonorNewsViewFragment context, List<NewsModel> newsModelList) {
        this.context = context;
        this.newsModelList = newsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.donornewsviewmenu,parent,false);

        return new DonorNewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        // display news data for donor
        NewsModel newsModel = newsModelList.get(position);
        holder.ntitle.setText(newsModel.getTitle());
        holder.ndesc.setText(newsModel.getDes());
        holder.nlink.setText(newsModel.getLink());
        Glide.with(holder.newimg.getContext()).load(newsModel.getImage()).into(holder.newimg);


        holder.nlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(holder.nlink.getText().toString()));
                context.startActivity(openLink);
            }
        });



        // load news images
        String imageUri = null;
        imageUri = newsModel.getImage();
        Picasso.get().load(imageUri).into(holder.newimg);

    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        ZoomageView newimg;
        TextView ntitle, ndesc,nlink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            newimg = itemView.findViewById(R.id.newsimage1);
            ntitle = itemView.findViewById(R.id.titlefornews1);
            ndesc = itemView.findViewById(R.id.newsdescription1);
            nlink = itemView.findViewById(R.id.newslink1);



        }
    }

}
