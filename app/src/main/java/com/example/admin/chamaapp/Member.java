package com.example.admin.chamaapp;


/**
 * Created by TOSHIBA on 5/4/2018.
 */
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@Entity(tableName = "Member")
@IgnoreExtraProperties
public class Member implements Parcelable
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

     public void setMembershipID(int membershipID)
     {
         this.membershipID = membershipID;
     }
     public void setEmailAddress(String emailAddress)
     {
         this.emailAddress = emailAddress;
     }
     public void setAttendance(int attendance)
     {
         this.attendance = attendance;
     }
     public void setContribution(int contribution)
     {
         this.contribution = contribution;
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
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    public int getAttendance()
    {
        return this.attendance;
    }
    public int getContribution()
    {
        return this.contribution;
    }

    protected Member(Parcel in) {
        membershipID = in.readInt();
        type = in.readString();
        emailAddress = in.readString();
        userId = in.readString();
        attendance = in.readInt();
        contribution = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(membershipID);
        dest.writeString(type);
        dest.writeString(emailAddress);
        dest.writeString(userId);
        dest.writeInt(attendance);
        dest.writeInt(contribution);
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

