package com.pedrodev.appchat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.activities.postDetailActivity;
import com.pedrodev.appchat.models.Like;
import com.pedrodev.appchat.models.Post;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.LikesProvider;
import com.pedrodev.appchat.providers.UsersProvider;
import com.pedrodev.appchat.providers.postProvider;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.ResourceBundle;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {

    // Context
    Context context;
    UsersProvider mUsersProvider;
    LikesProvider mLikesProvider;

    AuthProvider mAuthProvider;
    public PostsAdapter(FirestoreRecyclerOptions<Post> options,Context context){
        super(options);
        this.context = context;
        mUsersProvider = new UsersProvider();
        mLikesProvider = new LikesProvider();
        mAuthProvider = new AuthProvider();
    }

    // Set data for each view
    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post post) {

        // Position is an index
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String postId = documentSnapshot.getId();
        //final String idUser = documentSnapshot.getString("idUser");
        // Set the content of view in the views of ViewHolder with holder param
        holder.textViewTitle.setText(post.getTitle().toUpperCase());
        holder.textViewDescription.setText(post.getDescription());

        if (post.getImage1() != null){
            if (!post.getImage1().isEmpty()){
                Picasso.with(context).load(post.getImage1()).into(holder.imageViewPost);
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

        holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like like = new Like();
                like.setIdUser(mAuthProvider.getUid());
                like.setIdPost(postId);
                like.setTimestamp(new Date().getTime());
                // Will allow us to access our views
                likePost(like, holder);
            }
        });

      getUserInfo(mAuthProvider.getUid(), holder);
      getNumberLikesByPost(postId, holder);
        checkIfExistsLike(postId,mAuthProvider.getUid(),holder);
    }

    private void getNumberLikesByPost(String idPost, final ViewHolder holder){
        // allows get info real time
        mLikesProvider.getLikesByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numberLikes = value.size();
                switch(numberLikes){
                    case 0:
                        holder.textViewLikes.setText("0 Me gustas");
                        break;
                    case 1:
                        holder.textViewLikes.setText(String.valueOf(numberLikes) + " Me gusta");
                        break;
                    default:
                        holder.textViewLikes.setText(String.valueOf(numberLikes) + " Me gustas");
                        break;
                }

            }
        });
    }

    private void checkIfExistsLike(String idPost, String idUser, final ViewHolder vh) {
        mLikesProvider.getLikeByPostAndUser(idPost,idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocument = queryDocumentSnapshots.size();
                if (numberDocument > 0) {
                    vh.imageViewLike.setImageResource(R.drawable.like_blue);
                } else {
                    vh.imageViewLike.setImageResource(R.drawable.like_gray);
                }
            }
        });
    }
    private void likePost(final Like like, final ViewHolder vh) {
        mLikesProvider.getLikeByPostAndUser(like.getIdPost(), like.getIdUser()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocument = queryDocumentSnapshots.size();
                if (numberDocument > 0) {
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    vh.imageViewLike.setImageResource(R.drawable.like_gray);
                    mLikesProvider.deleteLike(idLike);
                } else {
                    vh.imageViewLike.setImageResource(R.drawable.like_blue);
                    mLikesProvider.create(like);
                }

            }
        });

    }

    private void getUserInfo(final String idUser, final ViewHolder holder) {
        mUsersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        assert username != null;
                        holder.textViewUsername.setText(username.toUpperCase());
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        // Instance view from cardView
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewUsername;
        TextView textViewLikes;
        ImageView imageViewPost;
        ImageView imageViewLike;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewLikes = view.findViewById(R.id.textViewLikes);
            imageViewLike = view.findViewById(R.id.imageViewLike);
            textViewTitle = view.findViewById(R.id.textViewPostTitle);
            textViewDescription = view.findViewById(R.id.textViewPostDesc);
            imageViewPost = view.findViewById(R.id.imagePostCard);
            textViewUsername = view.findViewById(R.id.textViewUserNamePostCard);

            viewHolder = view;
        }
    }
}
