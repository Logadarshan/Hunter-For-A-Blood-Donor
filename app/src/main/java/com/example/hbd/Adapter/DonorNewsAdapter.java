package com.example.hbd.Adapter;


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
        Glide.with(holder.newimg.getContext()).load(newsModel.getImage()).into(holder.newimg);


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
        TextView ntitle, ndesc;
        ImageButton ndeletebtn,nupdatebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            newimg = itemView.findViewById(R.id.newsimage);
            ntitle = itemView.findViewById(R.id.titlefornews);
            ndesc = itemView.findViewById(R.id.newsdescription);
            ndeletebtn = itemView.findViewById(R.id.newsdeletebtn);
            nupdatebtn = itemView.findViewById(R.id.newsupdatebtn);


        }
    }

}
