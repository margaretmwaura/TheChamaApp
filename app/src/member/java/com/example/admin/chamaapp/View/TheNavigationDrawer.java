package com.example.admin.chamaapp.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.example.admin.chamaapp.Presenter.BlurBuilder;
import com.example.admin.chamaapp.Presenter.TheNavigationDrawerPresenter;
import com.example.admin.chamaapp.R;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class TheNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TheNavigationDrawerPresenter.View {

    private boolean ShouldExecuteOnReusme = true;
    private String[] userDetails = new String[2];
    private String phonenumber ;
    private String userId;
    private String imageUrl;
    private ImageView imgvw;
    private TextView userName;

    private ImageView profileImage;
    private TextView profileName,profileEmail, profileTitle;
    private Button editPhoto , editYourName, sendMoneyViaMpesa;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private String value, name,image;
    Daraja daraja;
    String phoneNumber;
    private EditText editTextPhone;
    private TheNavigationDrawerPresenter theNavigationDrawerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ShouldExecuteOnReusme = false;
        setContentView(R.layout.activity_navigation_drawer_member);

        theNavigationDrawerPresenter = new TheNavigationDrawerPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBackIcon), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        Log.d("OnCreate","oncreate method has been called ");

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
                Log.d("DrawerOpening","Drawer has been opened from onCreate");
            }
        },1500);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //        This method will retrun the users details that are important for the user profile
        userDetails = theNavigationDrawerPresenter.getUserDetails();
        userId = userDetails[0];
        phonenumber = userDetails[1];



//        This is the part where i add the h.eader programmatically
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setItemTextColor(null);
        //inflate header layout
        View navView =  navigationView.inflateHeaderView(R.layout.nav_header_navigation_drawer_member);
//reference to views
        imgvw = (ImageView)navView.findViewById(R.id.profile_image_the_nav);
        TextView tv = (TextView)navView.findViewById(R.id.textView);
        userName = (TextView)navView.findViewById(R.id.textview_user_name);


        //        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        image = settings.getString("profileImage"," ");
        name = settings.getString("userName"," ");
        userName.setText(name);

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
            imgvw.setImageResource(R.drawable.face);
        }

//set views
//        imgvw.setImageResource(R.drawable.image1);
        tv.setText(phonenumber);
        navigationView.setNavigationItemSelectedListener(this);

       includeLayout();
    }

    @Override
    protected void onResume()
    {
        Log.d("OnResume","OnResume method has been called ");
        super.onResume();

        //        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        image = settings.getString("profileImage"," ");
        name = settings.getString("userName"," ");
        userName.setText(name);

        Log.d("The image string","This is the image String " + image);
//        Had to use the " " because there was a default value that had been set if the image is not found
        if(!image.equals(" "))
        {
            Uri imageUri = Uri.parse(image);
            imgvw.setImageURI(imageUri);
            profileImage.setImageURI(imageUri);
            Toast.makeText(this,"The image had been set ",Toast.LENGTH_LONG).show();
        }
        else
        {
//            Will look for a better image
            Toast.makeText(this,"No image has been set yet",Toast.LENGTH_LONG).show();
            imgvw.setImageResource(R.drawable.face);
        }

        if(!name.equals(" "))
        {
            profileName.setText(name);
        }
        else
        {
            profileName.setText("Enter your name");
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
                drawer.openDrawer(GravityCompat.START);
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
        getMenuInflater().inflate(R.menu.navigation_drawer_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        if (id == R.id.nav_my_details)
        {

            Intent intent = new Intent(this, MyDetails.class);
            intent.putExtra("Phonenumber",phonenumber);
            intent.putExtra("UserID",userId);
            intent.putExtra("ImageSet",image);
            startActivity(intent);
        }
        if (id == R.id.nav_events)
        {
            Intent intent = new Intent(this, EventActivity.class);
            startActivity(intent);
        }
        if(id == R.id.nav_general_chat)
        {
//            The email is important so that the users can see who has added a chat
            Intent intent = new Intent(this, TheAllChat.class);
            intent.putExtra("Phonenumber",phonenumber);
            intent.putExtra("UserID",userId);
            startActivity(intent);
        }

        if(id == R.id.nav_admin_only)
        {
//            The email is important so that the users can see who has added a chat
            Intent intent = new Intent(this, AdminOnlyChat.class);
            intent.putExtra("Phonenumber",phonenumber);
            intent.putExtra("UserID",userId);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void includeLayout()
    {
        View view = findViewById(R.id.senior);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.newback);
        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        view.setBackground(new BitmapDrawable(getResources(), blurredBitmap));

        profileImage = (ImageView) view.findViewById(R.id.profile_image);
        profileName = (TextView) view.findViewById(R.id.your_name);
        profileEmail = (TextView) view.findViewById(R.id.your_email_address);

//        Setting the email address on the email address text view
        if(phoneNumber != null && !phoneNumber.isEmpty())
        {
            profileEmail.setText(phoneNumber);
            Toast.makeText(this,"Setting email",Toast.LENGTH_LONG).show();
            Log.d("EmialNotThere ", " setting email");
        }
        editPhoto = (Button) view.findViewById(R.id.edit_profile_photo);
        editYourName = (Button) view.findViewById(R.id.edit_your_name);

        if(!image.equals(" "))
        {
            Uri imageUri = Uri.parse(image);
            profileImage.setImageURI(imageUri);
            Toast.makeText(this,"The image had been set ",Toast.LENGTH_LONG).show();
        }
        else
        {
//            Will look for a better image
            Toast.makeText(this,"No image has been set yet",Toast.LENGTH_LONG).show();
            profileImage.setImageResource(R.drawable.face);
        }
        if(!name.equals(" "))
        {
            profileName.setText(name);
        }
        else
        {
            profileName.setText("Enter your name");
        }

        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                This method should be called once all the permissions have been granted
                requestRead();
            }
        });

        editYourName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(TheNavigationDrawer.this);

                alert.setTitle("Edit name");
                alert.setMessage("Enter your new name ");

// Set an EditText view to get user input
                final EditText input = new EditText(TheNavigationDrawer.this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        value = input.getText().toString();
                        profileName.setText(value);
                        userName.setText(value);
                        SharedPreferences profileImage =getSharedPreferences("prefs",0);
                        SharedPreferences.Editor editor= profileImage.edit();
                        editor.putString("userName",value);
                        editor.commit();
                        // Do something with value!
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            String path = "";
            if (requestCode == 1) {

                if (resultCode == RESULT_OK)
                {
                    final Uri imageUri = data.getData();
                    Log.d("image selected path", imageUri.getPath());
                    System.out.println("image selected path"
                            + imageUri.getPath());
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(this);
                    }
                    builder.setTitle("Change profile Image")
                            .setMessage("This is the image you want to set \n as your profile image")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // continue with setting the image
                                    profileImage.setImageURI(imageUri);
                                    imgvw.setImageURI(imageUri);
                                    image= imageUri.toString();
//                                    Saving to the shared preferences was for the purposes of use when the activity had been destroyed
                                    String toStroreString = imageUri.toString();
                                    SharedPreferences profileImage =getSharedPreferences("prefs",0);
                                    SharedPreferences.Editor editor= profileImage.edit();
                                    editor.putString("profileImage",toStroreString);
                                    editor.commit();


                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // the image is retained
                                }
                            })

//                            Will change the icon for purposes of UI/UX
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();



                }
                //DO other stuff

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void startingUpTheCameraPicker()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }
    public void requestRead()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
//             In this code the user is requesting for permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        else
        {
//                In this case the permissions have been offered already
            startingUpTheCameraPicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
//                In this case the permission has been granted and the user can start the camera picker
                startingUpTheCameraPicker();
            } else {
                // Permission Denied
                Toast.makeText(TheNavigationDrawer.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public File getTheActivityFile() {
        File directory = this.getFilesDir();
        return directory;
    }
}
