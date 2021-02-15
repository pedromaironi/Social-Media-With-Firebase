package com.pedrodev.appchat.providers;

import com.pedrodev.appchat.models.FCMBody;
import com.pedrodev.appchat.models.FCMResponse;
import com.pedrodev.appchat.retrofit.IFCMApi;
import com.pedrodev.appchat.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider(){

    }

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }
    
    public NotificationProvider(String url) {
        this.url = url;
    }
}
