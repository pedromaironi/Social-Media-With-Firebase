package com.pedrodev.appchat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.adapters.MypostAdapter;
import com.pedrodev.appchat.models.Post;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.UsersProvider;
import com.pedrodev.appchat.providers.postProvider;
import com.pedrodev.appchat.utils.ViewedMessageHelper;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    // If we are in a fragment property we must create a view type property
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    postProvider mPostProvider;

    // textViews
    TextView mTextViewUserName;
    TextView mTextViewEmail;
    TextView mTexViewPosts;
    TextView mTextViewPhone;
    TextView mTextViewExists;
    FloatingActionButton mFabChat;

    CircleImageView mImageViewProfile;
    ImageView mImageViewCover;

    private String mImageCover = "";
    private String mImageProfile = "";

    //CircleImageView mCircleImageViewBack;

    MypostAdapter mypostAdapter;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;

    // That String is for receive the intent.putExtra from POST DETAIL ACTIVITY
    String mExtraIdUser;
    ListenerRegistration mListener;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mRecyclerView = findViewById(R.id.RecyclerViewMyPost);

        // Show itemns in lines
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserProfileActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();
        mPostProvider = new postProvider();

        //TextViews
        mTextViewPhone = findViewById(R.id.textViewPhoneNumber);
        mTextViewUserName = findViewById(R.id.editTextNameProfile);
        mTextViewEmail = findViewById(R.id.editTextEmailProfile);
        mTextViewExists = findViewById(R.id.Posts);
        mToolbar = findViewById(R.id.Toolbar);
        mFabChat = findViewById(R.id.fabChat);

//          Delete this, we are on activity
//        ((AppCompatActivity) UserProfileActivity.this).

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Image profile and circle profile
        mImageViewProfile = findViewById(R.id.CircleImageProfile);
        mImageViewCover = findViewById(R.id.imageCover);
        mTexViewPosts = findViewById(R.id.textViewPublications);

        mExtraIdUser = getIntent().getStringExtra("idUser");

        if (mAuthProvider.getUid().equals(mExtraIdUser)) {
            mFabChat.setEnabled(false);
        }
        mFabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity();
            }
        });
        getUser();
        getPostCant();
        checkIfExistsPosts();
    }


    private void goToChatActivity() {
        Intent intent = new Intent(UserProfileActivity.this,ChatActivity.class);
        intent.putExtra("idUser1",mAuthProvider.getUid());
        intent.putExtra("idUser2",mExtraIdUser);
        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = mPostProvider.getPostByUser(mExtraIdUser);
        // Type post model
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions
                        .Builder<Post>()
                        .setQuery(query,Post.class)
                        .build();
        mypostAdapter = new MypostAdapter(options, UserProfileActivity.this);
        mRecyclerView.setAdapter(mypostAdapter);
        mypostAdapter.startListening();
        ViewedMessageHelper.updateOnline(true, UserProfileActivity.this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, UserProfileActivity.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mypostAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.remove();
        }
    }
    private void checkIfExistsPosts() {
        mListener =  mPostProvider.getPostByUser(mExtraIdUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                int numberPost = value.size();
                if(numberPost > 0){
                    mTextViewExists.setText("Publicaciones");
                    mTextViewExists.setTextColor(Color.BLACK);

                }else{
                    mTextViewExists.setText("No hay publicaciones");
                    mTextViewExists.setTextColor(Color.GRAY);
                }
                }
            }
        });
    }

    private void getUser(){
        mUsersProvider.getUser(mExtraIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                Picasso.with(UserProfileActivity.this).load(mImageCover).into(mImageViewCover);
                            }
                        }
                    }
                    if(documentSnapshot.contains("image_profile")){
                        mImageProfile = documentSnapshot.getString("image_profile");
                        if (mImageProfile != null) {
                            if (!mImageProfile.isEmpty()) {
                                Picasso.with(UserProfileActivity.this).load(mImageProfile).into(mImageViewProfile);
                            }
                        }
                    }
                }
            }
        });
    }

    // Delete mAuthProvider.getUid(), por el extra que recibo desde postDetailActivity para mostrar la info del usuario
    private void getPostCant(){
        mPostProvider.getPostByUser(mExtraIdUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                // Returns the number of documents in the QuerySnapshot.
                mTexViewPosts.setText(String.valueOf(numberPost));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return true;


    }
}