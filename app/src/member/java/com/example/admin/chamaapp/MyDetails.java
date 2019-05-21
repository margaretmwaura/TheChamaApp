package com.example.admin.chamaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MyDetails extends AppCompatActivity {

    private TextView phonenumberTextView , monthsContribued, totalContributed;
    private TextView dateTextView;
    private String image,userId,phonenumber;
    private Contribution contribution;
    private Button sendEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);



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
        image = intent.getStringExtra("ImageSet");

//        emailTextView.setText(email);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.profile_image, null);

        CircleImageView circleImageView = (CircleImageView)v.findViewById(R.id.imageView5);
        if(!image.equals(" "))
        {
            circleImageView.setImageURI(Uri.parse(image));
        }
        else
        {
            circleImageView.setImageResource(R.drawable.face);
        }
        getSupportActionBar().setCustomView(v);

        contribution = new Contribution();


        dateTextView = (TextView) findViewById(R.id.date_textView);
        dateTextView.setText(getDateInWords());
//        Retrieve data saved on the sharedPreferences
        //        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        String userNameText = settings.getString("userName"," ");


        Log.d("The image string","This is the image String " + image);

//Getting the details from the extras
        getUserDetailFromDatabase();
        drawAgraph();

        totalContributed = (TextView)findViewById(R.id.total_contribution);
        totalContributed.setText(String.valueOf(totalContribution()));
        monthsContribued = (TextView) findViewById(R.id.months_contributed);
        monthsContribued.setText(String.valueOf(months()));

        sendEmail = (Button) findViewById(R.id.send_email);
        phonenumberTextView = (TextView)findViewById(R.id.welcome_textView);
        phonenumberTextView.append(phonenumber);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "mwauramargaret1@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                } catch(Exception e) {
                    Toast.makeText(MyDetails.this, "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
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
                DataSnapshot userID = dataSnapshot.child("database").child("users").child("Member").child(userId);
                Boolean exist = userID.exists();

                Log.d("Confirming","This confirms that the datasnapshot user detsila exists " + exist);
                Member member = userID.getValue(Member.class);
                contribution = member.getContribution();

//                    membershipIdTextView.setText(String.valueOf(member.getID()));

//                    contributionTextView.setText(String.valueOf(totalContribution()));
                Log.d("ContributionValue","This is the contribution value " +  contribution.getApril());
//                    attendanceTextView.setText(String.valueOf(String.valueOf(0)));




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

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#00EEEE"));

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
        axis.setTextColor(Color.parseColor("#FF4081"));
        axis.setTextSize(16);
        axis.setValues(axisValues);
        axis.setName("Contributions in Ksh");
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#3F51B5"));
        yAxis.setTextSize(16);
        yAxis.setName("Months of the year");
        data.setAxisYLeft(yAxis);
        lineChartView.setLineChartData(data);

        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 700;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);



    }

    public int totalContribution()
    {
        int total = contribution.getJan() + contribution.getFeb() + contribution.getMarch() + contribution.getApril()+
                contribution.getMayy() + contribution.getJune() + contribution.getJuly() + contribution.getaugust() +
                contribution.getSeptemeber() + contribution.getNovember() + contribution.getDecember();
        return  total;
    }
    public String getDateInWords()
    {
        String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December" };
        Calendar cal = Calendar.getInstance();
        String month = monthName[cal.get(Calendar.MONTH)];
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        Log.d("Year","This is the year " + year);
        String date = day +" " + month + " " + year;
        System.out.println("Date name: " + date);

        return date;
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        finish();
        return true;
    }
    public int months()
    {
        int count = 0;
        if(contribution.getJan() != 0)
        {
            count= count + 1;
        }
        if(contribution.getFeb() != 0)
        {
            count= count + 1;
        }
        if(contribution.getMarch() != 0)
        {
            count= count + 1;
        }
        if(contribution.getApril() != 0)
        {
            count= count + 1;
        }
        if(contribution.getMayy() != 0)
        {
            count= count + 1;
        }
        if(contribution.getJune() != 0)
        {
            count= count + 1;
        }
        if(contribution.getJuly() != 0)
        {
            count= count + 1;
        }
        if(contribution.getaugust() != 0)
        {
            count= count + 1;
        }
        if(contribution.getSeptemeber() != 0)
        {
            count= count + 1;
        }
        if(contribution.getOctober() != 0)
        {
            count= count + 1;
        }
        if(contribution.getNovember() != 0)
        {
            count= count + 1;
        }
        if(contribution.getDecember() != 0)
        {
            count= count + 1;
        }

            return count;
    }
}
