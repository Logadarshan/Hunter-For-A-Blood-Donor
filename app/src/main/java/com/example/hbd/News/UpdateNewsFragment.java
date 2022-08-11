package com.example.hbd.News;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.Model.NewsModel;
import com.example.hbd.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UpdateNewsFragment extends Fragment {

    String Title;
    String Des;
    String Id;
    String Image;
    NewsModel newsModel ;
    DatabaseReference databaseReference;
    private static final int Gallery_Code=1;

    ImageView newpro;
    TextInputLayout newsedittitle ,newseditdes ;
    TextView newsid;
    StorageReference storageReference;

    public UpdateNewsFragment() {

    }

    public UpdateNewsFragment(String title, String des, String Id, String Image) {
        this.Title = title;
        this.Des = des;
        this.Id = Id;
        this.Image = Image;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_update_news, container, false);


         newsedittitle = v.findViewById(R.id.editnewstitle);
         newseditdes = v.findViewById(R.id.editnewsdescription);
         newsid = v.findViewById(R.id.updatenewsid1);
        // newpro = v.findViewById(R.id.updatenewsimage);
        Button updatenews = v.findViewById(R.id.newsupdatebtn);


        newsedittitle.getEditText().setText(Title);
        newseditdes.getEditText().setText(Des);
        newsid.setText(Id);

        // Update news
        updatenews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display news data
                Map<String,Object> map = new HashMap<>();
                map.put("Id", newsid.getText().toString());
                map.put("Title", newsedittitle.getEditText().getText().toString());
                map.put("Des", newseditdes.getEditText().getText().toString());

                // update in the database
                FirebaseDatabase.getInstance().getReference().child("news").child(Id)
                        .updateChildren(map);

                // Navigate to View News Feed
                Fragment addapp = new NewsfeedFragment();
                FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
                addappoint.replace(R.id.container,addapp).commit();

            }
        });


        return  v;
    }




}