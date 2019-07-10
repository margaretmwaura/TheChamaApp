package com.example.admin.chamaapp.Presenter;

import android.util.Log;
import android.view.View;

import com.example.admin.chamaapp.Model.Chat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TheAllChatPresenter
{
    public View view;

    public TheAllChatPresenter(View view)
    {
        this.view = view;
    }

    public void readChats()
    {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mref;
        final List<Chat> chatList = new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("database").child("messages");

        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
//             This allows it to be realtime adding to the screen
                Chat chat =dataSnapshot.getValue(Chat.class);
                chatList.add(chat);
                if(chatList.size()!= 0)
                {

                    view.setUpAdapter(chatList);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public interface View
    {
        void setUpAdapter(List<Chat> chatList);
    }
}
