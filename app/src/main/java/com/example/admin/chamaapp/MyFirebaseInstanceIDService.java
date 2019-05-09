package com.example.admin.chamaapp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";
    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        Log.i(TAG, "onTokenRefresh completed with token: " + token);
    }
}
