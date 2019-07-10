package com.example.admin.chamaapp.Presenter;

import com.example.admin.chamaapp.Model.Event;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public interface EventActivityContract
{
     interface Presenter
    {
        void readFromDatabase(DataSnapshot dataSnapshot, List<Event> eventListCurrent , List<Event> eventListUpcoming);
    }
    interface EventAView
    {
        void populateView();
    }
}
