package com.example.admin.chamaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;

public class TheNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean ShouldExecuteOnReusme = true;
    private String[] userDetails = new String[2];
    private String email ;
    private String userId;
    private String imageUrl;
    private ImageView  imgvw;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ShouldExecuteOnReusme = false;
        setContentView(R.layout.activity_the_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("OnCreate","oncreate method has been called ");
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);
                Log.d("DrawerOpening","Drawer has been opened from onCreate");
            }
        },1500);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //        This method will retrun the users details that are important for the user profile
        userDetails = getUserDetails();
        userId = userDetails[0];
        email = userDetails[1];



//        This is the part where i add the h.eader programmatically
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //inflate header layout
        View navView =  navigationView.inflateHeaderView(R.layout.nav_header_the_navigation_drawer);
//reference to views
        imgvw = (ImageView)navView.findViewById(R.id.profile_image_the_nav);
        TextView tv = (TextView)navView.findViewById(R.id.textView);
        userName = (TextView)navView.findViewById(R.id.textview_user_name);


        //        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        String image = settings.getString("profileImage"," ");
        String userNameText = settings.getString("userName"," ");
        userName.setText(userNameText);

        Log.d("The image string","This is the image String " + image);
//        Had to use the " " because there was a default value that had been set if the image is not found
        if(!image.equals(" "))
        {
            Uri imageUri = Uri.parse(image);
            imgvw.setImageURI(imageUri);
            Toast.makeText(this,"The image had been set ",Toast.LENGTH_LONG).show();
        }
        else
        {
//            Will look for a better image
            Toast.makeText(this,"No image has been set yet",Toast.LENGTH_LONG).show();
            imgvw.setImageResource(R.drawable.image1);
        }

//set views
//        imgvw.setImageResource(R.drawable.image1);
        tv.setText(email);
        navigationView.setNavigationItemSelectedListener(this);




    }

    @Override
    protected void onResume()
    {
        Log.d("OnResume","OnResume method has been called ");
        super.onResume();

        //        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        String image = settings.getString("profileImage"," ");
        String userNameText = settings.getString("userName"," ");
        userName.setText(userNameText);

        Log.d("The image string","This is the image String " + image);
//        Had to use the " " because there was a default value that had been set if the image is not found
        if(!image.equals(" "))
        {
            Uri imageUri = Uri.parse(image);
            imgvw.setImageURI(imageUri);
            Toast.makeText(this,"The image had been set ",Toast.LENGTH_LONG).show();
        }
        else
        {
//            Will look for a better image
            Toast.makeText(this,"No image has been set yet",Toast.LENGTH_LONG).show();
            imgvw.setImageResource(R.drawable.image1);
        }

        if(ShouldExecuteOnReusme)
        {
            openDrawer();
            Log.d("DrawerOpening","Drawer has been opened from onResume");
        }
        else
        {
            Log.d("DrawerOpening","Drawer has not being opened");
            ShouldExecuteOnReusme = true;
        }
    }

    private void openDrawer()
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);
            }
        },1000);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    This has worked
    @Override
    protected void onPause() {
        super.onPause();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.the_navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_account)
        {
            // Handle the camera action
//            Clicking on this option should lead one to the myAccount page
            Intent intent = new Intent(this,MyProfile.class);
            intent.putExtra("UserEmail",email);
            startActivity(intent);
        }
        if (id == R.id.nav_my_details)
        {
            // Handle the camera action
//            Clicking on this option should lead one to the myAccount page
            Intent intent = new Intent(this,MyDetails.class);
            intent.putExtra("UserEmail",email);
            intent.putExtra("UserID",userId);
            startActivity(intent);
        }
        if (id == R.id.nav_all_members_details)
        {
            // Handle the camera action
//            Clicking on this option should lead one to the myAccount page
            Intent intent = new Intent(this,AllDetails.class);

            startActivity(intent);
        }
        if (id == R.id.nav_events)
        {
            // Handle the camera action
//            Clicking on this option should lead one to the myAccount page
            Intent intent = new Intent(this,EnterAnEvent.class);

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String[] getUserDetails ()
    {
        //        This is reading the email from the file

        String[] details = new String[2];
        String detail;
        File directory = this.getFilesDir();
        File file = new File(directory,"UserDetails" );

//        String yourFilePath = this.getFilesDir() + "/" + ".txt";
//        File yourFile = new File( yourFilePath );

        try {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(in);

            details = new String[2];
            details = (String[]) s.readObject();

            for(int i = 0; i<details.length;i++)
            {
                detail = details[i];
                Log.d("UserDetails","This are the user details " + detail);
            }
            return details;
        }
        catch (Exception e)
        {
            Log.d("ErrorFileReading","Error encountered while reading the file " + e.getMessage());

            return details;

        }

    }
}
