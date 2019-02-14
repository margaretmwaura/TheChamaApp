package com.example.admin.chamaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Sign extends AppCompatActivity
{

    private EditText inputPhoneNumber,emailEditText,passwordEditText;
    private Button  btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Button btnSingUpWithEmail;
    private String email,password , userId;
    private static String emailStringToBeStored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

//blurring the background image
        LinearLayout mContainerView = (LinearLayout) findViewById(R.id.sign);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
//End of code of blurring the background image
        //Get auth instance
        auth = FirebaseAuth.getInstance();

//        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button_with_phonenumber);
        btnSingUpWithEmail = (Button) findViewById(R.id.sign_up_button_with_email);

        inputPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
//        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        emailEditText = (EditText) findViewById(R.id.emailAddress);
        passwordEditText = (EditText) findViewById(R.id.password);
        //have to change this to its correct version which was ResetPasswordActivity.class
        //Remember to change for the sake of syntax and semantics
//        btnResetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Sign.this, Welcome.class));
//            }
//        });
//
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });



//        This will be used when i am rolling out the app
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = inputPhoneNumber.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                //create user

                if(phoneNumber.isEmpty() || phoneNumber.length() < 10)
                {
                    inputPhoneNumber.setError("Enter a valid phone number");
                    inputPhoneNumber.requestFocus();
                }

                Intent intent = new Intent(Sign.this,Login.class);
                intent.putExtra("The phone number ",phoneNumber);
                startActivity(intent);
            }
        });



//        This is what I will be used at the moment until i come to rolling out the app

//        This will be deleted just for testing purposes
        btnSingUpWithEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(Sign.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    UserSessionManager mine = new UserSessionManager(Sign.this);
                                    mine.createUserLoginSession();
                                    Toast.makeText(Sign.this,"The signing was successful " , Toast.LENGTH_LONG).show();

//                                    This gets the users user id
                                    userId = auth.getCurrentUser().getUid();

                                    Log.d("TheUsersId","This is the users id " + userId);
//                               This code is for saving of the userID and email to the internal files

                                    String filename = "UserDetails";
                                    FileOutputStream outputStream;

                                    try {

                                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                       String[] userdetails = new String[2];
                                       userdetails[0] = userId;
                                       userdetails[1] = email;
                                        ObjectOutput s = new ObjectOutputStream(outputStream);
                                        s.writeObject(userdetails);

                                        outputStream.close();

                                        Log.d("FileCreation","File has been created");
                                    } catch (Exception e)
                                    {
                                        Log.d("FileCreation","File creation failed " + e.getMessage());
                                        e.printStackTrace();
                                    }

//                                    End of writing data to the file
                                    emailStringToBeStored = email;
                                    Log.d("TheEmail","This is the email " + emailStringToBeStored);

//                                    Add the phone number as an extra
                                    Intent intent = new Intent(Sign.this,ThePager.class);
                                    intent.putExtra("EmailAddress",email);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(Sign.this,"Failed",Toast.LENGTH_LONG).show();
//                                    Toast.makeText(Login.this,"This is the entered email and password " + emailString + passwordString , Toast.LENGTH_LONG).show();
                                    Log.d("Exception","This is the cause of the error " + task.getException());
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
    public static String returnEmail()
    {
        return emailStringToBeStored;
    }

}
