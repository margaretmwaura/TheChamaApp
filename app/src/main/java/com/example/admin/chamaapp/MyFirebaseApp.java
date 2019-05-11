package com.example.admin.chamaapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseApp extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
