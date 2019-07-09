package com.example.admin.chamaapp;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager
{
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public UserSessionManager(Context paramContext)
    {
        this._context = paramContext;
        this.pref = this._context.getSharedPreferences("AndroidPref", this.PRIVATE_MODE);
        this.editor = this.pref.edit();
    }

    public void createUserLoginSession()
    {
//        Will use this variable to check if the user is logged in to change the starting activity
        this.editor.putBoolean("IsUserLoggedIn", true);
        this.editor.commit();
    }
    public boolean isUserLoggedIn()
    {
        return this.pref.getBoolean("IsUserLoggedIn", false);
    }
}
