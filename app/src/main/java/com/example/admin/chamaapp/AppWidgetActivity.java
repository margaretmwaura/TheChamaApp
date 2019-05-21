package com.example.admin.chamaapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;

public class AppWidgetActivity extends AppWidgetProvider
{
    private static RemoteViews views;
//    This method is for updating a single widget

    //    Call this method in the method that is being called in the static method that is being called in the activity
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Bundle info) {


        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.app_widget_activity);


        setTheUiData(info,context);
//        This is where I will set the information of the views
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //    This is the method that will be called in the details activity
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

    //    This method will be called in the updateAppWidget method
//    Will create a method that will be populating the widget view with the current step and ingredient data
    public static void setTheUiData( Bundle info,Context context)
    {

//     Retrieve all the data in the bundle
        String phonenumber = info.getString("PhoneNumber");
        String contribution = info.getString("Contribution");
        Log.d("Phonenumber","Widget phone number " + phonenumber);
        Log.d("Contribution","Widget contribution " + contribution);

//        Saving the recipe in a shared preference
        SharedPreferences mPrefs = context.getSharedPreferences("UserDetail",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("Phonenumber", phonenumber);
        prefsEditor.putString("Contribution",contribution);
        prefsEditor.commit();
//End of the storing

        views.setTextViewText(R.id.appwidget_phone_number,phonenumber);
        views.setTextViewText(R.id.appwidget_contribution,contribution);


//
        //        Retrieving the value of the shared preference

//        Starting up the intent that will be used to open up the intent
        Intent appIntent = new Intent(context,TheNavigationDrawer.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context,0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.containerLayout,appPendingIntent);

//        End of the code that launches the details activity
    }
}
