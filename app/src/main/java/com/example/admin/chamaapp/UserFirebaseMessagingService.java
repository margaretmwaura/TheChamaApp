package com.example.admin.chamaapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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
//        Amount of timeg
//
//        sendNotification(this);

        Intent intent = new Intent(this,NotificationBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long now = SystemClock.elapsedRealtime();
        long time = 60*1000;
        long alarmtime = now + time;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME,alarmtime,pendingIntent);
    }
}
