package com.example.admin.chamaapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.admin.chamaapp.Sign.returnEmail;

public class MyProfile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName,profileEmail;
    private Button editPhoto , editYourName;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

//        This displays the home button arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        profileImage = (ImageView) findViewById(R.id.profile_image);
        profileName = (TextView) findViewById(R.id.your_name);
        profileEmail = (TextView) findViewById(R.id.your_email_address);

        editPhoto = (Button) findViewById(R.id.edit_profile_photo);
        editYourName = (Button) findViewById(R.id.edit_your_name);
//        editName = (Button) findViewById(R.id.edit_your_name);

//        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        String image = settings.getString("profileImage"," ");


        //        This is meant to get the email address

        String email = returnEmail();
        Log.d("Email","This is the email " + email);
        profileEmail.setText(returnEmail());



        Log.d("The image string","This is the image String " + image);
//        Had to use the " " because there was a default value that had been set if the image is not found
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
            profileImage.setImageResource(R.drawable.image1);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(MyProfile.this);

                alert.setTitle("Edit name");
                alert.setMessage("Enter your new name ");

// Set an EditText view to get user input
                final EditText input = new EditText(MyProfile.this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        profileName.setText(value);
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

//                    Having a dialog that lets one say yes or no to the image
//                    This is just for the user experience
//This code will be used when the admin is editting the users entries
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
    public void requestRead() {
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
                Toast.makeText(MyProfile.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
