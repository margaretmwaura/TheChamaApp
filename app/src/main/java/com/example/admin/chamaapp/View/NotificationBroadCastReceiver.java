package com.example.admin.chamaapp.View;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;

import com.example.admin.chamaapp.R;


import androidx.core.app.NotificationCompat;

public class NotificationBroadCastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("NotificationReceiver","The Notification is sent after a set time ");
        sendNotification(context);

    }

    public void sendNotification(Context context)
    {
        try {
            Intent intent = new Intent(context, EventActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Create the pending intent to launch the activity
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager notificationManager;
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                    .setContentTitle("Event notification")
                    .setContentText("It is time to act")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
//                    This channel has to correspond with the one set at the manifest
                    .setChannelId(context.getString(R.string.default_notification_channel_id))
                    .setContentIntent(pendingIntent);

            notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelId = context.getString(R.string.default_notification_channel_id);
                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable", NotificationManager.IMPORTANCE_HIGH);

                channel.setDescription("Channel human readable");
                notificationManager.createNotificationChannel(channel);



                Log.d("NotificationBuilding","The notification has been created");
            }
            try
            {
                notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
                Log.d("CreatingTheNotification","The notification had been created with no errors encounters");
            }
            catch (Exception e)
            {
                Log.d("CreatingTheNotification","The error creating the notification is the following " + e.getMessage());
            }


            Log.d("MessageReceived ", "The message has been recieved ");
        }
        catch (Exception e)
        {
            Log.d("NotificationError","This is the reasin why the notification is not building " + e.getMessage());
        }

    }

}
