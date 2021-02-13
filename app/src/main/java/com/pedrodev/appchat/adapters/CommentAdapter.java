package com.pedrodev.appchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.activities.postDetailActivity;
import com.pedrodev.appchat.models.Comment;
import com.pedrodev.appchat.models.Post;
import com.pedrodev.appchat.providers.UsersProvider;
import com.squareup.picasso.Picasso;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, CommentAdapter.ViewHolder> {

    // Context
    Context context;

    UsersProvider mUsersProvider;
    public CommentAdapter(FirestoreRecyclerOptions<Comment> options, Context context){
        super(options);
        mUsersProvider = new UsersProvider();
        this.context = context;
    }

    // set data
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment comment) {
        // Position is an index
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String commentId = documentSnapshot.getId();
        String idUser = documentSnapshot.getString("idUser");

        // Set the content of view in the views of ViewHolder with holder param
        holder.textViewComments.setText(comment.getComment());

        getUserInfo(idUser, holder);

    }

    private void getUserInfo(String idUser, final ViewHolder holder)
    {
        mUsersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    if(documentSnapshot.contains("username"))
                    {
                        String username = documentSnapshot.getString("username");
                        holder.textViewUsername.setText(username);
                    }

                    if(documentSnapshot.contains("image_profile"))
                    {
                        String img = documentSnapshot.getString("image_profile");
                        if(img!=null)
                        {
                            if (!img.isEmpty()) {
                                Picasso.with(context).load(img).into(holder.imageViewComment);
                            }
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Instance the class ViewHolder, that class receive a view
        // In this case VIEW HOLDER RECEIVE CARD VIEW POST LIKE A VIEW
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comments, parent, false);

        return new ViewHolder(view);
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        // Instance view from cardView
        TextView textViewUsername;
        TextView textViewComments;
        ImageView imageViewComment;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewUsername = view.findViewById(R.id.textViewUserName);
            textViewComments = view.findViewById(R.id.textViewComments);
            imageViewComment = view.findViewById(R.id.circleimagecomments);
            viewHolder = view;
        }
    }
}
