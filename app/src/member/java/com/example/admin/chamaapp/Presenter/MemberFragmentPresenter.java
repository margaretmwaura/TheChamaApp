package com.example.admin.chamaapp.Presenter;

import android.util.Log;

import com.example.admin.chamaapp.Model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

public class MemberFragmentPresenter
{
    public View view;

    public MemberFragmentPresenter(View view)
    {
        this.view = view;
    }

    public void addMemberToFirebase(Member maggie , String userId)
    {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mref;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();
        mref.child("database").child("users").child("Member").child(userId).setValue(maggie).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                view.startNavigationDrawer();
            }

        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.d("Error" , "An error was encountered while adding user");
            }
        });
    }
    public interface View
    {
        void startNavigationDrawer();
    }
}
