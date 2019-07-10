package com.example.admin.chamaapp.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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
    private String UserID;
    private CountryCodePicker countryCodePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

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

                Intent intent = new Intent(Sign.this, Login.class);
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




