package com.example.admin.chamaapp.admin.View;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.admin.chamaapp.R;
import com.example.admin.chamaapp.admin.Model.ChatMessage;
import com.example.admin.chamaapp.admin.Model.chat_rec;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Chat_bot extends AppCompatActivity implements AIListener {

    RecyclerView recyclerView;
    EditText editText;
    RelativeLayout addBtn;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<ChatMessage, chat_rec> adapter;
    Boolean flagFab = true;
    private AIService aiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        editText = (EditText) findViewById(R.id.editText);

        addBtn = (RelativeLayout) findViewById(R.id.addBtn);

        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(
                true);

        recyclerView.setLayoutManager(linearLayoutManager);

        ref = FirebaseDatabase.getInstance().getReference();

        ref.keepSynced(true);


        final AIConfiguration config = new AIConfiguration("427285970ae44fdaae8abb949896acc1", AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.
                        System);
        aiService = AIService.getService(this,config);
        aiService.setListener(this);
        final AIDataService aiDataService = new AIDataService(this, config);
        final AIRequest aiRequest = new AIRequest();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override

            public void onClick(View view) {
                String message =
                        editText.getText().toString().trim();

                if (!message.equals("")) {
                    ChatMessage chatMessage =
                            new ChatMessage(message, "user");

                    ref.child("database").child("bot").push().setValue(chatMessage);
                    new AsyncTask<AIRequest, Void, AIResponse>() {
                        @SuppressLint("WrongThread")
                        @Override
                        protected AIResponse doInBackground(AIRequest... aiRequests) {

                            final AIRequest request = aiRequests[0];

                            try {

                                final AIResponse response = aiDataService.request(request);

                                return response;
                            } catch (AIServiceException e)
                            {
                                Log.d("AddingChat","Error getting a chat ");
                                Log.e("Exception ","Exception " + e.getMessage());
                            }

                            return null;
                        }

                        @Override

                        protected void onPostExecute(AIResponse response) {

                            if (response != null) {
                                Result result = response.getResult();
                                String reply = result.getFulfillment().getSpeech();
                                ChatMessage chatMessage =
                                        new ChatMessage(reply, "bot");

                                ref.child("database").child("bot").push().setValue(chatMessage);
                            }
                        }
                    }.execute(aiRequest);
                }
                else {

                    aiService.startListening();
                }


                editText.setText("");
            }
        });

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("database").child("bot");

        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                        .setQuery(query, new SnapshotParser<ChatMessage>() {
                            @NonNull
                            @Override
                            public ChatMessage parseSnapshot(@NonNull final DataSnapshot snapshot) {
                                return new ChatMessage(
                                        snapshot.child("msgText").getValue().toString(),
                                        snapshot.child("msgUser").getValue().toString());

                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, chat_rec>(options) {

            @NonNull
            @Override
            public chat_rec onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.msglist, viewGroup, false);

                return new chat_rec(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull chat_rec holder, int position, @NonNull ChatMessage model) {
                if (model.getMsgUser().equals("user")) {
                    holder.
                            rightText.setText(model.getMsgText());
                    holder.
                            rightText.setVisibility(View.VISIBLE);
                    holder.
                            leftText.setVisibility(View.GONE);
                } else {
                    holder.
                            leftText.setText(model.getMsgText());
                    holder.
                            rightText.setVisibility(View.GONE);
                    holder.
                            leftText.setVisibility(View.VISIBLE);
                }
            }

        };

        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onResult(AIResponse result) {

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}

