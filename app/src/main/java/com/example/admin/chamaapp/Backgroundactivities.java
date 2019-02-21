package com.example.admin.chamaapp;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mref;
    public static FirebaseAuth mAuth;
    public static final String generatePdfString = "Generate PDF";
    public static final String addAnEventToTheDatabase = "Add event to the database";
    Backgroundactivities(Context context)
    {
        this.mContext = context;
    }
    public static void generatePddf(List<Member> memberList, Boolean isStoragePermissionGranted)
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
    public static void addEventToDatabase(Event event)
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        mref.child("events").push().setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                Log.d("Firebase","Data has been succesfully added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Firebase","No data added");
            }
        });
    }


}
