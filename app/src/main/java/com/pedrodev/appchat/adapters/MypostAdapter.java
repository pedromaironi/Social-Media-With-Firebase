package com.pedrodev.appchat.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import com.pedrodev.appchat.R;
import com.pedrodev.appchat.activities.postDetailActivity;
import com.pedrodev.appchat.models.Post;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.LikesProvider;
import com.pedrodev.appchat.providers.UsersProvider;
import com.pedrodev.appchat.providers.postProvider;
import com.pedrodev.appchat.utils.RelativeTime;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MypostAdapter extends FirestoreRecyclerAdapter<Post, MypostAdapter.ViewHolder> {

    // Context
    Context context;
    UsersProvider mUsersProvider;
    postProvider mPostProvider;
    LikesProvider mLikesProvider;
    RecyclerView mRecyclerView;

    AuthProvider mAuthProvider;
    public MypostAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        mUsersProvider = new UsersProvider();
        mLikesProvider = new LikesProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new postProvider();

    }

    // Set data for each view
    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post post) {

        // Position is an index
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String postId = documentSnapshot.getId();
        //final String idUser = documentSnapshot.getString("idUser");
        // Set the content of view in the views of ViewHolder with holder param
        String relativeTime = RelativeTime.getTimeAgo(post.getTimestamp(), context);
        //Toast.makeText(context, relativeTime, Toast.LENGTH_LONG).show();

        holder.textViewTime.setText(relativeTime);

        holder.textViewTitle.setText(post.getTitle().toUpperCase());

        if(post.getIdUser().equals(mAuthProvider.getUid()))
        {
            holder.mImageViewDelete.setVisibility(View.VISIBLE);
        }else{
            holder.mImageViewDelete.setVisibility(View.GONE);
        }
        if (post.getImage1() != null){
            if (!post.getImage1().isEmpty()){
                Picasso.with(context).load(post.getImage1()).into(holder.circleImageViewMyPost);
            }
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // To detail
                Intent intentDetail = new Intent(context, postDetailActivity.class);
                intentDetail.putExtra("id",postId);
                context.startActivity(intentDetail);
            }
        });

        holder.mImageViewDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showConfirmDelete(postId);
            }
        });
    }

    private void showConfirmDelete(final String postId) {
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_warn)
                .setTitle("Eliminar publicación")
                .setMessage("¿Estas seguro de realizar esta acción?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost(postId);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        
    }

    private void deletePost(String postId) {
        mPostProvider.delete(postId).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(context, "El post ha sido eliminado correctamente",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "El post no ha podido ser eliminado correctamente",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Instance the class ViewHolder, that class receive a view
        // In this case VIEW HOLDER RECEIVE CARD VIEW POST LIKE A VIEW
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mypost, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        // Instance view from cardView
        TextView textViewTitle;
        TextView textViewTime;

        CircleImageView circleImageViewMyPost;
        ImageView mImageViewDelete;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewTitle = view.findViewById(R.id.textViewTitleMyPost);
            textViewTime = view.findViewById(R.id.RelativeTimeMyPost);
            mImageViewDelete = view.findViewById(R.id.btnMyPostCancel);
            circleImageViewMyPost = view.findViewById(R.id.CircleImageMyPost);

            viewHolder = view;
        }
    }
}
