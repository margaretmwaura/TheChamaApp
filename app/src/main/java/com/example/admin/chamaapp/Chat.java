package com.example.admin.chamaapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Chat implements Parcelable
{
    public String userEmailAddress;
    public String userMessage;

    public Chat()
    {

    }

    protected Chat(Parcel in)
    {
        userEmailAddress = in.readString();
        userMessage = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    public void setUserEmailAddress(String emailAddress)
    {
        this.userEmailAddress = emailAddress;
    }
    public void setUserMessage(String message)
    {
        this.userMessage = message;
    }
    public String getUserEmailAddress()
    {
        return userEmailAddress;
    }
    public String getUserMessage()
    {
        return userMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userEmailAddress);
        dest.writeString(userMessage);
    }
}
