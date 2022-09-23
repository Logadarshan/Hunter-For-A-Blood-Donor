package com.example.hbd.News;

import static android.app.Activity.RESULT_OK;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.Model.NewsModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.R;
import com.example.hbd.Slot.SlotFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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
    Uri imageUrl=null;
    CollectionReference documentReference;
    ImageView newpro;
    TextInputLayout newsedittitle ,newseditdes ;
    TextView newsid;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;


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
         newpro = v.findViewById(R.id.updatenewsimg);
         Button updatenews = v.findViewById(R.id.newsupdatebtn);

         firebaseFirestore = FirebaseFirestore.getInstance();

        newsedittitle.getEditText().setText(Title);
        newseditdes.getEditText().setText(Des);
        newsid.setText(Id);
        Picasso.get().load(Image).into(newpro);




        // Update news
        updatenews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateslot();
            }
        });


        return  v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Code && resultCode == RESULT_OK)
        {
            imageUrl = data.getData();
            newpro.setImageURI(imageUrl);
        }


    }


    private void updateslot() {


        Map<String,Object> map1 = new HashMap<>();
        map1.put("Id", newsid.getText().toString());
        map1.put("Title", newsedittitle.getEditText().getText().toString());
        map1.put("Des", newseditdes.getEditText().getText().toString());


        documentReference= FirebaseFirestore.getInstance().
                collection("News");

        documentReference.document(Id).update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // Navigate to View News Feed
                Fragment addapp = new NewsfeedFragment();
                FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
                addappoint.replace(R.id.container,addapp).commit();
                Toast.makeText(getActivity(), "News Updated successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"News Updated");
            }
        });


    }




}