package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class MyDetails extends AppCompatActivity {

    private TextView phonenumberTextView, userNameTextView, membershipIdTextView, contributionTextView, attendanceTextView;
    private String phonenumber , userId;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mref;
    private Contribution contribution ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

          contribution = new Contribution();
        membershipIdTextView = (TextView) findViewById(R.id.textView_membershipId);
        contributionTextView = (TextView) findViewById(R.id.textView_contribution);
        attendanceTextView = (TextView) findViewById(R.id.textView_attendance);


//        Retrieve data saved on the sharedPreferences
        //        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        String image = settings.getString("profileImage"," ");
        String userNameText = settings.getString("userName"," ");


        Log.d("The image string","This is the image String " + image);

//Getting the details from the extras

        drawAgraph();

        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("Phonenumber");
        userId = intent.getStringExtra("UserID");

       phonenumberTextView = (TextView)findViewById(R.id.welcome_textView);
       phonenumberTextView.append(phonenumber);
        getUserDetailFromDatabase();
//        final List<Integer> contributionRead = new ArrayList<>();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mref = mFirebaseDatabase.getReference();
//        mref.child("users").child(userId).child("contribution").addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                Boolean exists = dataSnapshot.exists();
//                Log.d("Datasnapshot","Checking whether the datasnapshot exists " + exists);
//                Iterable<DataSnapshot> journals = dataSnapshot.getChildren();
//                for(DataSnapshot journal : journals)
//                {
//                    int contribution;
//                    contribution = journal.getValue(Integer.class);
//                    contributionRead.add(contribution);
//                }
//                Log.d("ContributionArray","This is the size of the array " +  contributionRead.size());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



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
                    Admin member = userID.getValue(Admin.class);

                    contribution = userContribution.getValue(Contribution.class);

                    membershipIdTextView.setText(String.valueOf(member.getID()));

                    contributionTextView.setText(String.valueOf(contribution.getApril()));
                    Log.d("ContributionValue","This is the contribution value " +  contribution.getApril());
                    attendanceTextView.setText(String.valueOf(String.valueOf(0)));

                drawAgraph();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        finish();
        return true;
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
