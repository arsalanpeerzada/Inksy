package com.inksy.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.inksy.R;
import com.inksy.UI.Activities.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private final String SHARD_PREF = "com.inksy.gcmcloudmessaging";
    private final Context mContext = this;
    private final String NOTIFICATION_ENABLED = "NOTIFICATION_ENABLED";
    String title, alert;
    Intent notificationIntent;
    //PendingIntent intent;

    private Notification.Builder mBuilder;


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
      /*  FCMTokenRefresh fcmTokenRefresh = new FCMTokenRefresh(AnalyticsSampleApp.getAppContext());
        fcmTokenRefresh.sendTokenToServer(token);*/
    }


    /**
     * This message will get called
     * whenever SMS received from FCM
     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();
        Log.d("MSG", "onMessageReceived: " + data + " And " + from);

        JSONObject jsonObject = new JSONObject();
        Set<String> keys = remoteMessage.getData().keySet();
        for (String key : keys) {
            try {
                jsonObject.put(key, remoteMessage.getData().get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            SharedPreferences appPrefs = mContext.getSharedPreferences(SHARD_PREF, Context.MODE_PRIVATE);

            if (jsonObject.has("title")) {
                title = jsonObject.getString("title");
            }

            if (jsonObject.has("alert")) {
                alert = jsonObject.getString("alert");
            }

            sendNotification(alert, title);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void sendNotification(final String alert, final String title) {

        PendingIntent pendingIntent = null;
        Long getCurrentTime = System.currentTimeMillis();
        int id = Math.abs(getCurrentTime.intValue());

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("ComesFromNotification", "True");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);


        String channelId = getString(R.string.notification_channel_general);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Inksy");
        bigTextStyle.bigText(alert);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(alert)
                        .setAutoCancel(true)
                        .setStyle(bigTextStyle)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Inksy Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify("com.inksy", id, notificationBuilder.build());

    }


    private void createNotificationChannel() {

        String CHANNEL_ID = getResources().getString(R.string.notification_channel_general);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.enableVibration(true);
            channel.setDescription(description);
            channel.enableLights(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
