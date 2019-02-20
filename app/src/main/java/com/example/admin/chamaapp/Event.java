package com.example.admin.chamaapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event
{
    private long eventTime;
    private String eventLocation, eventAgenda;

    public Event()
    {

    }

    public void setEventTime(long eventTime)
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
    public long returnEventTime()
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
}
