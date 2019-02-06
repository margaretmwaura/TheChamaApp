package com.example.admin.chamaapp;


/**
 * Created by TOSHIBA on 5/4/2018.
 */
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Member
{
    public int membershipID;
    public String type;

    Member()
    {

    }

    Member(int membershipID,String type)
    {
        this.type=type;
        this.membershipID=membershipID;
    }
    public String getType()
    {
        return this.type;
    }
    public int getMembershipID()
    {
        return this.membershipID;
    }
}

