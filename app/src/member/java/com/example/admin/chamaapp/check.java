package com.example.admin.chamaapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


public class check extends Activity
{
    boolean isUserLoggedIn = true;
    // User Session Manager Class
    UserSessionManager session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        The below code is for allowing the splash screen to only show once
        SharedPreferences settings=getSharedPreferences("prefs",0);
        boolean firstRun=settings.getBoolean("firstRun",false);
        if(firstRun == false)
            //if running for first time
        //Splash will load for first time
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.commit();
            Intent i=new Intent(check.this,Welcome.class);
            startActivity(i);
            finish();
        }
        else
        {
//            This checks whether the user had logged in before and if they had they are directed to a different activity
            session = new UserSessionManager(getApplicationContext());
            isUserLoggedIn = session.isUserLoggedIn();

            Log.d("ValueOfUserLogIn","This is status of the user log in " + isUserLoggedIn);
            if(isUserLoggedIn)
            {
                Toast.makeText(this,"The user has been logged in ", Toast.LENGTH_LONG).show();
                Log.d("UserLoggedInBefore","The user has been logged in before this ");
                Intent intent = new Intent(check.this, TheNavigationDrawer.class);
                startActivity(intent);
                finish();
            }
            else
                {
                    Log.d("UserNotLoggedInBefore","The user has not been logged in before this ");
                Intent a = new Intent(check.this, Sign.class);
                startActivity(a);
                finish();
            }
        }
    }
}
