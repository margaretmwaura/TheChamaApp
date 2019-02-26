package com.example.admin.chamaapp;

import android.content.Intent;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EnterAnEvent extends AppCompatActivity {


    private EditText eventTimeEditText, eventLocationEditText, eventAgendaEditText;
    private Event newEvent;
    private Button enterAnEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_an_event);

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
        eventAgendaEditText = (EditText) findViewById(R.id.Event_Agenda_event);

        eventTimeEditText = (EditText) findViewById(R.id.Event_Time_event);
//        This first line of code allows one to know the format of the date that one should enter
        eventTimeEditText.addTextChangedListener(tw);
//        The next line of code allows the keypad to be all numeric
        eventTimeEditText.setRawInputType(Configuration.KEYBOARD_QWERTY);
        eventLocationEditText = (EditText) findViewById(R.id.Event_Location_event);

//        This allows one to set the variables of the event instance


        enterAnEvent = (Button) findViewById(R.id.submit_new_event);
        enterAnEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                newEvent = new Event();

//                Date date1;
//                try
//                {
//                     date1= (Date) new SimpleDateFormat("dd/MM/yyyy").parse(eventTimeEditText.getText().toString());
//                }
//                catch (ParseException e)
//                {
//                    e.printStackTrace();
//                }
                newEvent.setEventAgenda(eventAgendaEditText.getText().toString());
                newEvent.setEventTime(eventTimeEditText.getText().toString());
                newEvent.setEventLocation(eventLocationEditText.getText().toString());

                Intent addTaskIntent = new Intent(EnterAnEvent.this,MyIntentService.class);
                addTaskIntent.setAction(Backgroundactivities.addAnEventToTheDatabase);
                addTaskIntent.putExtra("TheEvent",newEvent);
                startService(addTaskIntent);

            }
        });
    }
}
