package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserViewModel extends ViewModel
{
    private static final DatabaseReference USER_JOURNAL =
            FirebaseDatabase.getInstance().getReference().child("users");

    private final FirebaseLiveData liveData = new FirebaseLiveData(USER_JOURNAL);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }
}
