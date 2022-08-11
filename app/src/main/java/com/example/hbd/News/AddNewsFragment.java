package com.example.hbd.News;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddNewsFragment extends Fragment {

    TextInputLayout newstitle, newsdesc;
    TextInputEditText newstitletext, newsdesctext;
    Button savebtn;
    ImageButton imgnewsbtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    private static final int Gallery_Code=1;
    Uri imageUrl=null;
    ProgressDialog progressDialog;
    long nid = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_news, container, false);



        newstitle = v.findViewById(R.id.newstitle);
        newsdesc = v.findViewById(R.id.newsdec);
        newstitletext = v.findViewById(R.id.newstitletext);
        newsdesctext = v.findViewById(R.id.newsdesctext);
        savebtn = v.findViewById(R.id.savebtn);
        imgnewsbtn = v.findViewById(R.id.imagenewsBtn);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("news");
        firebaseStorage   = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this.getContext());


        // Open gallery
      imgnewsbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
              intent.setType("image/url*");
              startActivityForResult(intent,Gallery_Code);
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


                // Generate unique id
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nid = (snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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

                                    DatabaseReference newPost = databaseReference.push();



                                    databaseReference.child(String.valueOf(nid+1)).child("Id").setValue(String.valueOf(nid+1));
                                    databaseReference.child(String.valueOf(nid+1)).child("Title").setValue(ntitle);
                                    databaseReference.child(String.valueOf(nid+1)).child("Des").setValue(ndesc);
                                    databaseReference.child(String.valueOf(nid+1)).child("Image").setValue(task.getResult().toString());
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