package com.example.admin.chamaapp.Model;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.chamaapp.Presenter.OnItemClickListener;
import com.example.admin.chamaapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsAdapterViewHolder>
{

    List<Event> eventList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
     public EventsAdapter()
    {

    }
    public void setClickListener(OnItemClickListener itemClickListener)
    {
        this.onItemClickListener = itemClickListener;
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

    class EventsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private TextView eventTimeTextView, eventAgendaTextView , eventLocationTextView ;
        public EventsAdapterViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            eventTimeTextView = (TextView) itemView.findViewById(R.id.Event_Time);
            eventAgendaTextView = (TextView) itemView.findViewById(R.id.Event_Agenda);
            eventLocationTextView = (TextView) itemView.findViewById(R.id.Event_Location);
        }

        @Override
        public void onClick(View v)
        {
            onItemClickListener.onClick(v,getAdapterPosition());
        }
    }
}
