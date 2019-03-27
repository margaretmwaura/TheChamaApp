package com.example.admin.chamaapp;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

        private FirebaseDatabase mFirebaseDatabase;
        private String enteredPhoneNumber,mVerificationId;
        private DatabaseReference mref;
        private EditText inputCode;
        private FirebaseAuth auth;
        private ProgressBar progressBar;
        private Button btnSignup, btnLogin, btnReset;
        private String email;
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();

            //the code that has been commented because it causes error since there is no code for logging the user out
            //the code will be uncommneted once the code for logging out has been added
            //if (auth.getCurrentUser() != null) {
            //startActivity(new Intent(login.this, MainActivity.clachrss));
            //finish();
            //}

            setContentView(R.layout.activity_login);


            //blurring the background image
            LinearLayout mContainerView = (LinearLayout) findViewById(R.id.sign);
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
            Bitmap blurredBitmap = BlurBuilder.blur(this, originalBitmap);
            mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
//End of code of blurring the background image

            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mref = mFirebaseDatabase.getReference();
           inputCode = (EditText) findViewById(R.id.verfication_code);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
//            btnSignup = (Button) findViewById(R.id.btn_signup);
            btnLogin = (Button) findViewById(R.id.button_login);
//            btnReset = (Button) findViewById(R.id.btn_reset_password);


            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();


//This intents get the phoneNumber that had been input
            Intent intent = getIntent();
            enteredPhoneNumber = intent.getStringExtra("The phone number ");

//            btnSignup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(login.this, Sign.class));
//                }
//            });
//
//            btnReset.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(login.this, MainActivity.class));
//                }
//            });


//
            setUpTheCallBacks();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    enteredPhoneNumber,        // Phone number to verify
                    120,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallBacks);
        }

//        This will be useful when loging in the user once they log out
//        private void showData(DataSnapshot dataSnapshot)
//        {
//            FirebaseUser user = auth.getCurrentUser();
//            //This is the String representing the users user Id
//
//            String userId = user.getUid();
//
//
//            for(DataSnapshot ds: dataSnapshot.getChildren())
//            {
//                String type = ds.child(userId).getValue(Admin.class).getType();
//
//                Log.i("Account : " , type);
//
//
//                String admin = "Admin";
//                String member = "Member";
//
//                if ( type.equals(admin))
//                {
////                If the type is the same as admin get the other values that an admin has and send them to the other activity
////                Check out the other data pertaining the admin
//
//                    int id =  ds.child(userId).getValue(Admin.class).getID();
//                    String position = ds.child(userId).getValue(Admin.class).getPosition();
//
//                    final Intent s = new Intent( this, Welcome.class).putExtra("myEmail",email)
//                            .putExtra("theAccountType",type)
//                            .putExtra("theIdNumber",id);
//                    startActivity(s);
//
////                    This code is meant for starting the drawer activity will get to it later
//
//
////                    final Intent s = new Intent( this, AdminDrawerActivity.class).putExtra("myEmail",email)
////                            .putExtra("theAccountType",type)
////                            .putExtra("theIdNumber",id)
////                            .putExtra("myPosition",position);
////                    startActivity(s);
//
//                }
//                else if ( type.equals(member))
//                {
////                    If the user is a member get the other values of the member and then send
////                    This is the code
//
////                    Have to create a navigation drawer for the member class
//
//
//                    int id  = ds.child(userId).getValue(Member.class).getMembershipID();
//                    final Intent s = new Intent( this, Welcome.class).putExtra("myEmail",email)
//                            .putExtra("theAccountType",type)
//                            .putExtra("theIdNumber",id);
//                    startActivity(s);
//
//
//                }
//                else
//                {
//                    final Intent s = new Intent( this, Sign.class);
//                    startActivity(s);
//
//                }
//            }
//        }

    private void setUpTheCallBacks()
    {
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {

                String code = phoneAuthCredential.getSmsCode();

                if (code != null) {
                    inputCode.setText(code);
                    //verifying the code
                    verifyVerificationCode(code);
                }


            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                Toast.makeText(Login.this,"Verification failed " + e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("VerificationFailed","This is the message " + e.getMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
            }
        };

    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            UserSessionManager mine = new UserSessionManager(Login.this);
                            mine.createUserLoginSession();

                            Toast.makeText(Login.this,"Successful " , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this,AdminFragment.class);
                            startActivity(intent);
                        }
                        else
                        {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException)
                            {
                                // The verification code entered was invalid
                                Log.d("ErrorVerfiying","This is the error " + task.getException());
                                Log.d("ErrorVerfying","There is an error verrfying the user " + task.getException());
                            }

                            Toast.makeText(Login.this,"Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    }











