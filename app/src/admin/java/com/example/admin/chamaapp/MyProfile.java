package com.example.admin.chamaapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;


public class MyProfile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName,profileEmail, profileTitle;
    private Button editPhoto , editYourName, sendMoneyViaMpesa;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private String value,image;
    Daraja daraja;
    String phoneNumber;
    private EditText editTextPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //        This is about the mpesa library code on sending money


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Toolbar :: Transparent
        toolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBackIcon), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        String title = " ";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#0366ff")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);


        // Status bar :: Transparent
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
              //This single line of code sets the status bar to alert
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }

        //blurring the background image
        LinearLayout mContainerView = (LinearLayout) findViewById(R.id.senior);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.newback);
        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));



        daraja = Daraja.with("08MUHjEBN7qJ5RhfYR008fVFbw0R1i4N", "UOYiGP1PyFkvQL8U", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken)
            {
                Log.d("DarajaCreation","The daraja class has beeen created");
                Log.i(MyProfile.this.getClass().getSimpleName(), accessToken.getAccess_token());
            }

            @Override
            public void onError(String error) {
                Log.d("DarajaCreation","The daraja class has not been created,an error has been encountered" + error);
                Log.e(MyProfile.this.getClass().getSimpleName(), error);
            }
        });
//        This displays the home button arrow

//        This is the email from the intents extras
        Intent intent = getIntent();
         phoneNumber = intent.getStringExtra("Phonenumber");


        profileImage = (ImageView) findViewById(R.id.profile_image);
        profileName = (TextView) findViewById(R.id.your_name);
        profileEmail = (TextView) findViewById(R.id.your_email_address);

//        Setting the email address on the email address text view
        if(phoneNumber != null && !phoneNumber.isEmpty())
        {
            profileEmail.setText(phoneNumber);
            Toast.makeText(this,"Setting email",Toast.LENGTH_LONG).show();
            Log.d("EmialNotThere ", " setting email");
        }
        editPhoto = (Button) findViewById(R.id.edit_profile_photo);
        editYourName = (Button) findViewById(R.id.edit_your_name);

//        editName = (Button) findViewById(R.id.edit_your_name);
//        editTextPhone = (EditText) findViewById(R.id.edit_text_phonenumber);



//        This is meant to get the previously set imageUri
        SharedPreferences settings=getSharedPreferences("prefs",0);
        String image = settings.getString("profileImage"," ");
        String name = settings.getString("userName"," ");
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
                AlertDialog.Builder alert = new AlertDialog.Builder(MyProfile.this);

                alert.setTitle("Edit name");
                alert.setMessage("Enter your new name ");

// Set an EditText view to get user input
                final EditText input = new EditText(MyProfile.this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        value = input.getText().toString();
                        profileName.setText(value);
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


//        sendMoneyViaMpesa.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
////Get Phone Number from User Input
//                phoneNumber = editTextPhone.getText().toString().trim();
//
//                if (TextUtils.isEmpty(phoneNumber)) {
//                    editTextPhone.setError("Please Provide a Phone Number");
//                    return;
//                }
//
//                //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
//                LNMExpress lnmExpress = new LNMExpress(
//                        "174379",
//                        "MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTkwMzI0MTYzOTUw",  //https://developer.safaricom.co.ke/test_credentials
//                        TransactionType.CustomerPayBillOnline,
//                        "1",
//                        "254710120612",
//                        "174379",
//                        "254710120612",
//                        "http://mpesa-requestbin.herokuapp.com/wqzvixwr",
//                        "001ABC",
//                        "Goods Payment"
//                );
//
//                daraja.requestMPESAExpress(lnmExpress,
//                        new DarajaListener<LNMResult>() {
//                            @Override
//                            public void onResult(@NonNull LNMResult lnmResult) {
//                               Log.d("SendingMoney","Money has been sent");
//                            }
//
//                            @Override
//                            public void onError(String error) {
//                                Log.d("SendingMoney","Money has not been sent " + error);
//                            }
//                        }
//                );
//            }
//        });
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
                Toast.makeText(MyProfile.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        Log.d("ClosingTheMyProfile","Method has been called ");
        finish();
        return true;
    }



}
