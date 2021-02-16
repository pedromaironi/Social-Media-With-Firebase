package com.pedrodev.appchat.models;

import java.util.ArrayList;

public class Chat {

    private boolean isWriting;
    private Long timestamp;
    private String idUser1;
    private String idUser2;
    private String id;
    private ArrayList<String> ids;


    public Chat() {

    }

    public Chat(boolean isWriting, Long timestamp, String idUser1, String idUser2, String id, ArrayList<String> ids) {
        this.isWriting = isWriting;
        this.timestamp = timestamp;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.id = id;
        this.ids = ids;
    }

    public boolean isWriting() {
        return isWriting;
    }

    public void setWriting(boolean writing) {
        isWriting = writing;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }

    public String getid() {
        return id;
    }

    public void setid(String idChat) {
        this.id = id;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }
}