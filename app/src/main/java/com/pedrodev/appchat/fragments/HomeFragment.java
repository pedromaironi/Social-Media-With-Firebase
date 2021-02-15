package com.pedrodev.appchat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.activities.MainActivity;
import com.pedrodev.appchat.activities.PostActivity;
import com.pedrodev.appchat.adapters.PostsAdapter;
import com.pedrodev.appchat.models.Post;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.postProvider;


public class HomeFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {

    View mView;
    FloatingActionButton mFab;
    MaterialSearchBar mSearchBar;
    AuthProvider mAuthProvider;
    RecyclerView mRecyclerView;
    postProvider mPostProvider;
    PostsAdapter mPostAdapterSearch;

     PostsAdapter mPostAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        // save inflater
        mFab = mView.findViewById(R.id.fab);
        mSearchBar = mView.findViewById(R.id.searchBar);
        mAuthProvider = new AuthProvider();
        mRecyclerView = mView.findViewById(R.id.RecyclerViewHome);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        // Show cards
        mPostProvider = new postProvider();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost();
            }

        });
        //Other stuff in OnCreate();
        mSearchBar.setOnSearchActionListener(this);
        mSearchBar.inflateMenu(R.menu.main_menu);
        mSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.itemLogout) {
                    logout();
                }
                return true;
            }
        });
        return mView;
    }

    private void searchByTitle(String title){
        Query query = mPostProvider.getPostByTitle(title);
        // Type post model
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions
                        .Builder<Post>()
                        .setQuery(query,Post.class)
                        .build();
        mPostAdapterSearch = new PostsAdapter(options, getContext());
        mPostAdapterSearch.notifyDataSetChanged();
        mRecyclerView.setAdapter(mPostAdapterSearch);
        mPostAdapterSearch.startListening();
    }

    private void getAllPost(){
        //Post provider
        Query query = mPostProvider.getAllPost();
        // Type post model
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions
                        .Builder<Post>()
                        .setQuery(query,Post.class)
                        .build();
        mPostAdapter = new PostsAdapter(options, getContext());
        mPostAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mPostAdapter);
        mPostAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllPost();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mPostAdapterSearch!=null){
            mPostAdapter.stopListening();
        }
    }

    private void goToPost() {
        Intent intent = new Intent(getContext(), PostActivity.class);
        startActivity(intent);
    }

//    @Override
////    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
////        inflater.inflate(R.menu.main_menu, menu);
////        super.onCreateOptionsMenu(menu, inflater);
////
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////
////    }

    private void logout() {
        mAuthProvider.logout();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    @Override
    public void onSearchStateChanged(boolean enabled) {
        if(!enabled){
        getAllPost();
        }
    }


    // return the text writer in the input text
    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByTitle(text.toString().toLowerCase());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}