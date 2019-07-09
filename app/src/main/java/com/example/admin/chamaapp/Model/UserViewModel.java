package com.example.admin.chamaapp.Model;



import com.example.admin.chamaapp.Model.FirebaseLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel
{


//    Trying editing the userViewModel so that it can also be used with the displaying of events
    private static final DatabaseReference USER_JOURNAL =
            FirebaseDatabase.getInstance().getReference();

    private final FirebaseLiveData liveData = new FirebaseLiveData(USER_JOURNAL);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }
    public FirebaseLiveData getLiveData()
    {
        return liveData;
    }
}
