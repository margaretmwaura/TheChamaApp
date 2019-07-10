package com.example.admin.chamaapp.View;

import android.appwidget.AppWidgetManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.chamaapp.Model.Contribution;
import com.example.admin.chamaapp.Model.Member;
import com.example.admin.chamaapp.Model.UserViewModel;
import com.example.admin.chamaapp.Presenter.MyDetailsPresenterMember;
import com.example.admin.chamaapp.R;
import com.google.firebase.database.DataSnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MyDetails extends AppCompatActivity implements MyDetailsPresenterMember.View {

    private TextView phonenumberTextView , monthsContribued, totalContributed;
    private TextView dateTextView;
    private String image,userId,phonenumber;
    private Contribution contribution;
    private Button sendEmail;
    private Bundle appWidget;
    private LineChartView lineChartView ;
    private MyDetailsPresenterMember myDetailsPresenterMember;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);

        myDetailsPresenterMember = new MyDetailsPresenterMember(this);
        appWidget = new Bundle();

        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("Phonenumber");
        userId = intent.getStringExtra("UserID");
        image = intent.getStringExtra("ImageSet");
        lineChartView = findViewById(R.id.chart);
        appWidget.putString("PhoneNumber",phonenumber);
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
            appWidget.putString("Image",image);
        }
        else
        {
            circleImageView.setImageResource(R.drawable.face);
        }
        getSupportActionBar().setCustomView(v);

        contribution = new Contribution();


        dateTextView = (TextView) findViewById(R.id.date_textView);
        dateTextView.setText(myDetailsPresenterMember.getDateInWords());
//        Retrieve data saved on the sharedPreferences
        //        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        String userNameText = settings.getString("userName"," ");


        Log.d("The image string","This is the image String " + image);

//Getting the details from the extras
        getUserDetailFromDatabase();
        myDetailsPresenterMember.drawAgraph(contribution);

        totalContributed = (TextView)findViewById(R.id.total_contribution);
        totalContributed.setText(String.valueOf(totalContribution()));
        monthsContribued = (TextView) findViewById(R.id.months_contributed);
        monthsContribued.setText(String.valueOf(myDetailsPresenterMember.months(contribution)));

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




                myDetailsPresenterMember.drawAgraph(contribution);
            }
        });
    }

    public int totalContribution()
    {
        int total = contribution.getJan() + contribution.getFeb() + contribution.getMarch() + contribution.getApril()+
                contribution.getMayy() + contribution.getJune() + contribution.getJuly() + contribution.getaugust() +
                contribution.getSeptemeber() + contribution.getNovember() + contribution.getDecember();

        appWidget.putString("Contribution",String.valueOf(total));

        String phonenumber = appWidget.getString("PhoneNumber");
        String contribution = appWidget.getString("Contribution");
        Log.d("Phonenumber","Widget phone number " + phonenumber);
        Log.d("Contribution","Widget contribution " + contribution);


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, AppWidgetActivity.class));
        AppWidgetActivity.wiringUpTheWidget(this,appWidgetManager,appWidgetIds,appWidget);
        Toast.makeText(this,"The Widget has been updated ",Toast.LENGTH_LONG).show();
        return  total;
    }


    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        finish();
        return true;
    }

    @Override
    public void setLineChartData(LineChartData data)
    {
        lineChartView.setLineChartData(data);
    }

    @Override
    public void setLinecChartDataViewPort()
    {
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 700;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}
