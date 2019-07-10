package com.example.admin.chamaapp.View;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.admin.chamaapp.R;


public class AppWidgetActivity extends AppWidgetProvider
{
    private static RemoteViews views;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Bundle info) {

        views = new RemoteViews(context.getPackageName(), R.layout.app_widget_activity);


        setTheUiData(info,context);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void wiringUpTheWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,Bundle info)
    {
        for (int appWidgetId : appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetId,info);
        }
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // There may be multiple widgets active, so update all of them

    }

    @Override
    public void onEnabled(Context context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void setTheUiData( Bundle info,Context context)
    {

        String phonenumber = info.getString("PhoneNumber");
        String contribution = info.getString("Contribution");
        Log.d("Phonenumber","Widget phone number " + phonenumber);
        Log.d("Contribution","Widget contribution " + contribution);

        SharedPreferences mPrefs = context.getSharedPreferences("UserDetail",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("Phonenumber", phonenumber);
        prefsEditor.putString("Contribution",contribution);
        prefsEditor.commit();


        views.setTextViewText(R.id.appwidget_phone_number,phonenumber);
        views.setTextViewText(R.id.appwidget_contribution,contribution);

//        Starting up the intent that will be used to open up the intent
        Intent appIntent = new Intent(context, TheNavigationDrawer.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context,0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.containerLayout,appPendingIntent);


    }
}
