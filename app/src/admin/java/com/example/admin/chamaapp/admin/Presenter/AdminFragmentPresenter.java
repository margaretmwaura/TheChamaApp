package com.example.admin.chamaapp.admin.Presenter;

import com.example.admin.chamaapp.Admin;

public class AdminFragmentPresenter implements AdminFragmentContract.AdminFragmentPresenter
{

    AdminFragmentContract.AdminFragmentView adminFragmentView;
    Admin admin;
    String id;

    public AdminFragmentPresenter(AdminFragmentContract.AdminFragmentView adminFragmentView , Admin maggie , String userId)
    {
        this.adminFragmentView = adminFragmentView;
        this.admin = maggie;
        this.id = userId;
    }
    @Override
    public void doAddUser()
    {
        if (adminFragmentView != null)
        {
            adminFragmentView.onAddUser(admin , id);
        }
    }
}
