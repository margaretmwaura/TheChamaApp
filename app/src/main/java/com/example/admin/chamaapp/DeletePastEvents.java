package com.example.admin.chamaapp;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class DeletePastEvents
{
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mref;

    public void deleteAPastEvent(Event event)
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();

        //        End of arguements declaration
        Query EventQuery = mref.child("events").orderByChild("eventAgenda").equalTo(event.returnEventAgenda());
        EventQuery.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot EventSnapshot: dataSnapshot.getChildren())
                {
                    EventSnapshot.getRef().removeValue();
                    Log.d("DeletingEvents","Finally the event has been deleted ");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

