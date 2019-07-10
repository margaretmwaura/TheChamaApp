package com.example.admin.chamaapp.Presenter;

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

public class AdminOnlyPresenterMember implements AdminOnlyContractMember.AdminOnlyPresenterMember
{
    public AdminOnlyContractMember.AdminOnlyViewMember view;

    public AdminOnlyPresenterMember(AdminOnlyContractMember.AdminOnlyViewMember  view)
    {
        this.view = view;
    }
    @Override
    public void readFromDatabase()
    {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mref;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("database").child("adminMessages");
        final List<Chat> chatList = new ArrayList<>();
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
//             This allows it to be realtime adding to the screen
                Chat chat =dataSnapshot.getValue(Chat.class);
                chatList.add(chat);

                if(chatList.size() != 0)
                {

                    view.populateAdapter(chatList);

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
}
