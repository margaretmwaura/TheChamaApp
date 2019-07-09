package com.example.admin.chamaapp.admin.Presenter;

import android.os.Environment;
import android.util.Log;

import com.example.admin.chamaapp.Model.Admin;
import com.example.admin.chamaapp.Model.Member;
import com.google.firebase.database.DataSnapshot;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class AllDetailsPresenter
{
    private View view;
    public AllDetailsPresenter(View view)
    {
        this.view = view;
    }

    public void readDataFromFirebase(DataSnapshot dataSnapshot , List<Member> memberList ,List<Admin> adminList, List<String> userID , List<String> userIDTwo)
    {
        DataSnapshot userDetails = dataSnapshot.child("database").child("users").child("Member");
        DataSnapshot userDetailsTwo = dataSnapshot.child("database").child("users").child("Admin");
        Boolean exist = userDetails.exists();
        Log.d("Confirming","This confirms that the datasnapshot exists " + exist);
        Iterable<DataSnapshot> journals = userDetails.getChildren();
        for(DataSnapshot journal : journals)
        {
            String id;
            Member maggie = new Member();
            maggie = journal.getValue(Member.class);
            id = journal.getKey();
            memberList.add(maggie);
            userID.add(id);
        }
        Log.d("TheListRead","This are the number of journals found " + memberList.size());
        if(memberList.size() != 0)
        {
            view.displayMemberData();

        }

        Boolean existAdmin = userDetailsTwo.exists();
        Log.d("Confirming","This confirms that the datasnapshot exists " + existAdmin);
        Iterable<DataSnapshot> journalsTwo = userDetailsTwo.getChildren();
        for(DataSnapshot journal : journalsTwo)
        {
            String id;
            Admin maggie = new Admin();
            maggie = journal.getValue(Admin.class);
            id = journal.getKey();
            adminList.add(maggie);
            userIDTwo.add(id);
        }
        Log.d("TheListRead","This are the number of journals found " + memberList.size());
        if(adminList.size() != 0)
        {

            view.displayAdminData();
        }
    }
    public String generatePddf(List<Member> memberList, Boolean isStoragePermissionGranted)
    {
//        This method will be called in order to generate a pdf with all the details of the members
//        Each paragraph should take a member data

        Log.d("GeneratePDF","Currently generating a pdf ");
        String root = Environment.getExternalStorageDirectory().toString();
        Document doc = new Document();

        try
        {

            if (isStoragePermissionGranted)
            { // check or ask permission
//                This file will be generated and stored in the download folder of the android phone
                File dir = new File(root, "/Download");
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                else
                {
                    dir.delete();
                    dir.mkdirs();
                }

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


                    Paragraph p1 = new Paragraph();
                    p1.add(phonenumber);
//                    p1.add(String.valueOf(membershipID));



                    p1.setAlignment(Paragraph.ALIGN_CENTER);

                    //add paragraph to document
                    doc.add(p1);
                    Log.d("WritingToTheFile", "Writing to the file");
                }

                doc.newPage();

            }
            else
            {
                Log.d("Wrtiting to file","No permission for writing to file");
            }

            return "Success";
        }
        catch(DocumentException de)
        {
            Log.e("PDFCreator", "DocumentException:" + de);
            return "Fail";
        }
//        catch(IOException e){
//            Log.e("PDFCreator", "ioException:" + e);
//        }
        finally
        {
            doc.close();
        }


    }

    public interface View
    {
       void displayMemberData();
       void displayAdminData();
    }
}
