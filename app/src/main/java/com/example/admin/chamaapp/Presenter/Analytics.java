package com.example.admin.chamaapp.Presenter;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics
{
    public static void addAdminChatAnalysis(Context context, String event, Bundle bundle)
    {
        FirebaseAnalytics.getInstance(context).logEvent(event,bundle);
    }
    public static void addGeneralChatAnalysis(Context context, String event, Bundle bundle)
    {
        FirebaseAnalytics.getInstance(context).logEvent(event,bundle);
    }
    public static void addEventAnalysis(Context context, String event, Bundle bundle)
    {
        FirebaseAnalytics.getInstance(context).logEvent(event,bundle);
    }
}
