package com.pedrodev.appchat.retrofit;

import com.pedrodev.appchat.models.FCMBody;
import com.pedrodev.appchat.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAkZ7yxqg:APA91bErBtMh3wfEwvUABJjnH73JjJt_iNptcq_aXBp_WCpQH_w9ijKQT_DbBDFftYE010hqklWhabAOxbBY2MspHRvi_RIXFvKVwBk98apnMBBLLV1XO2469pgtajLtvFw6FQuGFdk0"
    })
    @POST("fcm/send")

    Call<FCMResponse> send(@Body FCMBody body);
}
