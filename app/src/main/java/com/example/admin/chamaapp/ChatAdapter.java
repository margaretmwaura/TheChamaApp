package com.example.admin.chamaapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{

    List<Chat> chatList = new ArrayList<>();
    Context mContext;

    public ChatAdapter(Context context)
    {
        this.mContext = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
//        this is for getting the id of the layout with the data textView
        int id = R.layout.chat_list_item;
//        This is for inflating of the layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(id,parent,shouldAttachToParentImmediately);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position)
    {
      Chat currentChat = chatList.get(position);
      holder.userEmailAddress.setText(currentChat.getUserEmailAddress());
      holder.userMessage.setText(currentChat.getUserMessage());
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

    class ChatViewHolder extends RecyclerView.ViewHolder
    {
       TextView userEmailAddress , userMessage;
        public ChatViewHolder(View itemView)
        {
            super(itemView);
            userEmailAddress = itemView.findViewById(R.id.users_email_address);
            userMessage = itemView.findViewById(R.id.users_text_message);

        }
    }
}
