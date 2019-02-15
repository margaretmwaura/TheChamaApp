package com.example.admin.chamaapp;


/**
 * Created by TOSHIBA on 5/4/2018.
 */
import android.arch.persistence.room.Entity;

import com.google.firebase.database.IgnoreExtraProperties;

@Entity(tableName = "Member")
@IgnoreExtraProperties
public class Member
{
    public int membershipID;
    public String type;
    public String emailAddress;
    public String userId;
    public int attendance;
    public int contribution;
    Member()
    {

    }
//This constructor is important for the firebase saving of data
    Member(int membershipID,String type,String emailAddress)
    {
        this.type=type;
        this.membershipID=membershipID;
        this.emailAddress = emailAddress;
        this.attendance = 0;
        this.contribution = 0;
    }

//    This constructor is important for the Room database saving of data
    Member(int membershipID, String type, String emailAddress,String userId)
    {
        this.type=type;
        this.membershipID=membershipID;
        this.emailAddress = emailAddress;
        this.userId = userId;
    }
    public String getType()
    {
        return this.type;
    }
    public int getMembershipID()
    {
        return this.membershipID;
    }
    public String getEmailAddress()
    {
        return this.emailAddress;
    }
    public String getUserId()
    {
        return this.userId;
    }
    public int getAttendance()
    {
        return this.attendance;
    }
    public int getContribution()
    {
        return this.contribution;
    }
}

