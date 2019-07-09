package com.example.admin.chamaapp.admin.View;

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

import com.example.admin.chamaapp.Admin;
import com.example.admin.chamaapp.AppWidgetActivity;
import com.example.admin.chamaapp.Contribution;
import com.example.admin.chamaapp.R;
import com.example.admin.chamaapp.UserViewModel;
import com.example.admin.chamaapp.admin.Presenter.MyDetailsPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MyDetails extends AppCompatActivity implements MyDetailsPresenter.View {

    private TextView phonenumberTextView, dateTextView,montsContributed,totalContributed;
    private String phonenumber , userId,image;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mref;
    private Contribution contribution ;
    private Button sendEmail;
    private Bundle appWidget;
    private int total;
    private MyDetailsPresenter myDetailsPresenter;
    private LineChartView lineChartView ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);


        myDetailsPresenter = new MyDetailsPresenter(this);
//        This is for the AppWidget;
        appWidget = new Bundle();

        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("Phonenumber");
//        Add the phoneNumber to the widget;

        appWidget.putString("PhoneNumber",phonenumber);

        userId = intent.getStringExtra("UserID");
        image = intent.getStringExtra("ImageSet");
        lineChartView = findViewById(R.id.chart);
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
          dateTextView.setText(myDetailsPresenter.getDateInWords());

        SharedPreferences settings=getSharedPreferences("prefs",0);
        String userNameText = settings.getString("userName"," ");


        Log.d("The image string","This is the image String " + image);


        getUserDetailFromDatabase();
        myDetailsPresenter.drawAgraph(contribution);


        montsContributed = (TextView) findViewById(R.id.months_contributed);
        totalContributed = (TextView) findViewById(R.id.total_contributed);

        myDetailsPresenter.months(contribution);
        totalContribution();

        //        Updating the widget
        //            Calling the method for updating the widget data

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
                DataSnapshot userID = dataSnapshot.child("database").child("users").child("Admin").child(userId);

                Boolean exist = userID.exists();

                Log.d("Confirming","This confirms that the datasnapshot user detsila exists " + exist);

                    Admin member = userID.getValue(Admin.class);

                    contribution = member.getContribution();

                    Log.d("ContributionValue","This is the contribution value " +  contribution.getApril());


                myDetailsPresenter.drawAgraph(contribution);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
        finish();
        return true;
    }



    public int totalContribution()
    {
        total = contribution.getJan() + contribution.getFeb() + contribution.getMarch() + contribution.getApril()+
                contribution.getMayy() + contribution.getJune() + contribution.getJuly() + contribution.getaugust() +
                contribution.getSeptemeber() + contribution.getNovember() + contribution.getDecember();

        Log.d("Contribution","This is the contribution" + total);
        totalContributed.setText(String.valueOf(total));

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
    public void displayMonths(int count)
    {
        montsContributed.setText(String.valueOf(count));
    }

    @Override
    public void setLineChartData(LineChartData data) {
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
