package com.example.admin.chamaapp.admin.View;


import android.Manifest;
import android.annotation.SuppressLint;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;

import android.os.Bundle;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.admin.chamaapp.Model.Admin;
import com.example.admin.chamaapp.Model.AdminDetailAdapter;
import com.example.admin.chamaapp.Presenter.Backgroundactivities;
import com.example.admin.chamaapp.BuildConfig;
import com.example.admin.chamaapp.Model.DetailsAdapter;
import com.example.admin.chamaapp.Model.Member;
import com.example.admin.chamaapp.Presenter.MyIntentService;
import com.example.admin.chamaapp.Presenter.OnItemClickListenerWithType;
import com.example.admin.chamaapp.R;
import com.example.admin.chamaapp.Model.UserViewModel;
import com.example.admin.chamaapp.admin.Presenter.AllDetailsPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllDetails extends AppCompatActivity implements OnItemClickListenerWithType, LoaderManager.LoaderCallbacks<String>, AllDetailsPresenter.View
{


    //    This is the activity that displays for all the members
    private RecyclerView recyclerView,recyclerViewAdmin;
    private DetailsAdapter detailsAdapter;
    private AdminDetailAdapter adminDetailAdapter;
    private FirebaseAuth auth;
    private List<Member> memberList = new ArrayList<>();
    private List<Admin> adminList =new ArrayList<>();
    private List<String> userID = new ArrayList<>();
    private List<String> userIDTwo = new ArrayList<>();
    private boolean storagePermission;
    public static final int OPERATION_SEARCH_LOADER = 22;
    private Member member;
    private String userIDSelected;
    private Admin admin;
    private int position;
    private UserViewModel viewModel;
    final String[] selectedMonth = new String[1];
    private TextView adminSize, memberSize;
    private AllDetailsPresenter allDetailsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_details);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         allDetailsPresenter = new AllDetailsPresenter(this);
        String title = "Details";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

         adminSize = (TextView) findViewById(R.id.admin_size);
         memberSize = (TextView) findViewById(R.id.member_size);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerViewAdmin = (RecyclerView) findViewById(R.id.recycler_admin);
//        this is the code for creating of the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        LinearLayoutManager layoutManagerTwo = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

//        Set the layoutManager of the recyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdmin.setLayoutManager(layoutManagerTwo);
        recyclerViewAdmin.setHasFixedSize(true);

        detailsAdapter = new DetailsAdapter(this);
        adminDetailAdapter = new AdminDetailAdapter(this);

        recyclerView.setAdapter(detailsAdapter);
        recyclerViewAdmin.setAdapter(adminDetailAdapter);

        detailsAdapter.setClickListener(this);
        adminDetailAdapter.setClickListener(this);

        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
        livedata.observe(this, new Observer<DataSnapshot>()
        {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot)
            {

            allDetailsPresenter.readDataFromFirebase(dataSnapshot,memberList,adminList,userID,userIDTwo);


            }
        }
        );

     isStoragePermissionGranted();

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
    public void onClick(View view, int position,String type)
    {

            if(type.equals("member")) {
                position = position;
                Log.d("OnClick", "This method has been called ");
                member = memberList.get(position);
                userIDSelected = userID.get(position);
                showCustomDialog("Member", member.getPhonenumber(), userIDSelected);
            }

            if(type.equals("Admin")) {
                position = position;
                Log.d("OnClick", "This method has been called ");
                admin = adminList.get(position);
                userIDSelected = userIDTwo.get(position);
                showCustomDialog("Admin", admin.getPhonenumber(), userIDSelected);
            }

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
               String result =  allDetailsPresenter.generatePddf(memberList1,permission);
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

    private void makeOperationSearchQuery()
    {

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



    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    private void showCustomDialog(final String type, final String phonenumber, final String userClicked)
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

//        The month array
        String [] monthsOfTheYear = {
                "january","february","march","april","may","june","july","august","september","october","november","december"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, monthsOfTheYear);
        final AutoCompleteTextView monthEdittext = dialogView.findViewById(R.id.month_dialog);
        monthEdittext.setThreshold(1);
        monthEdittext.setAdapter(adapter);

        monthEdittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            selectedMonth[0] = String.valueOf(parent.getItemAtPosition(position));
            Log.d("MonthSelected","This is the selected month " + selectedMonth[0]);

            }
        });

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
//
                String input = contribution.getText().toString();

                if(input.isEmpty() || input.equals( " ") || selectedMonth[0] == null)
                {

                    if(input.isEmpty() || input.equals( " ") )
                    {
                        contribution.setError("Enter contribution");
                    }
                    if(selectedMonth[0] == null)
                    {
                        monthEdittext.setError(" Choose a month");
                    }
//                    alertDialog.dismiss();
                }
                else
                {
                    int contributionData = Integer.parseInt(contribution.getText().toString());
                    Intent addTaskIntent = new Intent(AllDetails.this, MyIntentService.class);
                    addTaskIntent.setAction(Backgroundactivities.editUserData);
                    addTaskIntent.putExtra("UserPhoneNumber",phonenumber);
                    addTaskIntent.putExtra("UserID",userClicked);
                    addTaskIntent.putExtra("Type",type);
                    addTaskIntent.putExtra("Month", selectedMonth[0]);
                    addTaskIntent.putExtra("ContributionData",contributionData);
                    startService(addTaskIntent);


                    viewModel = ViewModelProviders.of(AllDetails.this).get(UserViewModel.class);
                    LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
                    livedata.observe(AllDetails.this, new Observer<DataSnapshot>()
                            {
                                @Override
                                public void onChanged(@Nullable DataSnapshot dataSnapshot)
                                {
                                    memberList.clear();
                                    adminList.clear();

                                    allDetailsPresenter.readDataFromFirebase(dataSnapshot,memberList,adminList,userID,userIDTwo);

                                }
                            }
                    );

                    alertDialog.dismiss();
                }


            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("OnResume","OnResume method has been called ");
    }


    @Override
    public void displayMemberData()
    {
        detailsAdapter.setMembersList(memberList);
        memberSize.setText(String.valueOf(memberList.size()));
    }

    @Override
    public void displayAdminData()
    {
        adminDetailAdapter.setAdminList(adminList);
        adminSize.setText(String.valueOf(adminList.size()));
    }
}
