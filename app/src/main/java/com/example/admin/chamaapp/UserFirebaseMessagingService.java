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

//                sendNotification(this);

        Intent intent = new Intent(this,NotificationBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long now = SystemClock.elapsedRealtime();
        long time = returnNotificationEventTime();
        Log.d("NotificationEventTime","This is how long we will wait inorder to receive the notification" + time);
        long alarmtime = now + 120000;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME,alarmtime,pendingIntent);
    }
//    public void sendNotification(Context context)
//    {
//        try {
//            Intent intent = new Intent(context, EventActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            // Create the pending intent to launch the activity
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
//                    PendingIntent.FLAG_ONE_SHOT);
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationManager notificationManager;
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "default")
//                    .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
//                    .setContentTitle("Journal notification")
//                    .setContentText("It is time to act")
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setChannelId("default")
//                    .setContentIntent(pendingIntent);
//
//
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String channelId = getString(R.string.default_notification_channel_id);
//                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable", NotificationManager.IMPORTANCE_DEFAULT);
//                notificationManager =
//                        (NotificationManager) context.getSystemService(NotificationManager.class);
//                channel.setDescription("Channel human readable");
//                notificationManager.createNotificationChannel(channel);
//
//                notificationManager.notify(492 /* ID of notification */, notificationBuilder.build());
//
//                Log.d("NotificationBuilding","The notification has been created");
//            }
//
//
//
//            Log.d("MessageReceived ", "The message has been recieved ");
//        }
//        catch (Exception e)
//        {
//            Log.d("NotificationError","This is the reasin why the notification is not building " + e.getMessage());
//        }
//
//    }


}
