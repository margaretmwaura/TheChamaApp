package com.example.admin.chamaapp;


/**
 * Created by TOSHIBA on 5/4/2018.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Admin
{
    public String type;
    public String position;
    public int AdminId;
    public String emailAddress;

    Admin()
    {

    }

    Admin(String type,String position,int Id,String emailAddress)
    {
        this.type=type;
        this.position=position;
        this.AdminId = Id;
        this.emailAddress = emailAddress;
    }
    public String getType()
    {
        return this.type;
    }
    public int getID( )
    {
        return this.AdminId;
    }
    public String getPosition(){return  this.position;}
}

