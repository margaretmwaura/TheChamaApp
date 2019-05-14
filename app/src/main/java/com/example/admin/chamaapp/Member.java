package com.example.admin.chamaapp;


/**
 * Created by TOSHIBA on 5/4/2018.
 */
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Member")
@IgnoreExtraProperties
public class Member implements Parcelable
{
    public int membershipID;
    public String type;
    public String phonenumber;
    public String userId;
    public int attendance;
    public Contribution contribution;
    Member()
    {

    }
//This constructor is important for the firebase saving of data
    Member(int membershipID,String type, String phonenumber,Contribution contribution)
    {
        this.type=type;
        this.membershipID=membershipID;
        this.phonenumber= phonenumber;
        this.attendance = 0;
        this.contribution = contribution;

    }

//    This constructor is important for the Room database saving of data
    Member(int membershipID, String type, String phonenumber,String userId)
    {
        this.type=type;
        this.membershipID=membershipID;
        this.phonenumber = phonenumber;
        this.userId = userId;
    }

     public void setMembershipID(int membershipID)
     {
         this.membershipID = membershipID;
     }
     public void setEmailAddress(String phonenumber)
     {
         this.phonenumber = phonenumber;
     }
     public void setAttendance(int attendance)
     {
         this.attendance = attendance;
     }
    public String getUserId()
    {
        return this.userId;
    }
    public String getType()
    {
        return this.type;
    }
    public int getMembershipID()
    {
        return this.membershipID;
    }
    public String getPhonenumber( )
    {
        return this.phonenumber;
    }
    public void setContribution(Contribution contribution)
    {
        this.contribution = contribution;
    }
    public Contribution getContribution()
    {
        return this.contribution;
    }

    public int getAttendance()
    {
        return this.attendance;
    }

    protected Member(Parcel in) {
        membershipID = in.readInt();
        type = in.readString();
        phonenumber = in.readString();
        userId = in.readString();
        attendance = in.readInt();
        contribution = in.readParcelable(Contribution.class.getClassLoader());


    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(membershipID);
        dest.writeString(type);
        dest.writeString(phonenumber);
        dest.writeString(userId);
        dest.writeInt(attendance);
        dest.writeParcelable(contribution,flags);
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

}

