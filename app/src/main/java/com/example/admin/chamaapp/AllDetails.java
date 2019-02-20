package com.example.admin.chamaapp;


import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllDetails extends AppCompatActivity implements OnItemClickListener
{

//    This is the activity that displays for all the members
    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;
    private FirebaseAuth auth;
    private List<Member> memberList = new ArrayList<>();
    private List<String> userID = new ArrayList<>();
    private Button generatePdf,viewPDFButton;
    private boolean storagePermission;

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
        generatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTaskIntent = new Intent(AllDetails.this,MyIntentService.class);
                addTaskIntent.putParcelableArrayListExtra("Members", (ArrayList<? extends Parcelable>) memberList);
                addTaskIntent.putExtra("StoragePermission",storagePermission);
                                    startService(addTaskIntent);
                Log.d("Intent", "Intent Service started");
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
}
