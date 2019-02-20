package com.example.admin.chamaapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsAdapterViewHolder>
{

    List<Event> eventList = new ArrayList<>();
     public EventsAdapter()
    {

    }

    @Override
    public EventsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
//        this is for getting the id of the layout with the data textView
        int id = R.layout.event_list_items;
//        This is for inflating of the layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(id,parent,shouldAttachToParentImmediately);
        return new EventsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsAdapterViewHolder holder, int position)
    {
       Event current = eventList.get(position);
       holder.eventLocationTextView.setText(current.returnEventLocation());
       holder.eventAgendaTextView.setText(current.returnEventAgenda());
       holder.eventTimeTextView.setText(String.valueOf(current.returnEventTime()));
    }

    @Override
    public int getItemCount()
    {
        return eventList.size();
    }

    public void setEventList(List<Event> eventList)
    {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    class EventsAdapterViewHolder extends RecyclerView.ViewHolder
    {

        private TextView eventTimeTextView, eventAgendaTextView , eventLocationTextView ;
        public EventsAdapterViewHolder(View itemView)
        {
            super(itemView);
            eventTimeTextView = (TextView) itemView.findViewById(R.id.Event_Time);
            eventAgendaTextView = (TextView) itemView.findViewById(R.id.Event_Agenda);
            eventLocationTextView = (TextView) itemView.findViewById(R.id.Event_Location);
        }
    }
}