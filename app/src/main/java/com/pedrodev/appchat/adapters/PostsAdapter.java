package com.pedrodev.appchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.models.Post;
import com.squareup.picasso.Picasso;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {

    // Context
    Context context;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options,Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {
        // Set the content of view in the views of ViewHolder with holder param
        holder.textViewTitle.setText(post.getTitle());
        holder.textViewDescription.setText(post.getDescription());
        if (post.getImage() != null){
            if (!post.getImage().isEmpty()){
                Picasso.with(context).load(post.getImage()).into(holder.imageViewPost);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Instance the class ViewHolder, that class receive a view
        // In this case VIEW HOLDER RECEIVE CARD VIEW POST LIKE A VIEW
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        // Instance view from cardView
        TextView textViewTitle;
        TextView textViewDescription;
        ImageView imageViewPost;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewTitle = view.findViewById(R.id.textViewPostTitle);
            textViewDescription = view.findViewById(R.id.textViewPostDesc);
            imageViewPost = view.findViewById(R.id.imagePostCard);
            viewHolder = view;
        }
    }
}
