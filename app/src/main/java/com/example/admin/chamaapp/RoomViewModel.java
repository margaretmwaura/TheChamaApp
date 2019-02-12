package com.example.admin.chamaapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class RoomViewModel extends AndroidViewModel
{
    private LiveData<Member> memberLiveData;
    public RoomViewModel(@NonNull Application application)
    {
        super(application);

    }
}
