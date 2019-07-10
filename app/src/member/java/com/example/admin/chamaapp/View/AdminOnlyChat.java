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
import android.widget.Toast;

import com.example.admin.chamaapp.Model.Chat;
import com.example.admin.chamaapp.Model.ChatAdapter;
import com.example.admin.chamaapp.Presenter.AdminOnlyContractMember;
import com.example.admin.chamaapp.Presenter.AdminOnlyPresenterMember;
import com.example.admin.chamaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminOnlyChat extends AppCompatActivity implements AdminOnlyContractMember.AdminOnlyViewMember
{

    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private Button sendButton;
    private EditText chatEditText;
    private String emailAddress;
    private ChildEventListener childEventListener;
    private AdminOnlyPresenterMember adminOnlyPresenterMember;

    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_only_chat);

        adminOnlyPresenterMember = new AdminOnlyPresenterMember(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = "Announcement";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);


        mAuth = FirebaseAuth.getInstance();

//        Getting the emailAddress from the intent
        Intent intent = getIntent();
        emailAddress = intent.getStringExtra("Phonenumber");

        Log.d("EmailAddress","This is the users email address " + emailAddress);
        sendButton = findViewById(R.id.sendChat);
        chatEditText = findViewById(R.id.message_edittext);

        chatAdapter = new ChatAdapter(this);
        chatRecyclerView = findViewById(R.id.chat_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
//        Set the layoutManager of the recyclerView
        chatRecyclerView.setLayoutManager(layoutManager);

        chatRecyclerView.setAdapter(chatAdapter);

        adminOnlyPresenterMember.readFromDatabase();




        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String chatMessage = chatEditText.getText().toString();

                chatEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                Toast.makeText(AdminOnlyChat.this,"You cannot add a chat \n you are not admin",Toast.LENGTH_LONG).show();
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
    public void populateAdapter(List<Chat> chatList)
    {
        chatAdapter.setChatList(chatList);
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        Log.d("AddingChats","Chats have been added to the UI");
    }
}

