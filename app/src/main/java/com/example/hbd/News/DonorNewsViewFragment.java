package com.example.hbd.News;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbd.Adapter.DonorNewsAdapter;
import com.example.hbd.Model.NewsModel;
import com.example.hbd.Others.SpacesItemDecoration;
import com.example.hbd.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class DonorNewsViewFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    RecyclerView recyclerView;
    DonorNewsAdapter donorNewsAdapter;
    List<NewsModel> newsModelList;
    FirebaseFirestore firebaseFirestore;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_donor_news_view, container, false);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage   = FirebaseStorage.getInstance();
        recyclerView = v.findViewById(R.id.drvview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        newsModelList = new ArrayList<NewsModel>();
        donorNewsAdapter = new DonorNewsAdapter(DonorNewsViewFragment.this,newsModelList);

        recyclerView.setAdapter(donorNewsAdapter);

        initView();
        EventChangeListner();



        return v;
    }


    private void initView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));

    }




    private void EventChangeListner() {

        firebaseFirestore.collection("News")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Error", error.getMessage());
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){

                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                newsModelList.add(documentChange.getDocument().toObject(NewsModel.class));
                            }
                            donorNewsAdapter.notifyDataSetChanged();

                        }
                    }
                });


    }
}