package com.example.admin.chamaapp.Presenter;

import com.example.admin.chamaapp.Model.Chat;

import java.util.List;

public interface AdminOnlyContractMember
{
    public interface AdminOnlyViewMember
    {
        void populateAdapter(List<Chat> chatList);
    }
    interface AdminOnlyPresenterMember
    {
        void readFromDatabase();
    }
}
