package com.example.admin.chamaapp.View;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.chamaapp.Model.Event;
import com.example.admin.chamaapp.Model.EventsAdapter;
import com.example.admin.chamaapp.Model.UserViewModel;
import com.example.admin.chamaapp.Presenter.EventActivityContract;
import com.example.admin.chamaapp.Presenter.EventActivityPresenter;
import com.example.admin.chamaapp.R;
import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventActivity extends AppCompatActivity implements EventActivityContract.EventAView
{

    private RecyclerView recyclerViewCurrentEvent,recyclerViewUpcomingEvent;
    private EventsAdapter eventsAdapterCurrent,eventsAdapterUpcoming;
    private List<Event> eventListCurrent = new ArrayList<>();
    private List<Event> eventListUpcoming = new ArrayList<>();
    private Button addEventButton;
    private EventActivityPresenter eventActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventActivityPresenter = new EventActivityPresenter(this);
        String title = "Events";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        recyclerViewCurrentEvent = (RecyclerView) findViewById(R.id.recycler_view_current_events);
        recyclerViewUpcomingEvent = (RecyclerView) findViewById(R.id.recycler_view_upcoming_events);

        eventsAdapterCurrent = new EventsAdapter();
        eventsAdapterUpcoming = new EventsAdapter();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

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
              eventActivityPresenter.readFromDatabase(dataSnapshot,eventListCurrent,eventListUpcoming);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp()
    {

        finish();
        return true;
    }

    private void showdialogMessage()
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("No activity Today")
                .setMessage("There is no activity that is happening today");

        final AlertDialog alert = dialog.create();
        alert.show();

// Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 5000);
    }

    @Override
    public void populateView() {
        eventsAdapterCurrent.setEventList(eventListCurrent);

        if(eventListCurrent.size() == 0)
        {
            TextView textView = findViewById(R.id.no_activity);
            textView.setVisibility(View.VISIBLE);
            showdialogMessage();
        }
        eventsAdapterUpcoming.setEventList(eventListUpcoming);
    }
}
