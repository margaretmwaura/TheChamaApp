package com.example.admin.chamaapp.Presenter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.admin.chamaapp.Model.Event;
import com.example.admin.chamaapp.R;
import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventActivityPresenter implements EventActivityContract.Presenter
{
    public EventActivityContract.EventAView view;

    public EventActivityPresenter(EventActivityContract.EventAView view)
    {
        this.view = view;
    }
    @Override
    public void readFromDatabase(DataSnapshot dataSnapshot, List<Event> eventListCurrent , List<Event> eventListUpcoming)
    {
        DataSnapshot eventsDetails = dataSnapshot.child("database").child("events");
        Boolean exist = eventsDetails.exists();
        Log.d("Confirming","This confirms that the datasnapshot exists " + exist);
        Iterable<DataSnapshot> eventsDatasnapshot = eventsDetails.getChildren();
        for(DataSnapshot eventsList : eventsDatasnapshot)
        {
            Event event = new Event();
            event = eventsList.getValue(Event.class);

            String eventDate = event.returnEventTime();

            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            Log.d("Todays date","This is todays date " + date);

//        Getting the difference between the event date and the current date
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
            try {
                Date date1 = null;
                date1  =   format.parse(date);
                Date date2 = null;
                date2 =  format.parse(eventDate);
                long timedifference = date2.getTime() - date1.getTime();

                if(timedifference == 0)
                {
                    eventListCurrent.add(event);
                }
                else
                {
                    eventListUpcoming.add(event);
                }

            }
            catch (Exception e)
            {
                Log.d("DateConversionError","This is the date conversion error " + e.getMessage());
            }

        }

        view.populateView();
    }
}
