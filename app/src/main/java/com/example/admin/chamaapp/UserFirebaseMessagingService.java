package com.example.admin.chamaapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.admin.chamaapp.Backgroundactivities.returnNotificationEventTime;

public class UserFirebaseMessagingService extends FirebaseMessagingService
{

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d("MessageReceived","The message is from " + remoteMessage.getFrom());



//       Will set up the alarm manager so that it calls the notification method after that
//        Amount of time


        Intent intent = new Intent(this,NotificationBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long now = SystemClock.elapsedRealtime();
        long time = returnNotificationEventTime();
        Log.d("NotificationEventTime","This is how long we will wait inorder to receive the notification" + time);
        long alarmtime = now + 120000L;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME,alarmtime,pendingIntent);
    }


}
