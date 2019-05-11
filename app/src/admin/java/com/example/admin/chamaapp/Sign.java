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
    private String userId;
    private CountryCodePicker countryCodePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

////blurring the background image
//        LinearLayout mContainerView = (LinearLayout) findViewById(R.id.sign);
//        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
//        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
//        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
//End of code of blurring the background image
        //Get auth instance
        auth = FirebaseAuth.getInstance();

//        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button_with_phonenumber);


        inputPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
//        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//This is meant for the country code picker

        countryCodePicker = (CountryCodePicker)findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(inputPhoneNumber);


//        This will be used when i am rolling out the app
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = countryCodePicker.getFullNumberWithPlus();

                //create user

                if(phoneNumber.isEmpty() || phoneNumber.length() < 10)
                {
                    inputPhoneNumber.setError("Enter a valid phone number");
                    inputPhoneNumber.requestFocus();
                }
                else {
                    Intent intent = new Intent(Sign.this, Login.class);
                    intent.putExtra("The phone number ", phoneNumber);
                    startActivity(intent);
                }
            }
        });



//        This is what I will be used at the moment until i come to rolling out the app

    }




}
