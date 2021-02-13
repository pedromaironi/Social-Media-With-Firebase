package com.pedrodev.appchat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pedrodev.appchat.models.Comment;

import java.util.Collection;
import java.util.Collections;

public class commentProvider {

    CollectionReference mCollection;

    public commentProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Comments");
    }

    public Task<Void> create(Comment comment){
        return mCollection.document().set(comment);
    }

    public Query getCommentByPost(String idPost){
        return mCollection.whereEqualTo("idPost", idPost);
    }


}
