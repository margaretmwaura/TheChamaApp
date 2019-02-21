package com.example.admin.chamaapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event implements Parcelable
{
    public String eventTime;
    public String eventLocation, eventAgenda;

    public Event()
    {

    }

    public void setEventTime(String eventTime)
    {
        this.eventTime = eventTime;
    }
    public void setEventLocation(String eventLocation)
    {
        this.eventLocation = eventLocation;
    }
    public void setEventAgenda(String eventAgenda)
    {
        this.eventAgenda = eventAgenda;
    }
    public String returnEventTime()
    {
        return eventTime;
    }
    public String returnEventLocation()
    {
        return eventLocation;
    }
    public String returnEventAgenda()
    {
        return eventAgenda;
    }


//    All this is pertaining the parcelable implementation

    protected Event(Parcel in) {
        eventTime = in.readString();
        eventLocation = in.readString();
        eventAgenda = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventTime);
        dest.writeString(eventLocation);
        dest.writeString(eventAgenda);
    }
}
