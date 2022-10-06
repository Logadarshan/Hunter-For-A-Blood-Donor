package com.example.hbd.Report;

import static android.app.Activity.RESULT_OK;
import static com.example.hbd.Selftest.TestQuestionFragment.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hbd.Interface.IAllCityLoadListener;
import com.example.hbd.Interface.IAllStateLoadListener;
import com.example.hbd.News.NewsfeedFragment;
import com.example.hbd.Others.Common;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AddHospitalFragment extends Fragment implements IAllStateLoadListener {

    TextInputLayout hosname, hosnum, hosadd;
    TextInputEditText hosnametext,hosaddtext,hosnumtext;
    Button savebtn;
    ImageButton hosbtn,backbtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    private static final int Gallery_Code=1;
    Uri imageUrl=null;
    ProgressDialog progressDialog;
    long nid = 0;
    CollectionReference collectionReference;
    FirebaseFirestore firebaseFirestore;
    String hospitalname, hospitaladdress, hospitalnum;

    CollectionReference allstate;

    Unbinder unbinder;

    IAllStateLoadListener iAllStateLoadListener;

    @BindView(R.id.spinnerstate1)
    MaterialSpinner statespinner;

    View v;

    public AddHospitalFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allstate = FirebaseFirestore.getInstance().collection("State");
        iAllStateLoadListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_hospital, container, false);
        unbinder = ButterKnife.bind(this,v);

        hosaddtext = v.findViewById(R.id.hosaddressext);
        hosnametext = v.findViewById(R.id.hosnametext);
        hosnumtext = v.findViewById(R.id.hosnumtext);
        savebtn = v.findViewById(R.id.savehosbtn);
        hosbtn = v.findViewById(R.id.hospitalimgBtn);
        backbtn = v.findViewById(R.id.backBtn5);


        firebaseStorage   = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this.getContext());
        firebaseFirestore = FirebaseFirestore.getInstance();


        // Open gallery
        hosbtn.setOnClickListener(new View.OnClickListener() {
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
                Fragment hos = new HospitalFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,hos).commit();
            }
        });



        loadAllState();

        return v;
    }

    private void loadAllState() {

        allstate.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    List<String> list = new ArrayList<>();
                    list.add("Please Choose State");
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                        list.add(documentSnapshot.getId());
                    iAllStateLoadListener.onAllStateLoadSuccess(list);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllStateLoadListener.onAllStateLoadFailed(e.getMessage());
            }
        });




    }


    // Upload image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Code && resultCode == RESULT_OK)
        {
            imageUrl = data.getData();
            hosbtn.setImageURI(imageUrl);
        }




        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospitaladdress = hosaddtext.getText().toString().trim();
                hospitalname =  hosnametext.getText().toString().trim();
                hospitalnum = hosnumtext.getText().toString().trim();




                // If failed
                if(hospitaladdress.isEmpty() || hospitalname.isEmpty()  || hospitalnum.isEmpty()){


                }
                // If success
                else{

                    progressDialog.setTitle("Uploading....");
                    progressDialog.show();

                    // save in the database
                    StorageReference filepath = firebaseStorage.getReference().child("hospitalpics").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    String t = task.getResult().toString();


                                    String id = Common.generateString(6);

                                    Map<String,Object> map = new HashMap<>();
                                    map.put("name" , hospitalname);
                                    map.put("address" , hospitaladdress);
                                    map.put("num", hospitalnum);
                                    map.put("Image",task.getResult().toString());



                                    collectionReference = FirebaseFirestore.getInstance()
                                            .collection("State").document(statespinner.getText().toString())
                                            .collection("Branch");

                                    collectionReference.document(hospitalname+", "+hospitaladdress).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG,"News Created");
                                        }
                                    });




                                    progressDialog.dismiss();
                                    // move back to View News Feed Page
                                    Fragment hos = new HospitalFragment();
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.container,hos).commit();
                                }
                            });
                        }
                    });


                }

            }
        });

    }


    @Override
    public void onAllStateLoadSuccess(List<String> stateList) {
        statespinner.setItems(stateList);
    }

    @Override
    public void onAllStateLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }
}