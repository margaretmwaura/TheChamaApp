package com.example.admin.chamaapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Backgroundactivities
{
    private Context mContext;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mref;
    public FirebaseAuth mAuth;
    public static final String generatePdfString = "Generate PDF";
    public static final String addAnEventToTheDatabase = "Add event to the database";
    private String eventTime;
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

        try {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

//            File dir = new File(path);
//
//            if(!dir.exists())
//                dir.mkdirs();

            if (isStoragePermissionGranted)
            { // check or ask permission
                File dir = new File(root, "/saved_images");
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
                FileOutputStream fOut = new FileOutputStream(file);
                PdfWriter.getInstance(doc, fOut);


                //open the document
                doc.open();
//                doc.add(new Chunk(" "));
                doc.newPage();
                for (int i = 0; i < memberList.size(); i++)
                {
                    Member maggie = memberList.get(i);
                    String emailAddress = maggie.getEmailAddress();
                    int membershipID = maggie.getMembershipID();
                    int attendance = maggie.getAttendance();
                    int contribution = maggie.getContribution();

                    Paragraph p1 = new Paragraph();
                    p1.add(emailAddress);
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
        } catch(DocumentException de){
            Log.e("PDFCreator", "DocumentException:" + de);
        }
        catch(IOException e){
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally
        {
            doc.close();

        }


    }
    public void addEventToDatabase(final Event event)
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

//        This is the time against which data shall be checked against inorder to delete it
        eventTime = event.returnEventTime();

        mref.child("events").push().setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
//                Event has been added
                Log.d("Firebase","Event has been succesfully added");

//                Now that the event has been successfully added now starta job scheduler to delete the event
//                When that particular day comes
                scheduledeleteEvent(event);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Firebase","No data added");
            }
        });
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


        ComponentName componentName = new ComponentName(mContext,deleteEventJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1,componentName)
                .setExtras(extraEvent)
                .setPeriodic(60000)
                

//                Will add a criteria for time
        .build();
        JobScheduler jobScheduler = (JobScheduler)mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }


}