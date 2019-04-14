package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class MyDetails extends AppCompatActivity {

    private TextView membershipIdTextView, contributionTextView, attendanceTextView,phonenumberTextView;
    private ImageView profileImageView;
    private String email,userId,phonenumber;
    private Contribution contribution;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);

        profileImageView = (ImageView) findViewById(R.id.profile_image);

        contribution = new Contribution();
        membershipIdTextView = (TextView) findViewById(R.id.textView_membershipId);
        contributionTextView = (TextView) findViewById(R.id.textView_contribution);
        attendanceTextView = (TextView) findViewById(R.id.textView_attendance);

        drawAgraph();

//        Retrieve data saved on the sharedPreferences
        //        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        String image = settings.getString("profileImage"," ");
        String userNameText = settings.getString("userName"," ");
//        userNameTextView.setText(userNameText);

//        Log.d("The image string","This is the image String " + image);
////        Had to use the " " because there was a default value that had been set if the image is not found
//        if(!image.equals(" "))
//        {
//            Uri imageUri = Uri.parse(image);
//            profileImageView.setImageURI(imageUri);
//            Toast.makeText(this,"The image had been set ",Toast.LENGTH_LONG).show();
//        }
//        else
//        {
////            Will look for a better image
//            Toast.makeText(this,"No image has been set yet",Toast.LENGTH_LONG).show();
//            profileImageView.setImageResource(R.drawable.image1);
//        }

//Getting the details from the extras

        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("Phonenumber");
        userId = intent.getStringExtra("UserID");

//        emailTextView.setText(email);

        phonenumberTextView = (TextView)findViewById(R.id.welcome_textView);
        phonenumberTextView.append(phonenumber);

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
                DataSnapshot userID = dataSnapshot.child("database").child("users").child(userId);
                DataSnapshot userContribution = dataSnapshot.child("database").child("contribution").child(phonenumber);
                Boolean exist = userID.exists();
                Boolean contributionExits = userContribution.exists();
                Log.d("Confirming","This confirms that the datasnapshot user detsila exists " + exist);
                Log.d("Confirming","This confirms that the datasnapshot contribution exists " + contributionExits);

                Member member = userID.getValue(Member.class);

                contribution = userContribution.getValue(Contribution.class);

                membershipIdTextView.setText(String.valueOf(member.getMembershipID()));

                contributionTextView.setText(String.valueOf(contribution.getApril()));
                Log.d("ContributionValue","This is the contribution value " +  contribution.getApril());
                attendanceTextView.setText(String.valueOf(8));

                drawAgraph();
            }
        });
    }

    public void drawAgraph()
    {

        LineChartView lineChartView = findViewById(R.id.chart);
        String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
                "Oct", "Nov", "Dec"};

        int[] yAxisData = {contribution.getJan(),
                contribution.getFeb(),
                contribution.getMarch(),
                contribution.getApril(),
                contribution.getMayy(),
                contribution.getJune(),
                contribution.getJuly(),
                contribution.getaugust(),
                contribution.getSeptemeber(),
                contribution.getOctober(),
                contribution.getNovember(),
                contribution.getDecember()};


        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

        for(int i = 0; i < axisData.length; i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++){
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }
        List lines = new ArrayList();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        lineChartView.setLineChartData(data);

        Axis axis = new Axis();
        axis.setTextColor(Color.parseColor("#03A9F4"));
        axis.setTextSize(16);
        axis.setValues(axisValues);
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);




    }
}
