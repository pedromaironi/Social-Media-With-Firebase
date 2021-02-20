package com.pedrodev.appchat.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.activities.EditProfileActivity;
import com.pedrodev.appchat.activities.postDetailActivity;
import com.pedrodev.appchat.adapters.CommentAdapter;
import com.pedrodev.appchat.adapters.MypostAdapter;
import com.pedrodev.appchat.models.Comment;
import com.pedrodev.appchat.models.Post;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.UsersProvider;
import com.pedrodev.appchat.providers.postProvider;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    LinearLayout mLinearLayoutEditProfile;
    // If we are in a fragment property we must create a view type property
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    postProvider mPostProvider;

    MypostAdapter mypostAdapter;
    // textViews
    TextView mTextViewUserName;
    TextView mTextViewEmail;
    TextView mTexViewPosts;
    TextView mTextViewPhone;
    TextView mTextViewExists;

    CircleImageView mImageViewProfile;
    ImageView mImageViewCover;
    RecyclerView mRecyclerView;
    private String mImageCover = "";
    private String mImageProfile = "";
    ListenerRegistration mListener;

    View mView;
    public ProfileFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mLinearLayoutEditProfile = mView.findViewById(R.id.LinearLayoutEditProfile);

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();
        mPostProvider = new postProvider();

        mRecyclerView = mView.findViewById(R.id.RecyclerViewMyPost);

        // Show itemns in lines
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        //TextViews
        mTextViewPhone = mView.findViewById(R.id.textViewPhoneNumber);
        mTextViewUserName = mView.findViewById(R.id.editTextNameProfile);
        mTextViewEmail = mView.findViewById(R.id.editTextEmailProfile);
        mTextViewExists = mView.findViewById(R.id.Posts);

        // Image profile and circle profile
        mImageViewProfile = mView.findViewById(R.id.CircleImageProfile);
        mImageViewCover = mView.findViewById(R.id.imageCover);
        mTexViewPosts = mView.findViewById(R.id.textViewPublications);
        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditProfile();     
            }
        });
        getUser();
        getPostCant();
        checkIfExistsPosts();
        return mView;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.remove();
        }
    }

    private void checkIfExistsPosts() {
        mListener =  mPostProvider.getPostByUser(mAuthProvider.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null) {
                    int numberPost = value.size();
                    if (numberPost > 0) {
                        mTextViewExists.setText("Publicaciones");
                        mTextViewExists.setTextColor(Color.BLACK);

                    } else {
                        mTextViewExists.setText("No hay publicaciones");
                        mTextViewExists.setTextColor(Color.GRAY);
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getUser();
        Query query = mPostProvider.getPostByUser(mAuthProvider.getUid());
        // Type post model
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions
                        .Builder<Post>()
                        .setQuery(query,Post.class)
                        .build();
        mypostAdapter = new MypostAdapter(options, getContext());
        mRecyclerView.setAdapter(mypostAdapter);
        mypostAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mypostAdapter.stopListening();
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getUser(){
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("email")){
                        mTextViewEmail.setText(documentSnapshot.getString("email"));
                    }
                    if (documentSnapshot.contains("username")){
                        mTextViewUserName.setText(documentSnapshot.getString("username"));
                    }
                    if (documentSnapshot.contains("phoneNumber")){
                        mTextViewPhone.setText(documentSnapshot.getString("phoneNumber"));
                    }
                    if(documentSnapshot.contains("image_cover")){
                        mImageCover = documentSnapshot.getString("image_cover");
                        if (mImageCover != null) {
                            if (!mImageCover.isEmpty()) {
                                Picasso.with(getContext()).load(mImageCover).into(mImageViewCover);
                            }
                        }
                    }
                    if(documentSnapshot.contains("image_profile")){
                        mImageProfile = documentSnapshot.getString("image_profile");
                        if (mImageProfile != null) {
                            if (!mImageProfile.isEmpty()) {
                                Picasso.with(getContext()).load(mImageProfile).into(mImageViewProfile);
                            }
                        }
                    }
                }
            }
        });
    }

    private void getPostCant(){
        mPostProvider.getPostByUser(mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                // Returns the number of documents in the QuerySnapshot.
                mTexViewPosts.setText(String.valueOf(numberPost));
            }
        });
    }

}