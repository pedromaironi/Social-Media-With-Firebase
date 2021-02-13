package com.pedrodev.appchat.models;

public class Like {

    private String idPost;
    private String idUser;
    private String idDocument;
    private long timestamp;

    public Like(){

    }

    public Like(String idPost, String idUser, String idDocument, long timestamp) {
        this.idPost = idPost;
        this.idUser = idUser;
        this.idDocument = idDocument;
        this.timestamp = timestamp;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
