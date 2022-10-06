package com.example.hbd.News;

import static android.app.Activity.RESULT_OK;

import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.Others.Common;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddNewsFragment extends Fragment {

    TextInputLayout newstitle, newsdesc,newslink;
    TextInputEditText newstitletext, newsdesctext,newslinktext;
    Button savebtn;
    ImageButton imgnewsbtn,backbtn;
    FirebaseStorage firebaseStorage;
    private static final int Gallery_Code=1;
    Uri imageUrl=null;
    ProgressDialog progressDialog;
    long nid = 0;
    CollectionReference collectionReference;
    FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_news, container, false);



        newstitle = v.findViewById(R.id.newstitle);
        newsdesc = v.findViewById(R.id.newsdec);
        newslink = v.findViewById(R.id.newslink);
        newstitletext = v.findViewById(R.id.newstitletext);
        newsdesctext = v.findViewById(R.id.newsdesctext);
        newslinktext = v.findViewById(R.id.newslinktext);
        savebtn = v.findViewById(R.id.savebtn);
        imgnewsbtn = v.findViewById(R.id.imagenewsBtn);
        backbtn = v.findViewById(R.id.backBtn);


        firebaseStorage   = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this.getContext());
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Open gallery
      imgnewsbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
              intent.setType("image/url*");
              startActivityForResult(intent,Gallery_Code);
          }
      });

      backbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Fragment addapp = new NewsfeedFragment();
              FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
              addappoint.replace(R.id.container,addapp).commit();
          }
      });





        return v;
    }


    // Upload image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Code && resultCode == RESULT_OK)
        {
            imageUrl = data.getData();
            imgnewsbtn.setImageURI(imageUrl);
        }




        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ntitle = newstitletext.getText().toString().trim();
                final String ndesc = newsdesctext.getText().toString().trim();
                final String nlink = newslinktext.getText().toString().trim();




                // If failed
                if(ntitle.isEmpty() || ndesc.isEmpty()){


                }
                // If success
                else{

                    progressDialog.setTitle("Uploading....");
                    progressDialog.show();

                    // save in the database
                    StorageReference filepath = firebaseStorage.getReference().child("imagePost").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    String t = task.getResult().toString();


                                    String id = Common.generateString(6);

                                    Map<String,Object> map = new HashMap<>();
                                    map.put("Id" , id);
                                    map.put("Title" , ntitle);
                                    map.put("Des", ndesc);
                                    map.put("link", nlink);
                                    map.put("Image",task.getResult().toString());


                                    collectionReference = FirebaseFirestore.getInstance()
                                            .collection("News");

                                    collectionReference.document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG,"News Created");
                                        }
                                    });


                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });

                    // move back to View News Feed Page
                    Fragment addapp = new NewsfeedFragment();
                    FragmentTransaction addappoint = getActivity().getSupportFragmentManager().beginTransaction();
                    addappoint.replace(R.id.container,addapp).commit();

                }

            }
        });

    }

}