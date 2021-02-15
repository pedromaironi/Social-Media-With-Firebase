package com.pedrodev.appchat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.pedrodev.appchat.R;
import com.pedrodev.appchat.activities.filtersActivity;

public class FilterFragment extends Fragment {

    View mView;

    CardView mCardViewBug;
    CardView mCardViewOffTopic;
    CardView mCardViewSoftware;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_filter, container, false);
        mCardViewBug = mView.findViewById(R.id.cardViewBugs);
        mCardViewOffTopic = mView.findViewById(R.id.cardViewOffTopic);
        mCardViewSoftware = mView.findViewById(R.id.cardViewSoftware);

        mCardViewBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilterActivity("BUGS");
            }
        });

        mCardViewOffTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilterActivity("OFFTOPIC");
            }
        });
        mCardViewSoftware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilterActivity("SOFTWARE");
            }
        });

        return mView;
    }

    private void goToFilterActivity(String categoria){
        Intent intent = new Intent(getContext(), filtersActivity.class);
        intent.putExtra("category", categoria);
        startActivity(intent);
    }

}