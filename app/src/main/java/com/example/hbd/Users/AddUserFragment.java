package com.example.hbd.Users;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Interface.IAllCityLoadListener;
import com.example.hbd.Interface.IAllHosLoadListener;
import com.example.hbd.Interface.IAllStateLoadListener;
import com.example.hbd.Login;
import com.example.hbd.Model.HospitalModel;
import com.example.hbd.Model.UserModel;
import com.example.hbd.Others.Common;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddUserFragment extends Fragment implements IAllStateLoadListener, IAllHosLoadListener {


    EditText adminstaffname,adminstaffemail,adminstaffpassword,adminstafforg,adminstaffostate;
    Button addadminstaff;

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth fAuth,fAuth1;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    View V;

    Unbinder unbinder;
    String adduser;
    Dialog dialog;
    DatabaseReference databaseReference2;
    String userID;

    String  uhos,uemail, upassword, ostate;

    String uhos1, ostate1;


    CollectionReference allcity;
    CollectionReference allhos;


    @BindView(R.id.userostate12)
    MaterialSpinner statespinner;
    @BindView(R.id.userhos12)
    MaterialSpinner hosspinner;

    RadioGroup radioGroupUser;
    RadioButton radioButtonUser;



    IAllStateLoadListener iAllStateLoadListener;
    IAllHosLoadListener iAllHosLoadListener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allcity = FirebaseFirestore.getInstance().collection("State");
        iAllStateLoadListener = this;
        iAllHosLoadListener = this;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.fragment_add_user, container, false);
        unbinder = ButterKnife.bind(this,V);

        adminstaffname = V.findViewById(R.id.uusername);
        adminstaffemail = V.findViewById(R.id.useremail);
        adminstaffpassword = V.findViewById(R.id.userpassword);
        addadminstaff = V.findViewById(R.id.adduserdataBtn);
        dialog = new Dialog(this.getContext());
        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        radioGroupUser = V.findViewById(R.id.usertypeadmin);
        radioGroupUser.clearCheck();
        userID = firebaseUser.getUid();

        loadAllCity();
        // add admin and staff details
        addadminstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkEmail();
                 uemail = adminstaffemail.getText().toString();
                 upassword = adminstaffpassword.getText().toString();


                adddetails(uemail,upassword);

            }
        });



        return V;
    }

    private void loadAllCity() {
        allcity.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    List<String> list = new ArrayList<>();
                    list.add("Please Choose City");
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



    private void adddetails(String uemail, String upassword) {

        FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    firebaseUser = fAuth.getCurrentUser();
                    adduser = firebaseUser.getUid();
                    firebaseDatabase = FirebaseDatabase.getInstance();

                    int selectedUserTypeId = radioGroupUser.getCheckedRadioButtonId();
                    radioButtonUser = V.findViewById(selectedUserTypeId);

                    Map<String,Object> user = new HashMap<>();
                    user.put("ostate",statespinner.getText().toString());
                    user.put("usertype", radioButtonUser.getText().toString());
                    user.put("uname",adminstaffname.getText().toString());
                    user.put("uhos",hosspinner.getText().toString());
                    user.put("uemail",uemail);
                    user.put("upassword",upassword);
                    user.put("userid",adduser);

                    user.put("uage","-");
                    user.put("ublood","-");
                    user.put("ucaddress","-");
                    user.put("ucpost","-");
                    user.put("ucstate","-");
                    user.put("udob","-");
                    user.put("ufax","-");
                    user.put("ugender","-");
                    user.put("uhandphone","-");
                    user.put("uhomephone","-");
                    user.put("uic","-");
                    user.put("umarriage","-");
                    user.put("uoccupation","-");
                    user.put("urace","-");
                    user.put("urepassword","-");
                    user.put("userprofimage","-");


                    databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(adduser).setValue(user);

                    DocumentReference documentReference = firebaseFirestore.collection("User").document(adduser);
                    documentReference.set(user);

                    dialog.setContentView(R.layout.verify);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button btncon1 = dialog.findViewById(R.id.continueappBtn1);

                    // Navigate to selftest Page
                    btncon1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fAuth.signOut();
                            startActivity(new Intent(getContext(), Login.class));
                            dialog.dismiss();

                        }
                    });

                    dialog.show();


                }

            }
        });



    }

    private void checkEmail(){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.fetchSignInMethodsForEmail(adminstaffemail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            Log.e("TAG", "Is New User!");
                        } else {
                            adminstaffemail.setError("User already exists");
                        }

                    }
                });
    }


    @Override
    public void onAllStateLoadSuccess(List<String> stateList) {
        statespinner.setItems(stateList);
        statespinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0){
                    loadHosp(item.toString());
                }
            }
        });
    }

    private void loadHosp(String cityname) {


        Common.city = cityname;

        allhos= FirebaseFirestore.getInstance()
                .collection("State").document(cityname)
                .collection("Branch");


        allhos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    List<String> list = new ArrayList<>();
                    list.add("Please Choose Hospital");
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                        list.add(documentSnapshot.getId());
                    iAllHosLoadListener.onAllHosLoadSuccess(list);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllHosLoadListener.onAllHosLoadFailed(e.getMessage());
            }
        });


    }

    @Override
    public void onAllStateLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAllHosLoadSuccess(List<String> hosList) {
        hosspinner.setItems(hosList);
    }

    @Override
    public void onAllHosLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }
}