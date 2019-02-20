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

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    private List<Event> eventList = new ArrayList<>();
    private Button addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        eventsAdapter = new EventsAdapter();

        addEventButton = (Button) findViewById(R.id.enter_new_event);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        Set the layoutManager of the recyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        eventsAdapter = new EventsAdapter();

        recyclerView.setAdapter(eventsAdapter);

        addEventButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(EventActivity.this,EnterAnEvent.class);
                startActivity(intent);
            }
        });

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
                    eventList.add(event);
                }

                eventsAdapter.setEventList(eventList);
            }
        });

    }
}
