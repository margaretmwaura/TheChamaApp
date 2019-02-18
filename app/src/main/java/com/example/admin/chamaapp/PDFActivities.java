package com.example.admin.chamaapp;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFActivities
{
    private Context mContext;
    PDFActivities(Context context)
    {
        this.mContext = context;
            }
    public void generatePddf(List<Member> memberList,Boolean isStoragePermissionGranted)
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


}
