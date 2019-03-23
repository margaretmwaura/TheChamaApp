package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

public class MyDetails extends AppCompatActivity {

    private TextView emailTextView, userNameTextView, membershipIdTextView, contributionTextView, attendanceTextView;
    private ImageView profileImageView;
    private String email,userId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);

        profileImageView = (ImageView) findViewById(R.id.profile_image);
        emailTextView = (TextView) findViewById(R.id.textView_email);
        userNameTextView = (TextView) findViewById(R.id.textview_username);
        membershipIdTextView = (TextView) findViewById(R.id.textView_membershipId);
        contributionTextView = (TextView) findViewById(R.id.textView_contribution);
        attendanceTextView = (TextView) findViewById(R.id.textView_attendance);

//        Retrieve data saved on the sharedPreferences
        //        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        String image = settings.getString("profileImage"," ");
        String userNameText = settings.getString("userName"," ");
        userNameTextView.setText(userNameText);

        Log.d("The image string","This is the image String " + image);
//        Had to use the " " because there was a default value that had been set if the image is not found
        if(!image.equals(" "))
        {
            Uri imageUri = Uri.parse(image);
            profileImageView.setImageURI(imageUri);
            Toast.makeText(this,"The image had been set ",Toast.LENGTH_LONG).show();
        }
        else
        {
//            Will look for a better image
            Toast.makeText(this,"No image has been set yet",Toast.LENGTH_LONG).show();
            profileImageView.setImageResource(R.drawable.image1);
        }

//Getting the details from the extras

        Intent intent = getIntent();
        email = intent.getStringExtra("UserEmail");
        userId = intent.getStringExtra("UserID");

        emailTextView.setText(email);

        getUserDetailFromDatabase();
    }

    public void getUserDetailFromDatabase()
    {
        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        LiveData<DataSnapshot> livedata = viewModel.getDataSnapshotLiveData();
        livedata.observe(this, new Observer<DataSnapshot>()
        {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot)
            {
                DataSnapshot userID = dataSnapshot.child("users").child(userId);
                Boolean exist = userID.exists();
                Log.d("Confirming","This confirms that the datasnapshot exists " + exist);
//                for(DataSnapshot snapshot : userID.getChildren())
//                {
                    Member member = userID.getValue(Member.class);
                    membershipIdTextView.setText(String.valueOf(member.getMembershipID()));
//                }
            }
        });
    }
}
