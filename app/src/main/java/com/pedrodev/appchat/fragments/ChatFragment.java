package com.pedrodev.appchat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.adapters.ChatsAdapter;
import com.pedrodev.appchat.models.Chat;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.ChatsProvider;

public class ChatFragment extends Fragment {

    ChatsAdapter mChatsAdapter;
    RecyclerView mRecyclerView;
    View mView;
    ChatsProvider mChatsProvider;
    AuthProvider mAuthProvider;
    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = mView.findViewById(R.id.RecyclerViewChats);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mChatsProvider = new ChatsProvider();
        mAuthProvider = new AuthProvider();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Post provider
        Query query = mChatsProvider.getAll(mAuthProvider.getUid());
        // Type post model
        FirestoreRecyclerOptions<Chat> options =
                new FirestoreRecyclerOptions
                        .Builder<Chat>()
                        .setQuery(query,Chat.class)
                        .build();
        mChatsAdapter = new ChatsAdapter(options, getContext());
        mRecyclerView.setAdapter(mChatsAdapter);
        mChatsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mChatsAdapter.stopListening();
    }
}