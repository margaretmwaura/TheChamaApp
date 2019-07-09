package com.example.admin.chamaapp.admin.Presenter;


public class LoginPrsenter
{
    private View view;

    public LoginPrsenter(View view)
    {
        this.view = view;
    }


    public interface View
    {
        void showDialogBoxView();
    }
}
