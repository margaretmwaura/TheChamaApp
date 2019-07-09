package com.example.admin.chamaapp.admin.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.admin.chamaapp.Login;
import com.example.admin.chamaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import androidx.appcompat.app.AppCompatActivity;

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



        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            //This single line of code sets the status bar to alert
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }
        auth = FirebaseAuth.getInstance();

        btnSignUp = (Button) findViewById(R.id.sign_up_button_with_phonenumber);


        inputPhoneNumber = (EditText) findViewById(R.id.phoneNumber);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

//This is meant for the country code picker

        countryCodePicker = (CountryCodePicker)findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(inputPhoneNumber);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             getUserInputDetail();
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
    public void getUserInputDetail()
    {
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
    

}
