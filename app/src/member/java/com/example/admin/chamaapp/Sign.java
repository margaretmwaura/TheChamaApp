package com.example.admin.chamaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Sign extends AppCompatActivity
{

    private EditText inputPhoneNumber;
    private Button  btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String UserID;
    private CountryCodePicker countryCodePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

//blurring the background image
//        LinearLayout mContainerView = (LinearLayout) findViewById(R.id.sign);
//        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
//        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
//        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
//End of code of blurring the background image
        //Get auth instance


        auth = FirebaseAuth.getInstance();
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            //This single line of code sets the status bar to alert
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }



//        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button_with_phonenumber);
//        btnSingUpWithEmail = (Button) findViewById(R.id.sign_up_button_with_email);

        inputPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        countryCodePicker = (CountryCodePicker)findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(inputPhoneNumber);

//        inputPassword = (EditText) findViewById(R.id.password);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

//        emailEditText = (EditText) findViewById(R.id.emailAddress);
//        passwordEditText = (EditText) findViewById(R.id.password);
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

                String phoneNumber = countryCodePicker.getFullNumberWithPlus();
//                progressBar.setVisibility(View.VISIBLE);
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



            }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    }




