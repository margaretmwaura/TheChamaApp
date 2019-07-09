package com.example.admin.chamaapp.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class FirebaseLiveData extends LiveData<DataSnapshot>
{
    private final Query query;
    private final MyValueEventListener listener = new MyValueEventListener();

    public FirebaseLiveData(Query query) {
        this.query = query;
    }

    public FirebaseLiveData(DatabaseReference ref) {
        this.query = ref;
    }

    @Override
    protected void onActive() {
        Log.d("FireBasseLiveData", "onActive");
        query.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d("FireBaseLiveData", "onInactive");
        query.removeEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener
    {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
        {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError)
        {
            Log.d("FireBaseDatabase","No listening donne");
        }
    }
}
