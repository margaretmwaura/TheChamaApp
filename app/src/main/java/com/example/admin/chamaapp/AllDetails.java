package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllDetails extends AppCompatActivity
{

//    This is the activity that displays for all the members
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private FirebaseAuth auth;
    private List<Member> memberList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_details);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
//        this is the code for creating of the layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        Set the layoutManager of the recyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        detailsAdapter = new DetailsAdapter(this);
        recyclerView.setAdapter(detailsAdapter);

        //        This displays the home button arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
        livedata.observe(this, new Observer<DataSnapshot>()
        {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot)
            {


                Boolean exist = dataSnapshot.exists();
                Log.d("Confirming","This confirms that the datasnapshot exists " + exist);
                Iterable<DataSnapshot> journals = dataSnapshot.getChildren();
                for(DataSnapshot journal : journals)
                {
                    Member maggie = new Member();
                    maggie = journal.getValue(Member.class);
                    memberList.add(maggie);
                }

                Log.d("TheListRead","This are the number of journals found " + memberList.size());
                detailsAdapter.setMembersList(memberList);
            }
        });
    }
}
