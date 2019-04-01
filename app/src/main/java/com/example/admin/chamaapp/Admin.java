package com.example.admin.chamaapp;


/**
 * Created by TOSHIBA on 5/4/2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Admin implements Parcelable {
    public String type;
    public String position;
    public int AdminId;
    public String phonenumber;
    public int contribution;

    Admin()
    {

    }

    Admin(String type,String position,int Id,String phonenumber)
    {
        this.type=type;
        this.position=position;
        this.AdminId = Id;
        this.phonenumber = phonenumber;
        this.contribution = 0;
    }

    protected Admin(Parcel in) {
        type = in.readString();
        position = in.readString();
        AdminId = in.readInt();
        phonenumber = in.readString();
        contribution = in.readInt();
    }

    public static final Creator<Admin> CREATOR = new Creator<Admin>() {
        @Override
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };

    public String getType()
    {
        return this.type;
    }
    public int getID( )
    {
        return this.AdminId;
    }
    public String getPosition(){return  this.position;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(position);
        dest.writeInt(AdminId);
        dest.writeString(phonenumber);
        dest.writeInt(contribution);
    }
}

