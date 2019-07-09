package com.example.admin.chamaapp.admin.Presenter;

import android.util.Log;

import com.example.admin.chamaapp.Chat;
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

     private View view;
     public TheAllChatPresenter(View view)
     {
         this.view = view;
     }

     public void readDataFromDataBase()
     {
         FirebaseDatabase mFirebaseDatabase;
         DatabaseReference mref;
         mFirebaseDatabase = FirebaseDatabase.getInstance();
         mref = mFirebaseDatabase.getReference().child("database").child("messages");
          final List<Chat> chatList = new ArrayList<>();
         mref.addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
             {

//             This allows it to be realtime adding to the screen
                 Chat chat =dataSnapshot.getValue(Chat.class);
                 chatList.add(chat);
                 if(chatList.size()!= 0)
                 {

                     view.addDataToAdapter(chatList);

                     Log.d("AddingChats", "Chats have been added to the UI");
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
        void addDataToAdapter(List<Chat> chatList);
        void addToDatabase();
    }
}
