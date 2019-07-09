package com.example.admin.chamaapp.Model;

import android.app.Application;

import com.example.admin.chamaapp.Model.Member;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RoomViewModel extends AndroidViewModel
{
    private LiveData<Member> memberLiveData;
    public RoomViewModel(@NonNull Application application)
    {
        super(application);

    }
}
