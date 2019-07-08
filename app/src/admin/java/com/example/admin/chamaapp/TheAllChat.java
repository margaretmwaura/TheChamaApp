package com.example.admin.chamaapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.admin.chamaapp.Analytics.addGeneralChatAnalysis;

public class TheAllChat extends AppCompatActivity {

    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private Button sendButton;
    private EditText chatEditText;
    private String phonenumber;
    private ChildEventListener childEventListener;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mref;
    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_all_chat);

//        This code has been added to enable the layout below the keyboard to be moved up above the keyboard
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = "General";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("database").child("messages");

        mAuth = FirebaseAuth.getInstance();

//        Getting the emailAddress from the intent
        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("Phonenumber");

        Log.d("EmailAddress","This is the users email address " + phonenumber);
        sendButton = findViewById(R.id.sendChat);
        chatEditText = findViewById(R.id.message_edittext);

        chatAdapter = new ChatAdapter(this);
        chatRecyclerView = findViewById(R.id.chat_recyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//        Set the layoutManager of the recyclerView
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
//        chatRecyclerView.setHasFixedSize(true);

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
         public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
         {

//             This allows it to be realtime adding to the screen
             Chat chat =dataSnapshot.getValue(Chat.class);
             chatList.add(chat);
             if(chatList.size()!= 0)
             {
                 chatAdapter.setChatList(chatList);
                 chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
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



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String chatMessage = chatEditText.getText().toString();
                chatEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                //                This returns the current time
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String stringTime = sdf.format(new Date());

                Chat chat = new Chat();
                chat.setUserEmailAddress(phonenumber);
                chat.setUserMessage(chatMessage);
                chat.setChatTime(stringTime);

//                Logging the event to firebase analytics
                Bundle params = new Bundle();
                params.putString("Action","AddingGeneralChat");
                addGeneralChatAnalysis(TheAllChat.this,"GeneralChat",params);

                Intent addTaskIntent = new Intent(TheAllChat.this,MyIntentService.class);
                addTaskIntent.setAction(Backgroundactivities.addAChat);
                addTaskIntent.putExtra("ANewChat",chat);
                startService(addTaskIntent);

                chatEditText.setText("Message");

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
