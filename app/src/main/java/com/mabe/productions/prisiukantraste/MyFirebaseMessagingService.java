package com.mabe.productions.prisiukantraste;

/**
 * Created by Benas on 24/06/2017.
 */


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "TEST";

    private SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sharedPreferences = getSharedPreferences("user_date", MODE_PRIVATE);

        Map data = remoteMessage.getData();
        int type = (Integer) data.get("type");
        String title = (String) data.get("title");
        String message = (String) data.get("message");
        String url = (String) data.get("url");
        String date = (String) data.get("date");

        switch (type){
            case ChooseNewspapper.TYPE_15MIN:
                if(sharedPreferences.getBoolean("15min_state", true)){
                    sendNotification(title, message, url, date, R.drawable.min15_logo);
                }
                break;

            case ChooseNewspapper.TYPE_DELFI:
                if(sharedPreferences.getBoolean("delfi_state", true)){
                    sendNotification(title, message, url, date, R.drawable.delfi_logo);
                }
                break;

            case ChooseNewspapper.TYPE_ALFA:
                if(sharedPreferences.getBoolean("alfa_state", true)){
                    sendNotification(title, message, url, date, R.drawable.alfa_logo);
                }
                break;

            case ChooseNewspapper.TYPE_LRYTAS:
                if(sharedPreferences.getBoolean("lrytas_state", true)){
                    sendNotification(title, message, url, date, R.drawable.lrytas_logo);
                }
                break;

        }


    }


    private void sendNotification(String title, String message, String url, String date, int type) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        intent.putExtra("date", CheckingUtils.getDateInMillis(date));
        intent.putExtra("url", url);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon_black_single_patch)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.min15_logo))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }
}
