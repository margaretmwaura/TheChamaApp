package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.chamaapp.admin.MapsActivity;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity implements OnItemClickListener
{

    private RecyclerView recyclerViewCurrentEvent,recyclerViewUpcomingEvent;
    private EventsAdapter eventsAdapterCurrent,eventsAdapterUpcoming;
    private List<Event> eventListCurrent = new ArrayList<>();
    private List<Event> eventListUpcoming = new ArrayList<>();
    private FloatingActionButton addEventButton;
    private BottomSheetDialog dialog;
    private EditText eventTimeEditText, eventLocationEditText, eventAgendaEditText;
    private Event newEvent;
    private Button enterAnEvent;
    private View viewBottom,dialogView;
    private UserViewModel viewModel;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = "Events";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        recyclerViewCurrentEvent = (RecyclerView) findViewById(R.id.recycler_view_current_events);
        recyclerViewUpcomingEvent = (RecyclerView) findViewById(R.id.recycler_view_upcoming_events);

        eventsAdapterCurrent = new EventsAdapter();
        eventsAdapterCurrent.setClickListener(this);
        eventsAdapterUpcoming = new EventsAdapter();
        eventsAdapterUpcoming.setClickListener(this);

        addEventButton = (FloatingActionButton) findViewById(R.id.enter_new_event);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        Set the layoutManager of the recyclerView
        recyclerViewCurrentEvent.setLayoutManager(layoutManager1);
        recyclerViewCurrentEvent.setHasFixedSize(true);

        recyclerViewUpcomingEvent.setLayoutManager(layoutManager);
        recyclerViewUpcomingEvent.setHasFixedSize(true);



        recyclerViewCurrentEvent.setAdapter(eventsAdapterCurrent);
        recyclerViewUpcomingEvent.setAdapter(eventsAdapterUpcoming);

        addEventButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

               startUpTheBottomSheet();
            }
        });

        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
        livedata.observe(this, new Observer<DataSnapshot>()
        {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot)
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
                
                if(eventListCurrent.size() == 0)
                {
                    TextView textView = findViewById(R.id.no_activity);
                    textView.setVisibility(View.VISIBLE);
                    showdialogMessage();
                }
                eventsAdapterUpcoming.setEventList(eventListUpcoming);
            }
        });

        viewBottom = getLayoutInflater().inflate(R.layout.activity_enter_an_event, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(viewBottom);

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
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    private void startUpTheBottomSheet()
    {

        //        first remove the observers
        if (viewModel != null && viewModel.getLiveData().hasObservers())
        {
            viewModel.getLiveData().removeObservers(this);
        }
//        End of removing of the obsservers

        dialog.show();

        TextWatcher tw = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    eventTimeEditText.setText(current);
                    eventTimeEditText.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

        };
        eventAgendaEditText = (EditText) viewBottom.findViewById(R.id.Event_Agenda_event);

        eventTimeEditText = (EditText) viewBottom.findViewById(R.id.Event_Time_event);
//        This first line of code allows one to know the format of the date that one should enter
        eventTimeEditText.addTextChangedListener(tw);
//        The next line of code allows the keypad to be all numeric
        eventTimeEditText.setRawInputType(Configuration.KEYBOARD_QWERTY);
        eventLocationEditText = (EditText) viewBottom.findViewById(R.id.Event_Location_event);

//        This allows one to set the variables of the event instance


        enterAnEvent = (Button) viewBottom.findViewById(R.id.submit_new_event);
        enterAnEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                newEvent = new Event();

                newEvent.setEventAgenda(eventAgendaEditText.getText().toString());
                newEvent.setEventTime(eventTimeEditText.getText().toString());
                newEvent.setEventLocation(eventLocationEditText.getText().toString());

                Intent addTaskIntent = new Intent(EventActivity.this,MyIntentService.class);
                addTaskIntent.setAction(Backgroundactivities.addAnEventToTheDatabase);
                addTaskIntent.putExtra("TheEvent",newEvent);
                startService(addTaskIntent);



                viewModel = ViewModelProviders.of(EventActivity.this).get(UserViewModel.class);
                LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
                livedata.observe(EventActivity.this, new Observer<DataSnapshot>()
                {

                    @Override
                    public void onChanged(@Nullable DataSnapshot dataSnapshot)
                    {
                        eventListCurrent.clear();
                        eventListUpcoming.clear();
                        DataSnapshot eventsDetails = dataSnapshot.child("database").child("events");
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

                        if(eventListCurrent.size() == 0)
                        {
                            TextView textView = findViewById(R.id.no_activity);
                            textView.setVisibility(View.VISIBLE);
                            showdialogMessage();
                        }
                        eventsAdapterUpcoming.setEventList(eventListUpcoming);
                    }
                });

                dialog.dismiss();

            }
        });
    }

    @Override
    public void onClick(View view, int position)
    {
        Toast.makeText(this,"The onclick method has been called",Toast.LENGTH_LONG).show();
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//
//        if(dialogView == null)
//        {
//            //then we will inflate the custom alert dialog xml that we created
////            dialogView = LayoutInflater.from(this).inflate(R.layout.activity_maps, viewGroup, false);
//            //Now we need an AlertDialog.Builder object
//
//            LayoutInflater factory = LayoutInflater.from(this);
//            dialogView = factory.inflate(R.layout.activity_maps, null);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            //setting the view of the builder to our custom view that we already inflated
//            builder.setView(dialogView);
//
//            //finally creating the alert dialog and displaying it
//            alertDialog = builder.create();
//
//            alertDialog.show();
//        }
//        else
//        {
//            alertDialog.dismiss();
//            dialogView = null;
//            dialogView = new View(this);
////            dialogView = LayoutInflater.from(this).inflate(R.layout.activity_maps, viewGroup, false);
//            //Now we need an AlertDialog.Builder object
//
//            LayoutInflater factory = LayoutInflater.from(this);
//            dialogView = factory.inflate(R.layout.activity_maps, null);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            //setting the view of the builder to our custom view that we already inflated
//            builder.setView(dialogView);
//
//            //finally creating the alert dialog and displaying it
//            alertDialog = builder.create();
//
//            alertDialog.show();
//
//        }
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);


    }
}
