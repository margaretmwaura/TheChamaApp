package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity
{

    private RecyclerView recyclerViewCurrentEvent,recyclerViewUpcomingEvent;
    private EventsAdapter eventsAdapterCurrent,eventsAdapterUpcoming;
    private List<Event> eventListCurrent = new ArrayList<>();
    private List<Event> eventListUpcoming = new ArrayList<>();
    private Button addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        recyclerViewCurrentEvent = (RecyclerView) findViewById(R.id.recycler_view_current_events);
        recyclerViewUpcomingEvent = (RecyclerView) findViewById(R.id.recycler_view_upcoming_events);

        eventsAdapterCurrent = new EventsAdapter();
        eventsAdapterUpcoming = new EventsAdapter();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        Set the layoutManager of the recyclerView
        recyclerViewCurrentEvent.setLayoutManager(layoutManager1);
        recyclerViewCurrentEvent.setHasFixedSize(true);

        recyclerViewUpcomingEvent.setLayoutManager(layoutManager);
        recyclerViewUpcomingEvent.setHasFixedSize(true);



        recyclerViewCurrentEvent.setAdapter(eventsAdapterCurrent);
        recyclerViewUpcomingEvent.setAdapter(eventsAdapterUpcoming);


        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
        livedata.observe(this, new Observer<DataSnapshot>()
        {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot)
            {
                DataSnapshot eventsDetails = dataSnapshot.child("events");
                Boolean exist = eventsDetails.exists();
                Log.d("Confirming","This confirms that the datasnapshot exists " + exist);
                Iterable<DataSnapshot> eventsDatasnapshot = eventsDetails.getChildren();
                for(DataSnapshot eventsList : eventsDatasnapshot)
                {
                    Event event = new Event();
                    event = eventsList.getValue(Event.class);

                    String eventDate = event.returnEventTime();
//        This is the current date;
//        Use the same character of the formatter as the one on the dates either use the dashes or the strokes .. only work
//        With one of them
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

                eventsAdapterCurrent.setEventList(eventListCurrent);
                eventsAdapterUpcoming.setEventList(eventListUpcoming);
            }
        });

    }
}
