package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TheAllChat extends AppCompatActivity {

    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private Button sendButton;
    private EditText chatEditText;
    private String emailAddress;
    private ChildEventListener childEventListener;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mref;
    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_all_chat);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("messages");
        mAuth = FirebaseAuth.getInstance();

//        Getting the emailAddress from the intent
        Intent intent = getIntent();
        emailAddress = intent.getStringExtra("UserEmail");

        Log.d("EmailAddress","This is the users email address " + emailAddress);
        sendButton = findViewById(R.id.sendChat);
        chatEditText = findViewById(R.id.message_edittext);

        chatAdapter = new ChatAdapter(this);
        chatRecyclerView = findViewById(R.id.chat_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        Set the layoutManager of the recyclerView
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setHasFixedSize(true);

        chatRecyclerView.setAdapter(chatAdapter);

//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);


//        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
//        LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
//        livedata.observe(this, new Observer<DataSnapshot>()
//                {
//                    @Override
//                    public void onChanged(@Nullable DataSnapshot dataSnapshot)
//                    {
//
//                        DataSnapshot userDetails = dataSnapshot.child("messages");
//                        Boolean exist = userDetails.exists();
//                        Log.d("Confirming","This confirms that the datasnapshot exists " + exist);
//                        Iterable<DataSnapshot> journals = userDetails.getChildren();
//                        for(DataSnapshot journal : journals)
//                        {
//                            Chat chat = new Chat();
//                            chat = journal.getValue(Chat.class);
//                            chatList.add(chat);
//                        }
//
//                        Log.d("TheChatList","This are the number of chats found " + chatList.size());
//                        chatAdapter.setChatList(chatList);
//                    }
//                }
//        );

     mref.addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
             Chat chat =dataSnapshot.getValue(Chat.class);
             chatList.add(chat);
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
     chatAdapter.setChatList(chatList);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String chatMessage = chatEditText.getText().toString();

                Chat chat = new Chat();
                chat.setUserEmailAddress(emailAddress);
                chat.setUserMessage(chatMessage);

                Intent addTaskIntent = new Intent(TheAllChat.this,MyIntentService.class);
                addTaskIntent.setAction(Backgroundactivities.addAChat);
                addTaskIntent.putExtra("ANewChat",chat);
                startService(addTaskIntent);

            }
        });
    }
}
