package com.pedrodev.appchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.adapters.PostsAdapter;
import com.pedrodev.appchat.models.Post;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.postProvider;
import com.pedrodev.appchat.utils.ViewedMessageHelper;

public class filtersActivity extends AppCompatActivity {

    String mExtraCategory;
    AuthProvider mAuthProvider;
    RecyclerView mRecyclerView;
    postProvider mPostProvider;
    PostsAdapter mPostsAdapter;
    Toolbar mToolbar;
    TextView mTextViewNumberFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        mExtraCategory = getIntent().getStringExtra("category");

        mAuthProvider = new AuthProvider();
        mRecyclerView = findViewById(R.id.RecyclerViewFilter);
        mTextViewNumberFilter = findViewById(R.id.numberFilter);

        mRecyclerView.setLayoutManager(new GridLayoutManager(filtersActivity.this, 2));
        // Show cards
        mToolbar = findViewById(R.id.idToolbar);

//          Delete this, we are on activity
//        ((AppCompatActivity) UserProfileActivity.this).

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Filtros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPostProvider = new postProvider();
    }


    @Override
    public void onStart() {
        super.onStart();
        //Post provider
        Query query = mPostProvider.getAllPostByCategoryAndTimeStamp(mExtraCategory);
        // Type post model
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions
                        .Builder<Post>()
                        .setQuery(query,Post.class)
                        .build();
        mPostsAdapter = new PostsAdapter(options, filtersActivity.this, mTextViewNumberFilter);
        mRecyclerView.setAdapter(mPostsAdapter);
        mPostsAdapter.startListening();
        ViewedMessageHelper.updateOnline(true, filtersActivity.this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mPostsAdapter.stopListening();
        ViewedMessageHelper.updateOnline(false, filtersActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, filtersActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}