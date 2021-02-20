package com.pedrodev.appchat.activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.pedrodev.appchat.models.FCMBody;
import com.pedrodev.appchat.models.FCMResponse;
import com.pedrodev.appchat.providers.LikesProvider;
import com.pedrodev.appchat.providers.NotificationProvider;
import com.pedrodev.appchat.providers.TokenProvider;
import com.pedrodev.appchat.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.adapters.CommentAdapter;
import com.pedrodev.appchat.adapters.PostsAdapter;
import com.pedrodev.appchat.adapters.SliderAdapter;
import com.pedrodev.appchat.models.Comment;
import com.pedrodev.appchat.models.Post;
import com.pedrodev.appchat.models.SliderItem;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.UsersProvider;
import com.pedrodev.appchat.providers.commentProvider;
import com.pedrodev.appchat.providers.postProvider;
import com.pedrodev.appchat.utils.RelativeTime;
import com.pedrodev.appchat.utils.ViewedMessageHelper;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.security.DomainCombiner;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class postDetailActivity extends AppCompatActivity {

    SliderView mSliderView;
    SliderAdapter mSliderAdapter;
    String mExtraPostId;
    String idUser = "";

    // <Model>
    List<SliderItem> mSliderItems = new ArrayList<>();
    postProvider mPostProvider;
    commentProvider mCommentProvider;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProviders;
    LikesProvider mLikesProvider;
    NotificationProvider mNotificationProvider;
    TokenProvider mTokenProvider;
    // Textview

    TextView mtextViewTitle;
    TextView mtextViewPhone;
    TextView mtextViewDescription;
    TextView mtextViewCategory;
    TextView mTextViewUser;
    TextView mTextViewTime;
    TextView mtextViewLikes;

    // Image view

    CircleImageView mImageViewProfile;
    ImageView mImageCategory;

    //Buttons
    FloatingActionButton mFloatingButtonMessage;
    Button mButtonShowProfile;

    CircleImageView mCircleImageViewBack;
    RecyclerView mRecyclerView;
    CommentAdapter mCommentAdapter;

    ListenerRegistration listenerRegistration;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        mUsersProviders = new UsersProvider();
        mCommentProvider = new commentProvider();
        mAuthProvider = new AuthProvider();
        mLikesProvider = new LikesProvider();
        mNotificationProvider = new NotificationProvider();
        mTokenProvider = new TokenProvider();
        //Intance

        mImageCategory = findViewById(R.id.imageViewCategory);
        mImageViewProfile = findViewById(R.id.imgCircleViewProfile);

        mtextViewDescription = findViewById(R.id.textViewDescription);
        mtextViewPhone = findViewById(R.id.textViewPhoneNumberPostDetail);
        mtextViewTitle = findViewById(R.id.editTextTitlePost);
        mtextViewCategory = findViewById(R.id.editTextCategory);
        mTextViewUser = findViewById(R.id.textViewNameUser);
        mButtonShowProfile = findViewById(R.id.btnViewProfile);
        mFloatingButtonMessage = findViewById(R.id.floatingButtonMessage);
        mTextViewTime = findViewById(R.id.textViewRelativeTime);
        mtextViewLikes = findViewById(R.id.textViewLikes);

        mPostProvider = new postProvider();
        mExtraPostId = getIntent().getStringExtra("id");
        mSliderView = findViewById(R.id.imageSlider);

//        mCircleImageViewBack = findViewById(R.id.circleImageProfile);
        mRecyclerView = findViewById(R.id.RecyclerViewComments);

        // Show itemns in lines
        LinearLayoutManager layoutManager = new LinearLayoutManager(postDetailActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);

        mButtonShowProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToProfileFromPosts();
            }
        });

//        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        // Comments
        mFloatingButtonMessage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showDialogComment();
            }
        });
        mToolbar = findViewById(R.id.Toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getPost();
        getLikesNumber();
    }

    private void getLikesNumber() {
        listenerRegistration = mLikesProvider.getLikesByPost(mExtraPostId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numberLikes = value.size();
                switch(numberLikes){
                    case 0:
                        mtextViewLikes.setText(String.valueOf("No tiene me gustas"));
                        break;
                    case 1:
                        mtextViewLikes.setText(String.valueOf(numberLikes + " Me gusta"));
                        break;
                    default:
                        mtextViewLikes.setText(String.valueOf(numberLikes + " Me gustas"));
                        break;
                }
            }
        });
    }

    /*
    NO TIME REAL
    mLikesProvider.getLikesByPost(mExtraPostId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberLikes = queryDocumentSnapshots.size();
                switch(numberLikes){
                        case 0:
                            mtextViewLikes.setText("No tiene me gustas");
                        break;
                        case 1:
                            mtextViewLikes.setText(numberLikes + " Me gusta");
                        break;
                    default:
                        mtextViewLikes.setText(numberLikes + " Me gustas");
                        break;
                }
            }
        });
     */
    @Override
    protected void onStart() {
        super.onStart();

        Query query = mCommentProvider.getCommentByPost(mExtraPostId);
        // Type post model
        FirestoreRecyclerOptions<Comment> options =
                new FirestoreRecyclerOptions
                        .Builder<Comment>()
                        .setQuery(query,Comment.class)
                        .build();
        mCommentAdapter = new CommentAdapter(options, postDetailActivity.this);
        mRecyclerView.setAdapter(mCommentAdapter);
        mCommentAdapter.startListening();
        ViewedMessageHelper.updateOnline(true, postDetailActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, postDetailActivity.this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        mCommentAdapter.stopListening();
    }

    private void showDialogComment() {
        AlertDialog.Builder alert = new AlertDialog.Builder(postDetailActivity.this);
        alert.setTitle("¡COMENTARIO!");
        alert.setMessage("Ingresa tu comentario");

        final EditText editText = new EditText(postDetailActivity.this);
        editText.setHint("Texto");

       // alert.setView(editText);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(36, 0, 36, 36);
        editText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(postDetailActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
);
        container.setLayoutParams(relativeParams);
        container.addView(editText);

        alert.setView(container);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString();
                if(!value.isEmpty()){
                    createComment(value);
                }else{
                    Toast.makeText(postDetailActivity.this,"Ingresa un comentario",Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String value = editText.getText().toString();
            }
        });
        
        alert.show();
    }

    private void createComment(final String value) {
        Comment comment = new Comment();
        comment.setComment(value);
        comment.setIdPost(mExtraPostId);
        comment.setIdUser(mAuthProvider.getUid());
        comment.setTimestamp(new Date().getTime());
        mCommentProvider.create(comment).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sendNotification(value);

                Toast.makeText(postDetailActivity.this,"Comentario creado correctamente",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendNotification(final String comment){
        if(idUser!=null){

        mTokenProvider.getToken(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Map<String, String> data = new HashMap<>();
                    data.put("title", "Han comentado tu post");
                    data.put("body",comment);
                    String token = documentSnapshot.getString("token");
                    FCMBody body = new FCMBody(token,"high","4500s", data);
                    mNotificationProvider.sendNotification(body).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() !=null){
                                if(response.body().getSuccess() == 1){
                                    Toast.makeText(postDetailActivity.this, "Notificacion enviada",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(postDetailActivity.this, "Notificacion no enviada",Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {

                        }
                    });

                }else{
                    Toast.makeText(postDetailActivity.this, "Favor volver a iniciar sesión",Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

    }

    private void goToProfileFromPosts() {
        if (!idUser.equals("")) {
            Intent intent = new Intent(postDetailActivity.this, UserProfileActivity.class);
            intent.putExtra("idUser",idUser);
            startActivity(intent);
        }
    }

    private void instanceSlider() {
        mSliderAdapter = new SliderAdapter(postDetailActivity.this, mSliderItems);
        mSliderView.setSliderAdapter(mSliderAdapter);
        mSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        mSliderView.setIndicatorSelectedColor(Color.WHITE);
        mSliderView.setIndicatorUnselectedColor(Color.GRAY);
        mSliderView.setScrollTimeInSec(5);
        mSliderView.setAutoCycle(true);
        mSliderView.startAutoCycle();
    }

    private void getPost(){
        mPostProvider.getPostById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("image1")) {
                        String imageFirst = documentSnapshot.getString("image1");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(imageFirst);
                        mSliderItems.add(item);
                    }
                    if (documentSnapshot.contains("image2")) {
                        String imageSecond = documentSnapshot.getString("image2");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(imageSecond);
                        mSliderItems.add(item);
                    }
                    if (documentSnapshot.contains("title"))
                    {
                        String title = documentSnapshot.getString("title");
                        mtextViewTitle.setText(title.toUpperCase());
                    }
                    if(documentSnapshot.contains("description")){
                        String desc = documentSnapshot.getString("description");
                        mtextViewDescription.setText(desc);
                    }

                    if(documentSnapshot.contains("category")){
                        String cat = documentSnapshot.getString("category");
                        if(cat.equals("NEWS")) {
                           mImageCategory.setImageResource(R.drawable.ic_bug);
                        } else if(cat.equals("PC")){
                            mImageCategory.setImageResource(R.drawable.ic_computer);
                        }
                        else if(cat.equals("GAMES")){
                            mImageCategory.setImageResource(R.drawable.ic_baseline_games_24);
                        }
                        mtextViewCategory.setText(cat);
                    }
                    if(documentSnapshot.contains("idUser")){
                        idUser = documentSnapshot.getString("idUser");
                        getUserInfo(idUser);
                    }if(documentSnapshot.contains("timestamp")){
                        long time = documentSnapshot.getLong("timestamp");
                        String RelativeTime = com.pedrodev.appchat.utils.RelativeTime.getTimeAgo(time, postDetailActivity.this);
                        mTextViewTime.setText(RelativeTime);
                    }
                    instanceSlider();
                }
            }
        });
    }

    private void getUserInfo(final String idUser) {
        mUsersProviders.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username").toUpperCase();
                        mTextViewUser.setText(username);
                    }
                    if(documentSnapshot.contains("phoneNumber")){
                        String phone = documentSnapshot.getString("phoneNumber");
                        mtextViewPhone.setText(phone);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String img = documentSnapshot.getString("image_profile");
                        Picasso.with(postDetailActivity.this).load(img).into(mImageViewProfile);
                    }
                }
            }

        });
    }
}