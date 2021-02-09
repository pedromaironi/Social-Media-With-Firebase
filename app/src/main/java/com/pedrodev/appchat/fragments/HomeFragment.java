package com.pedrodev.appchat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.activities.PostActivity;


public class HomeFragment extends Fragment {

    View mView;
    FloatingActionButton mFab;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        // save inflater
        mFab = mView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                goToPost();
            }

        });
        return mView;
    }

    private void goToPost() {
        Intent intent = new Intent(getContext(), PostActivity.class);
        startActivity(intent);
    }
}