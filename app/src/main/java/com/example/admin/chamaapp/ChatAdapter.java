package com.example.admin.chamaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter
{

    List<Chat> chatList = new ArrayList<>();
    Context mContext;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    public ChatAdapter(Context context)
    {
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position)
    {
        Chat currentChat = chatList.get(position);
        if(currentChat.getUserEmailAddress() != null && !currentChat.getUserEmailAddress().isEmpty())
        {
            if (currentChat.getUserEmailAddress().equals("+254711309532"))
            {
                return VIEW_TYPE_MESSAGE_SENT;
            }
            else
                {
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }

        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        if(viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            Context context = parent.getContext();
//        this is for getting the id of the layout with the data textView
            int id = R.layout.receivedchats;
//        This is for inflating of the layout
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = layoutInflater.inflate(id, parent, shouldAttachToParentImmediately);
            return new ChatViewHolderMessageReceived(view);
        }
        if(viewType == VIEW_TYPE_MESSAGE_SENT)
        {
            Context context = parent.getContext();
//        this is for getting the id of the layout with the data textView
            int id = R.layout.sentchats;
//        This is for inflating of the layout
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = layoutInflater.inflate(id, parent, shouldAttachToParentImmediately);
            return new ChatViewHolderMessageSent(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat currentChat = chatList.get(position);
        int holderType = holder.getItemViewType();

        if(holderType == VIEW_TYPE_MESSAGE_RECEIVED)
        {
            ((ChatViewHolderMessageReceived)holder).bind(currentChat);
        }
        if(holderType == VIEW_TYPE_MESSAGE_SENT)
        {
            ((ChatViewHolderMessageSent)holder).bind(currentChat);
        }
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void setChatList(List<Chat> chats)
    {
        chatList = chats;
        notifyDataSetChanged();
    }

    class ChatViewHolderMessageSent extends RecyclerView.ViewHolder
    {
       TextView userMessage;

        public ChatViewHolderMessageSent(View itemView)
        {
            super(itemView);

            userMessage = itemView.findViewById(R.id.text_message_body);
        }

        void bind(Chat chat)
        {
            userMessage.setText(chat.getUserMessage());
        }
    }

    class ChatViewHolderMessageReceived extends RecyclerView.ViewHolder
    {
        TextView userMessage, useremail;
        public ChatViewHolderMessageReceived(View itemView)
        {
            super(itemView);
            userMessage = itemView.findViewById(R.id.text_message_body);
            useremail = itemView.findViewById(R.id.text_message_name);
        }
        void bind(Chat chat)
        {
            userMessage.setText(chat.getUserMessage());
            useremail.setText(chat.getUserEmailAddress());
        }
    }
}
