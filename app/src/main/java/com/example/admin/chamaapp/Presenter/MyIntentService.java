package com.example.admin.chamaapp.Presenter;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.admin.chamaapp.Model.Chat;
import com.example.admin.chamaapp.Model.Event;
import com.example.admin.chamaapp.Model.Member;
import com.example.admin.chamaapp.Presenter.Backgroundactivities;

import java.util.ArrayList;
import java.util.List;


public class MyIntentService extends IntentService {



    public MyIntentService()
    {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d("OnHandleIntent","The method has been called");

        Backgroundactivities backgroundactivities = new Backgroundactivities(this);


         String action = intent.getAction();
         if(Backgroundactivities.generatePdfString.equals(action))
         {
             List<Member> members = new ArrayList<>();
             members = intent.getParcelableArrayListExtra("Members");
             Log.d("SizeOfArray","This is the size of the array " + members.size());
             boolean storagePermission = intent.getBooleanExtra("StoragePermission",false);
             backgroundactivities.generatePddf(members,storagePermission);
         }
         if(Backgroundactivities.addAnEventToTheDatabase.equals(action))
         {
            Event event = intent.getParcelableExtra("TheEvent");
            backgroundactivities.addEventToDatabase(event);
         }
        if(Backgroundactivities.addANotification.equals(action))
        {
            backgroundactivities.sendANotification();
        }
        if(Backgroundactivities.addAChat.equals(action))
        {
            Chat chat = intent.getParcelableExtra("ANewChat");
            backgroundactivities.addAChatToTheChatList(chat);
        }
        if(Backgroundactivities.addAdminChat.equals(action))
        {
            Chat chat = intent.getParcelableExtra("ANewChat");
            backgroundactivities.addAChatToTheAmdinChatList(chat);
        }
           if(Backgroundactivities.editUserData.equals(action))
             {
               String userId = intent.getStringExtra("UserID");
               String phonenumber = intent.getStringExtra("UserPhoneNumber");
               String month  = intent.getStringExtra("Month");
               String type = intent.getStringExtra("Type");
               int contribution = intent.getIntExtra("ContributionData",0);

                      backgroundactivities.editUserData(userId,phonenumber,month,contribution,type);
                   }
    }


}
