package com.example.hbd.Selftest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hbd.R;


public class SelftestFragment extends Fragment {

    Button agreeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selftest, container, false);

        agreeBtn = v.findViewById(R.id.testagreeBtn);

        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate selftest question interface
                Fragment testquestion = new TestQuestionFragment();
                FragmentTransaction surveyquestion = getActivity().getSupportFragmentManager().beginTransaction();
                surveyquestion.replace(R.id.container,testquestion).commit();
            }
        });



        return v;
    }
}