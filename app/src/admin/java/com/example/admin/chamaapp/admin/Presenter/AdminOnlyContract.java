package com.example.admin.chamaapp.admin.Presenter;

import com.example.admin.chamaapp.Chat;

import java.util.List;

public interface AdminOnlyContract
{

    interface AdminOnlyView
    {
        void addChat();
        void populateAdapteer(List<Chat> chatList);
    }
    interface AdminOnlyPresenter
    {
        void readFromDatabase();
    }
}
