package com.example.admin.chamaapp;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter
{

    List<Chat> chatList = new ArrayList<>();
    Context mContext;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private String[] userDetails = new String[2];
    private String phonenumber ;


    public ChatAdapter(Context context)
    {
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position)
    {
        Chat currentChat = chatList.get(position);
        userDetails = getUserDetails();
        phonenumber = userDetails[1];


        if(currentChat.getUserEmailAddress() != null && !currentChat.getUserEmailAddress().isEmpty())
        {
            if (currentChat.getUserEmailAddress().equals(phonenumber))
            {

                Log.d("SentMessages","Sent messages retrieved");
                return VIEW_TYPE_MESSAGE_SENT;
            }
            else
                {

                    Log.d("ReceivedMessages","Received messages received");
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

    public String[] getUserDetails ()
    {
        //        This is reading the email from the file

        String[] details = new String[2];
        String detail;
        File directory = mContext.getFilesDir();
        File file = new File(directory,"UserDetails" );

//        String yourFilePath = this.getFilesDir() + "/" + ".txt";
//        File yourFile = new File( yourFilePath );

        try {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(in);

            details = new String[2];
            details = (String[]) s.readObject();

            for(int i = 0; i<details.length;i++)
            {
                detail = details[i];
                Log.d("UserDetails","This are the user details " + detail);
            }
            return details;
        }
        catch (Exception e)
        {
            Log.d("ErrorFileReading","Error encountered while reading the file " + e.getMessage());

            return details;

        }

    }

    class ChatViewHolderMessageSent extends RecyclerView.ViewHolder
    {
       TextView userMessage;
       TextView textMessageTime;

        public ChatViewHolderMessageSent(View itemView)
        {
            super(itemView);

            userMessage = itemView.findViewById(R.id.text_message_body);
            textMessageTime = itemView.findViewById(R.id.text_message_time);
        }

        void bind(Chat chat)
        {
            userMessage.setText(chat.getUserMessage());
            textMessageTime.setText(chat.getChatTime());
        }
    }

    class ChatViewHolderMessageReceived extends RecyclerView.ViewHolder
    {
        TextView userMessage, useremail , textMessageTime;
        public ChatViewHolderMessageReceived(View itemView)
        {
            super(itemView);
            userMessage = itemView.findViewById(R.id.text_message_body);
            useremail = itemView.findViewById(R.id.text_message_name);
            textMessageTime = itemView.findViewById(R.id.text_message_time);
        }
        void bind(Chat chat)
        {
            userMessage.setText(chat.getUserMessage());
            useremail.setText(chat.getUserEmailAddress());
            textMessageTime.setText(chat.getChatTime());
        }
    }



}
