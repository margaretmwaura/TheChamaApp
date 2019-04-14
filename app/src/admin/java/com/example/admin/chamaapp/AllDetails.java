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
import android.graphics.Color;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class AllDetails extends AppCompatActivity implements OnItemClickListener, LoaderManager.LoaderCallbacks<String>
{

//    This is the activity that displays for all the members
    private RecyclerView recyclerView, recyclerViewContribution;
    private DetailsAdapter detailsAdapter;
    private FirebaseAuth auth;
    private List<Member> memberList = new ArrayList<>();
    private List<String> userID = new ArrayList<>();
      private boolean storagePermission;
    public static final int OPERATION_SEARCH_LOADER = 22;
    private Member member;
    private String userIDSelected;
    private int position;
    private UserViewModel viewModel;
    private ContributionAdapter contributionAdapter;
    private List<Contribution> contributionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String title = "Member details";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
//        this is the code for creating of the layout manager


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

//        Set the layoutManager of the recyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        detailsAdapter = new DetailsAdapter(this);

        recyclerView.setAdapter(detailsAdapter);
        detailsAdapter.setClickListener(this);

        recyclerViewContribution = (RecyclerView) findViewById(R.id.recycler_contribution);
        LinearLayoutManager layoutManagerOne = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewContribution.setLayoutManager(layoutManagerOne);
        recyclerView.setHasFixedSize(true);
        contributionAdapter = new ContributionAdapter(this);
        recyclerViewContribution.setAdapter(contributionAdapter);



        //        This displays the home button arrow
        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
        livedata.observe(this, new Observer<DataSnapshot>()
        {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot)
            {

                DataSnapshot userDetails = dataSnapshot.child("database").child("users");
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
                    detailsAdapter.setMembersList(memberList);
                }

                DataSnapshot userDetailContribution = dataSnapshot.child("database").child("contribution");
                Boolean existContribution = userDetails.exists();
                Log.d("Confirming","This confirms that the datasnapshot exists " + existContribution);
                Iterable<DataSnapshot> journalsContribution = userDetailContribution.getChildren();
                for(DataSnapshot journal : journalsContribution)
                {
                    String id;
                    Contribution contribution = new Contribution();
                    contribution = journal.getValue(Contribution.class);
                    contributionList.add(contribution);

                }

                Log.d("TheListRead","This are the number of contributions found " + contributionList.size());
                if(contributionList.size() != 0)
                {
                    contributionAdapter.setContributionList(contributionList);
                }
            }
        }
        );

     isStoragePermissionGranted();
//        generatePdf.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
////                Intent addTaskIntent = new Intent(AllDetails.this,MyIntentService.class);
////                addTaskIntent.putParcelableArrayListExtra("Members", (ArrayList<? extends Parcelable>) memberList);
////                addTaskIntent.putExtra("StoragePermission",storagePermission);
////                addTaskIntent.setAction(Backgroundactivities.generatePdfString);
////                                    startService(addTaskIntent);
////
////                Log.d("Intent", "Intent Service started");
//
//
//            }
//        });
//        viewPDFButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.createPDFMenu == item.getItemId())
        {
            makeOperationSearchQuery();
            return true;
        }
        if(R.id.viewPDFMenu == item.getItemId())
        {
            viewPdf("newFile.pdf","saved_images");
            return true;
        }

        return false;
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
        position = position;
        Log.d("OnClick","This method has been called ");
        member = memberList.get(position);
        userIDSelected = userID.get(position);
        showCustomDialog();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        memberList.clear();

        Log.d("OnPauseMethod","OnPause method has been called ");
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
                    String phonenumber = maggie.getPhonenumber();
                    int membershipID = maggie.getMembershipID();
                    int attendance = maggie.getAttendance();

                    Paragraph p1 = new Paragraph();
                    p1.add(phonenumber);
                    p1.add(String.valueOf(membershipID));
                    p1.add(String.valueOf(attendance));


                    p1.setAlignment(Paragraph.ALIGN_CENTER);

                    //add paragraph to document
                    doc.add(p1);
                    Log.d("WritingToTheFile", "Writing to the file");
                }

                doc.newPage();
                for (int i = 0; i < contributionList.size(); i++)
                {
                    Contribution contribution = contributionList.get(i);
                    String phonenumber = contribution.getPhonenumber();
                    int jan = contribution.getJan();

                    Paragraph p1 = new Paragraph();
                    p1.add(phonenumber);
                    p1.add(String.valueOf(phonenumber));
                    p1.add(String.valueOf(jan));


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

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    private void showCustomDialog()
    {

//        first remove the observers
        if (viewModel != null && viewModel.getLiveData().hasObservers())
        {
            viewModel.getLiveData().removeObservers(this);
        }
//        End of removing of the obsservers


        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialogview, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText attendance = dialogView.findViewById(R.id.attendance_dialog);
        final EditText contribution = dialogView.findViewById(R.id.contribution_dialog);
        Button editButton = dialogView.findViewById(R.id.editbutton_dialog);
        Button cancelButton = dialogView.findViewById(R.id.cancelbutton_dialog);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int attendanceData = Integer.parseInt(attendance.getText().toString());
                int contributionData = Integer.parseInt(contribution.getText().toString());
                Intent addTaskIntent = new Intent(AllDetails.this,MyIntentService.class);
                addTaskIntent.setAction(Backgroundactivities.editUserData);
                addTaskIntent.putExtra("UserPhoneNumber",member.getPhonenumber());
                addTaskIntent.putExtra("UserID",userIDSelected);
                addTaskIntent.putExtra("Attendance", attendanceData);
                addTaskIntent.putExtra("ContributionData",contributionData);
                startService(addTaskIntent);

//                new java.util.Timer().schedule(
//                        new java.util.TimerTask() {
//                            @Override
//                            public void run()
//                            {
//                                // your code here
//
//                            }
//                        },
//                        5000
//                );
                viewModel = ViewModelProviders.of(AllDetails.this).get(UserViewModel.class);
                LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
                livedata.observe(AllDetails.this, new Observer<DataSnapshot>()
                        {
                            @Override
                            public void onChanged(@Nullable DataSnapshot dataSnapshot)
                            {
                                memberList.clear();
                                DataSnapshot userDetails = dataSnapshot.child("database").child("users");
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
                                    detailsAdapter.setMembersList(memberList);
                                }

                                contributionList.clear();
                                DataSnapshot userDetailContribution = dataSnapshot.child("database").child("contribution");
                                Boolean existContribution = userDetails.exists();
                                Log.d("Confirming","This confirms that the datasnapshot exists " + existContribution);
                                Iterable<DataSnapshot> journalsContribution = userDetailContribution.getChildren();
                                for(DataSnapshot journal : journalsContribution)
                                {
                                    String id;
                                    Contribution contribution = new Contribution();
                                    contribution = journal.getValue(Contribution.class);
                                    contributionList.add(contribution);

                                }

                                Log.d("TheListRead","This are the number of contributions found " + contributionList.size());
                                if(contributionList.size() != 0)
                                {
                                    contributionAdapter.setContributionList(contributionList);
                                }
                            }
                        }
                );
               alertDialog.dismiss();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("OnResume","OnResume method has been called ");
    }

}
