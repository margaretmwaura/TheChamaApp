package com.example.admin.chamaapp.admin.Presenter;

import com.example.admin.chamaapp.Admin;

public interface AdminFragmentContract {

    interface AdminFragmentView {
        //Methods for View
        void onAddUser(Admin maggie , String userId);
    }

    interface AdminFragmentPresenter
    {
        void doAddUser();

    }
}
