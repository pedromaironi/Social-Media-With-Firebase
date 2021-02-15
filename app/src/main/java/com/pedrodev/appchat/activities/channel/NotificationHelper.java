package com.pedrodev.appchat.activities.channel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.pedrodev.appchat.R;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "com.pedrodev.appchat";
    private static final String CHANNEL_NAME = "AppChat";

    private NotificationManager Manager;

    public NotificationHelper(Context context){
        super(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }
    }

    // Esto dice que se debe de ejecutar en una version superior o igual a android oreo
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels(){
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, Manager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager(){
        // Esto significa que no se ha instanciado
        if (Manager == null){
            Manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return Manager;
    }

    public NotificationCompat.Builder getNotification(String title, String body){
        return new NotificationCompat.Builder(
                getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setColor(Color.GRAY)
                .setSmallIcon(R.drawable.logo)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));
    }
}
