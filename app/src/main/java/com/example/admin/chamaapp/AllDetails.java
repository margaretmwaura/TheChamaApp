package com.example.admin.chamaapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.admin.chamaapp.AMemberDetails.OPERATION_QUERY_URL_EXTRA;

public class AllDetails extends AppCompatActivity implements OnItemClickListener, LoaderManager.LoaderCallbacks<String>
{

//    This is the activity that displays for all the members
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private FirebaseAuth auth;
    private List<Member> memberList = new ArrayList<>();
    private List<String> userID = new ArrayList<>();
    private Button generatePdf,viewPDFButton;
    private boolean storagePermission;
    public static final int OPERATION_SEARCH_LOADER = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_details);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
//        this is the code for creating of the layout manager

        generatePdf = (Button) findViewById(R.id.generate_pdf);
         viewPDFButton = (Button) findViewById(R.id.view_pdf);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        Set the layoutManager of the recyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        detailsAdapter = new DetailsAdapter(this);

        recyclerView.setAdapter(detailsAdapter);
        detailsAdapter.setClickListener(this);

        //        This displays the home button arrow
        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);


        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
        livedata.observe(this, new Observer<DataSnapshot>()
        {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot)
            {

                DataSnapshot userDetails = dataSnapshot.child("users");
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
                detailsAdapter.setMembersList(memberList);
            }
        }
        );

     isStoragePermissionGranted();
        generatePdf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
//                Intent addTaskIntent = new Intent(AllDetails.this,MyIntentService.class);
//                addTaskIntent.putParcelableArrayListExtra("Members", (ArrayList<? extends Parcelable>) memberList);
//                addTaskIntent.putExtra("StoragePermission",storagePermission);
//                addTaskIntent.setAction(Backgroundactivities.generatePdfString);
//                                    startService(addTaskIntent);
//
//                Log.d("Intent", "Intent Service started");

                makeOperationSearchQuery();
            }
        });
        viewPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                viewPdf("newFile.pdf","saved_images");
            }
        });
    }
    public boolean isStoragePermissionGranted()
    {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            {
                Log.v(TAG, "Permission is granted");
                storagePermission = true;
                return true;
            } else
            {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(AllDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                storagePermission = false;
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            storagePermission = true;
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            Log.v("Permissions granting","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission

            storagePermission = true;
        }
    }

    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = FileProvider.getUriForFile(AllDetails.this, BuildConfig.APPLICATION_ID + ".provider",pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        try
        {
            Log.d("OpeningTheFile","The File is opening ");
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(AllDetails.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view, int position)
    {
        Log.d("OnClick","This method has been called ");
        Member member = memberList.get(position);
        String userId  = userID.get(position);
        Log.d("UserID","This is the users id " + userId);
        Intent intent = new Intent(this,AMemberDetails.class);
        intent.putExtra("Member",member);
        intent.putExtra("UserID",userId);
        startActivity(intent);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        memberList.clear();
    }

//    This methods are for ensuring that creating the pdf is done outside the main Thread
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args)
    {
        return new AsyncTaskLoader<String>(this)
        {
            @Override
            public String loadInBackground()
            {
                Log.d("LoadInBackground","The load in background method has been called");
                List<Member> memberList1 = new ArrayList<>();
                memberList1 = args.getParcelableArrayList("MemberList");
                boolean permission = args.getBoolean("StoragePermission");
               String result =  generatePddf(memberList1,permission);
               return result;
            }

            @Override
            protected void onStartLoading()
            {
//                This is being called so that the loadInBackground method can be called
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data)
    {
         if(data == "Success")
         {
             Log.d("PdfGeneration","The pdf has been generated ");

             AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setTitle("PDF generation")
                     .setMessage("The pdf has been generated and has been stored in the Downloads folder")
                     .setCancelable(false)
                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id)
                         {
                             //do things
                              dialog.cancel();
                         }
                     });
             AlertDialog alert = builder.create();
             alert.show();
         }
         else
         {
             Log.d("PdfGeneration","The pdf generation was a fail");
         }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void makeOperationSearchQuery() {

        // Create a bundle called queryBundle
        Bundle queryBundle = new Bundle();
        // Call getSupportLoaderManager and store it in a LoaderManager variable
        queryBundle.putBoolean("StoragePermission",storagePermission);
        queryBundle.putParcelableArrayList("MemberList", (ArrayList<? extends Parcelable>) memberList);
        LoaderManager loaderManager = getSupportLoaderManager();
        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> loader = loaderManager.getLoader(OPERATION_SEARCH_LOADER);
        // If the Loader was null, initialize it. Else, restart it.
        if(loader==null)
        {
            loaderManager.initLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
            Log.d("InitializerLoader","Loader has been initialized ");
        }else{
            loaderManager.restartLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
            Log.d("RestartLoader","Loader has been restarted");
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
}
