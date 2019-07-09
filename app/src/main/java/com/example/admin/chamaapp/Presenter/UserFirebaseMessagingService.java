package com.example.admin.chamaapp.Presenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;

import android.util.Log;

import com.example.admin.chamaapp.View.NotificationBroadCastReceiver;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.admin.chamaapp.Presenter.Backgroundactivities.returnNotificationEventTime;

public class UserFirebaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";
    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        String token = FirebaseInstanceId.getInstance().getToken();

        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        Log.i(TAG, "onTokenRefresh completed with token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d("MessageReceived","The message is from " + remoteMessage.getFrom());


        Intent intent = new Intent(this, NotificationBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long now = SystemClock.elapsedRealtime();
        long time = returnNotificationEventTime();
        Log.d("NotificationEventTime","This is how long we will wait inorder to receive the notification" + time);
        long alarmtime = now + 120000L;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME,alarmtime,pendingIntent);
    }


}
