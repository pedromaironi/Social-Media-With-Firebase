package com.pedrodev.appchat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pedrodev.appchat.models.Like;

public class LikesProvider {

    private CollectionReference mCollectionReference;
    public LikesProvider(){
        mCollectionReference = FirebaseFirestore.getInstance().collection("Likes");
    }

    public Task<Void> create(Like like){
        DocumentReference document = mCollectionReference.document();
        String id = document.getId();
        like.setIdDocument(id);
        return mCollectionReference.document().set(like);
    }

    public Query getLikesByPost(String idPost){
        return mCollectionReference.whereEqualTo("idPost", idPost);
    }

    public Query getLikeByPostAndUser(String idPost, String idUser){
        return mCollectionReference.whereEqualTo("idPost",idPost).whereEqualTo("idUser",idUser);
    }


    public Task<Void> deleteLike(String id){
        return mCollectionReference.document(id).delete();
    }
}
