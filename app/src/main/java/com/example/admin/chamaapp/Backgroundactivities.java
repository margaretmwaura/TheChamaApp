package com.example.admin.chamaapp;

import android.app.LoaderManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import android.content.Intent;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Backgroundactivities
{
    private Context mContext;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mref;
    public FirebaseAuth mAuth;
    public static final String generatePdfString = "Generate PDF";
    public static final String addAnEventToTheDatabase = "Add event to the database";
    public static final String addANotification = "Add a notification for events";
    public static final String addAChat = "Adding a new chat to the chat list";
    public static final String addAdminChat = "Adding a new chat to the admin chat list";
    public static final String editUserData = "Edit user data";
    private static Long notificationTime;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final String LEGACY_SERVER = "AIzaSyBDCWI3JwL0obN4OuGCIRH0toradvjSmLA";



    public Backgroundactivities(Context context)
    {
        this.mContext = context;
    }
    public void generatePddf(List<Member> memberList, Boolean isStoragePermissionGranted)
    {
//        This method will be called in order to generate a pdf with all the details of the members
//        Each paragraph should take a member data

        Log.d("GeneratePDF","Currently generating a pdf ");
        String root = Environment.getExternalStorageDirectory().toString();
        Document doc = new Document();

        try
        {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

//            File dir = new File(path);
//
//            if(!dir.exists())
//                dir.mkdirs();

            if (isStoragePermissionGranted)
            { // check or ask permission
//                This file will be generated and stored in the download folder of the android phone
                File dir = new File(root, "/Download");
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
//                else
//                {
//                    myDir.delete();
//                    myDir.mkdirs();
//                }

                File file = new File(dir, "newFile.pdf");
                try {
                    FileOutputStream fOut = new FileOutputStream(file);
                    PdfWriter.getInstance(doc, fOut);
                }
                catch (Exception e)
                {
                    Log.d("FileOutputStreamError","A file outputstream has not been created; an error has been encountered");
                }

                //open the document
                doc.open();
//                doc.add(new Chunk(" "));
                doc.newPage();
                for (int i = 0; i < memberList.size(); i++)
                {
                    Member maggie = memberList.get(i);
                    String phonenumber = maggie.getPhonenumber();
                    int membershipID = maggie.getMembershipID();
                    int attendance = maggie.getAttendance();
                    int contribution = maggie.getContribution();


                    Paragraph p1 = new Paragraph();
                    p1.add(phonenumber);
                    p1.add(String.valueOf(membershipID));
                    p1.add(String.valueOf(attendance));
                    p1.add(String.valueOf(contribution));


                    p1.setAlignment(Paragraph.ALIGN_CENTER);

                    //add paragraph to document
                    doc.add(p1);
                    Log.d("WritingToTheFile", "Writing to the file");
                }
            }
            else
            {
                Log.d("Wrtiting to file","No permission for writing to file");
            }
        }
        catch(DocumentException de)
        {
            Log.e("PDFCreator", "DocumentException:" + de);
        }
//        catch(IOException e){
//            Log.e("PDFCreator", "ioException:" + e);
//        }
        finally
        {
            doc.close();
        }


    }
    public static Long returnNotificationEventTime()
    {
        return notificationTime;
    }
    public void addEventToDatabase(final Event event)
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();



        mref.child("database").child("events").push().setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
//                Event has been added
                Log.d("Firebase","Event has been succesfully added");

//                Now that an event has been added call code to get a message from firebase
//                when the message has been gotten an event will be scheduled using an alarm manager
                Intent addTaskIntent = new Intent(mContext,MyIntentService.class);
                addTaskIntent.setAction(Backgroundactivities.addANotification);
                mContext.startService(addTaskIntent);


//                Now that the event has been successfully added now start a job scheduler to delete the event
//                When that particular day comes
                scheduledeleteEvent(event);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("Firebase","No data added");
            }
        });


        sendANotification();
    }

//    Will build this method
    public void sendANotification()
    {
        Log.d("NotificationSending","The method has been called to send a notification");
//        String regToken = returnRegistrationToken();
        String regToken = " ";
        Log.d("RegistrationToken","This is the registration token " + regToken);
        OkHttpClient client = new OkHttpClient();
        JSONObject json=new JSONObject();
        JSONObject dataJson=new JSONObject();
        try {
            dataJson.put("body", "Reminder about the chama event \n taking place today");
            dataJson.put("title", "Chama event");
            json.put("notification", dataJson);
            json.put("to", "/topics/Group");
        }
        catch (Exception e)
        {
            Log.d("NotificationSettingUp", "An error was caught : details " + e.getMessage());
        }
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .header("Authorization","key="+LEGACY_SERVER)
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String finalResponse = response.body().string();
        }
        catch (Exception e)
        {
            Log.d("NotificationExecution","An error was caught : details " + e.getMessage());
            e.printStackTrace();
        }
//          String finalResponse = response.body().string();

        Log.d("NotificationSending","The method has completed");
    }

    private void scheduledeleteEvent(Event event)
    {
//        This is converting the string to GSON so that it can be sent across classes
        Gson g = new Gson();
        String json = g.toJson(event);

        Log.d("JobScheduler ","The job scheduler is being prepped ");
//        The event now which is now a string is set as an extra in the bundle
        PersistableBundle extraEvent = new PersistableBundle();
        extraEvent.putString(deleteEventJobScheduler.EVENT,json);

//Need to get the date entered in the event for it will subtract the current date
//        Then add 24 hours for the event will be deleted at the end of that day

        String eventDate = event.returnEventTime();
//        This is the current date;
//        Use the same character of the formatter as the one on the dates either use the dashes or the strokes .. only work
//        With one of them
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Log.d("Todays date","This is todays date " + date);

//        Getting the difference between the event date and the current date
         SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
         long timeMilliseconds;
         try {
             Date date1 = null;
                   date1  =   format.parse(date);
             Date date2 = null;
                   date2 =  format.parse(eventDate);


                   notificationTime = ( date2.getTime() - date1.getTime());

             Log.d("NotificationEventTime","This is the notification event time in the background activity class " + notificationTime);

             timeMilliseconds =( date2.getTime() - date1.getTime()) + 86400000;
             Log.d("DifferenceBetweenDates","This is the difference between the two dates " + timeMilliseconds);
//             long period = timeMilliseconds + 86
         }
         catch (Exception e)
         {
             Log.d("DateConversionError","This is the date conversion error " + e.getMessage());
         }

//         To get the event deleted at the end of the day will be the difference in time plus 24hours

        ComponentName componentName = new ComponentName(mContext,deleteEventJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1,componentName)
                .setExtras(extraEvent)
                .setPeriodic(60000)
//             Will add a criteria for time
        .build();

        JobScheduler jobScheduler = (JobScheduler)mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    public void addAChatToTheChatList(Chat chat)
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mref.child("database").child("messages").push().setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
//                Event has been added
                Log.d("Firebase","Chat has been succesfully added");



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("Firebase","No chat added");
            }
        });
    }

    public void addAChatToTheAmdinChatList(Chat chat)
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mref.child("database").child("adminMessages").push().setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
//                Event has been added
                Log.d("Firebase","Chat has been succesfully added");



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d("Firebase","No chat added");
            }
        });
    }

    public void editUserData(String userID, final String  phonenumber, int attendance , final int contributionValue)
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();

        Calendar calendar = Calendar.getInstance();
        String[] monthName = {"january","february","march","april","may","june","july","august","september","october","november","december"};
        String month = monthName[calendar.get(Calendar.MONTH)];
        mref.child("database").child("contribution").child(phonenumber).child(month).setValue(contributionValue).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                Log.d("ContributionAdding","Contribution value has been added to the database ");
            }
        });
        mref.child("database").child("users").child(userID).child("attendance").setValue(attendance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                Log.d("ContributionAdding","Contribution value has been added to the database ");
            }
        });
    }


}