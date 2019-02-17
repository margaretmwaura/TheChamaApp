package com.example.admin.chamaapp;


import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AMemberDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Member>
{

    String email,userID;
    private Button editButton;
    int contribution, attendance, membershipID;
    private TextView emailTextView, contributionTextView, attendanceTextView, membershipIDTextView;
    public static final int OPERATION_SEARCH_LOADER = 22;
    public static final String OPERATION_QUERY_URL_EXTRA = "query";
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mref;
    public FirebaseAuth mAuth;
    private Member maggie;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amember_details);

        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        maggie = intent.getParcelableExtra("Member");
        userID = intent.getStringExtra("UserID");



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();

        emailTextView = (TextView) findViewById(R.id.email_text_view);
        contributionTextView = (TextView) findViewById(R.id.contribution);
        attendanceTextView = (TextView) findViewById(R.id.attendance_textView);
        membershipIDTextView = (TextView)findViewById(R.id.membershipID_textview);



         bindDataToUI(maggie);

//        This is the code for the button when it is  clicked it should trigger editing of the members data in firebase
        editButton = (Button) findViewById(R.id.edit_member_data_button);
        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               startingTheDialog();
            }
        });
    }
    public void bindDataToUI(Member maggie)
    {
        email = maggie.getEmailAddress();
        contribution = maggie.getContribution();
        attendance = maggie.getAttendance();
        membershipID = maggie.getMembershipID();

        emailTextView.setText(email);
        contributionTextView.setText(String.valueOf(contribution));
        attendanceTextView.setText(String.valueOf(attendance));
        membershipIDTextView.setText(String.valueOf(membershipID));

        Log.d("BindDataToUi","Data has been binded to the UI");

    }

    public void editMemberData(String userID)
    {
        // Create a bundle called queryBundle
        Bundle queryBundle = new Bundle();
        // Use putString with OPERATION_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        queryBundle.putString(OPERATION_QUERY_URL_EXTRA,userID);
        queryBundle.putInt("NewContribution",contribution);
        // Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = getLoaderManager();
        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> loader = loaderManager.getLoader(OPERATION_SEARCH_LOADER);
        // If the Loader was null, initialize it. Else, restart it.
        if(loader==null)
        {
            loaderManager.initLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
        }
        else
            {
            loaderManager.restartLoader(OPERATION_SEARCH_LOADER, queryBundle, this);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Member> onCreateLoader(int id, final Bundle args)
    {
        return new AsyncTaskLoader<Member>(this)
        {
            @Override
            public Member loadInBackground()
            {
                String id = args.getString(OPERATION_QUERY_URL_EXTRA);
                int contribution = args.getInt("NewContribution");
                mref.child("users").child(id).child("attendance").setValue(contribution);

                 maggie.setAttendance(contribution);
                 return maggie;
            }

            @Override
            protected void onStartLoading()
            {
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Member> loader, Member data)
    {

         bindDataToUI(data);
    }

    @Override
    public void onLoaderReset(Loader<Member> loader) {

    }

    public void startingTheDialog()
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(AMemberDetails.this);

        alert.setTitle("Edit contribution");
        alert.setMessage("Enter Members contribution");

// Set an EditText view to get user input
        final EditText input = new EditText(AMemberDetails.this);
        alert.setView(input);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                contribution = Integer.parseInt(input.getText().toString()) ;
                // Do something with value!
                editMemberData(userID);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}
