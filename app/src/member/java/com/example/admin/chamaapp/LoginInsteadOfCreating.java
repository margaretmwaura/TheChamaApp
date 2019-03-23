package com.example.admin.chamaapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginInsteadOfCreating extends AppCompatActivity {

    private EditText emailEditText,passwordEditText;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Button btnSingUpWithEmail;
    private String email,password;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_instead_of_creating);

//blurring the background image
        LinearLayout mContainerView = (LinearLayout) findViewById(R.id.sign);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
//End of code of blurring the background image
        //Get auth instance
        auth = FirebaseAuth.getInstance();

//        btnSignIn = (Button) findViewById(R.id.sign_in_button);

        btnSingUpWithEmail = (Button) findViewById(R.id.sign_up_button_with_email);


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





//        This is what I will be used at the moment until i come to rolling out the app

//        This will be deleted just for testing purposes
        btnSingUpWithEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(LoginInsteadOfCreating.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(LoginInsteadOfCreating.this,"The signing was successful " , Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginInsteadOfCreating.this,NavigationDrawerMember.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(LoginInsteadOfCreating.this,"Failed",Toast.LENGTH_LONG).show();
                                    Toast.makeText(LoginInsteadOfCreating.this,"This is the entered email and password " + email + password , Toast.LENGTH_LONG).show();
                                    Log.d("Exception","This is the cause of the error ");
                                }
                            }
                        });

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginInsteadOfCreating.this, new OnSuccessListener<InstanceIdResult>()
        {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult)
            {
                token = instanceIdResult.getToken();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public static String returnRegistrationToken()
    {
        return token;
    }
}
