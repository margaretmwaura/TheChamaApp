package com.example.admin.chamaapp.Presenter;

import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class TheNavigationDrawerPresenter
{
    public View view;

    public TheNavigationDrawerPresenter(View view)
    {
        this.view = view;
    }

    public String[] getUserDetails ()
    {
        //        This is reading the email from the file

        String[] details = new String[2];
        String detail;

        File directory = view.getTheActivityFile();
        File file = new File(directory,"UserDetails" );

        try {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(in);

            details = new String[2];
            details = (String[]) s.readObject();

            for(int i = 0; i<details.length;i++)
            {
                detail = details[i];
                Log.d("UserDetails","This are the user details " + detail);
            }
            return details;
        }
        catch (Exception e)
        {
            Log.d("ErrorFileReading","Error encountered while reading the file " + e.getMessage());

            return details;

        }

    }
    public interface View
    {
        File getTheActivityFile();
    }
}
