package com.example.admin.chamaapp.Presenter;

import android.content.Context;
import android.os.Environment;

import android.util.Log;

import com.example.admin.chamaapp.Model.Contribution;
import com.example.admin.chamaapp.Model.Member;
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
    public void generatePddf(List<Member> memberList, Boolean isStoragePermissionGranted)
    {

        Log.d("GeneratePDF","Currently generating a pdf ");
        String root = Environment.getExternalStorageDirectory().toString();
        Document doc = new Document();

        try {


            if (isStoragePermissionGranted)
            { // check or ask permission
                File dir = new File(root, "/saved_images");
                if (!dir.exists())
                {
                    dir.mkdirs();
                }


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
                    String emailAddress = maggie.getPhonenumber();
                    int membershipID = maggie.getMembershipID();
                    int attendance = maggie.getAttendance();
                    Contribution contribution = maggie.getContribution();

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
