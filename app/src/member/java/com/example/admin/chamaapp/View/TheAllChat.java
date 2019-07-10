package com.example.admin.chamaapp.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.chamaapp.Model.Chat;
import com.example.admin.chamaapp.Model.ChatAdapter;
import com.example.admin.chamaapp.Presenter.Backgroundactivities;
import com.example.admin.chamaapp.Presenter.MyIntentService;
import com.example.admin.chamaapp.Presenter.TheAllChatPresenter;
import com.example.admin.chamaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TheAllChat extends AppCompatActivity implements TheAllChatPresenter.View {

    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private Button sendButton;
    private EditText chatEditText;
    private String phonenumber;
    private ChildEventListener childEventListener;
    public FirebaseAuth mAuth;
    public TheAllChatPresenter theAllChatPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_all_chat);

        theAllChatPresenter = new TheAllChatPresenter(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = "General";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

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

        layoutManager.setStackFromEnd(true);
//        Set the layoutManager of the recyclerView
        chatRecyclerView.setLayoutManager(layoutManager);

        chatRecyclerView.setAdapter(chatAdapter);
        theAllChatPresenter.readChats();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String chatMessage = chatEditText.getText().toString();
                chatEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                Chat chat = new Chat();
                chat.setUserEmailAddress(phonenumber);
                chat.setUserMessage(chatMessage);

                Intent addTaskIntent = new Intent(TheAllChat.this, MyIntentService.class);
                addTaskIntent.setAction(Backgroundactivities.addAChat);
                addTaskIntent.putExtra("ANewChat",chat);
                startService(addTaskIntent);

                chatEditText.setText(" ");

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void setUpAdapter(List<Chat> chatList)
    {
        chatAdapter.setChatList(chatList);
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        Log.d("AddingChats", "Chats have been added to the UI");
    }
}
