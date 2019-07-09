package com.example.admin.chamaapp.admin.View;

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

import com.example.admin.chamaapp.Backgroundactivities;
import com.example.admin.chamaapp.Chat;
import com.example.admin.chamaapp.ChatAdapter;
import com.example.admin.chamaapp.MyIntentService;
import com.example.admin.chamaapp.R;
import com.example.admin.chamaapp.admin.Presenter.TheAllChatPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.admin.chamaapp.Analytics.addGeneralChatAnalysis;

public class TheAllChat extends AppCompatActivity implements TheAllChatPresenter.View {

    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private Button sendButton;
    private EditText chatEditText;
    private String phonenumber,stringTime,chatMessage;
    private ChildEventListener childEventListener;
    public FirebaseAuth mAuth;
    public TheAllChatPresenter theAllChatPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_all_chat);

//        This code has been added to enable the layout below the keyboard to be moved up above the keyboard
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
//        Set the layoutManager of the recyclerView
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
//        chatRecyclerView.setHasFixedSize(true);

        chatRecyclerView.setAdapter(chatAdapter);
        theAllChatPresenter.readDataFromDataBase();



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                chatMessage = chatEditText.getText().toString();
                chatEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                //                This returns the current time
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                stringTime = sdf.format(new Date());

                addToDatabase();

                chatEditText.setText("Message");

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void addDataToAdapter(List<Chat> chatList)
    {
        chatAdapter.setChatList(chatList);
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    public void addToDatabase()
    {

       Chat chat = new Chat();
        chat.setUserEmailAddress(phonenumber);
        chat.setUserMessage(chatMessage);
        chat.setChatTime(stringTime);

//                Logging the event to firebase analytics
        Bundle params = new Bundle();
        params.putString("Action","AddingGeneralChat");
        addGeneralChatAnalysis(TheAllChat.this,"GeneralChat",params);

        Intent addTaskIntent = new Intent(TheAllChat.this, MyIntentService.class);
        addTaskIntent.setAction(Backgroundactivities.addAChat);
        addTaskIntent.putExtra("ANewChat",chat);
        startService(addTaskIntent);

    }
}
