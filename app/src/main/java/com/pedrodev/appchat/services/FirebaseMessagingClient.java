package com.pedrodev.appchat.services;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pedrodev.appchat.activities.channel.NotificationHelper;

import java.util.Map;

public class FirebaseMessagingClient extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

    }

    @Override
     public void onMessageReceived(@NonNull RemoteMessage remoteMessage){
         super.onMessageReceived(remoteMessage);
         // Information received from notification
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        if (title != null){
            showNotification(title,body);
        }
     }

     private void showNotification(String title, String body){
         NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
         NotificationCompat.Builder builder = notificationHelper.getNotification(title, body);
         notificationHelper.getManager().notify(1, builder.build());
     }


}
