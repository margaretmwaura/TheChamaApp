package com.example.admin.chamaapp.View;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.chamaapp.Presenter.UserSessionManager;
import com.example.admin.chamaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.admin.chamaapp.Presenter.ViewGroupUtils.replaceView;


public class Login extends AppCompatActivity {

        private FirebaseDatabase mFirebaseDatabase;
        private String enteredPhoneNumber,mVerificationId;
        private DatabaseReference mref;
        private EditText inputCode;
        private FirebaseAuth auth;
        private ProgressBar progressBar;
        private Button btnLogin, btnSendCode;
        private String userId;
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
        private PhoneAuthProvider.ForceResendingToken resendToken;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();


            setContentView(R.layout.activity_login);

            Window window = this.getWindow();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                //This single line of code sets the status bar to alert
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            }
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mref = mFirebaseDatabase.getReference();
           inputCode = (EditText) findViewById(R.id.verfication_code);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
//            btnSignup = (Button) findViewById(R.id.btn_signup);
            btnLogin = (Button) findViewById(R.id.button_login);
            btnSendCode = (Button) findViewById(R.id.button_send);

            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();


//This intents get the phoneNumber that had been input
            Intent intent = getIntent();
            enteredPhoneNumber = intent.getStringExtra("The phone number ");

            btnSendCode.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    setUpTheCallBacks();
                    verifyPhoneNumber();
                    new CountDownTimer(12000, 1000) {

                        public void onTick(long millisUntilFinished)
                        {
                            btnSendCode.setText("seconds remaining: " + millisUntilFinished / 1000);
                            //here you can have your logic to set text to edittext
                        }

                        public void onFinish()
                        {
                            btnLogin.setVisibility(View.VISIBLE);
                            replaceView(btnSendCode, btnLogin);
                            btnLogin.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    resendVerificationCode(enteredPhoneNumber,resendToken);
                                    new CountDownTimer(12000,1000)
                                    {

                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                            btnLogin.setText("seconds remaining: " + millisUntilFinished / 1000);
                                        }

                                        @Override
                                        public void onFinish()
                                        {
                                            btnLogin.setText("Resend code");
                                        }
                                    }.start();
                                }
                            });

                        }
                    }.start();
                }
            });


        }
        public void verifyPhoneNumber()
            {
             PhoneAuthProvider.getInstance().verifyPhoneNumber
              (
                    enteredPhoneNumber,        // Phone number to verify
                    120,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallBacks);


            }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private void setUpTheCallBacks()
    {
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {

                final String code = phoneAuthCredential.getSmsCode();

                if(code!= null)
                {
                    showDialogBox();
                    inputCode.setText(code);
//                   Show the pop-up dialog showing authentication
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
                resendToken = forceResendingToken;
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


//                                    This gets the users user id
                            userId = auth.getCurrentUser().getUid();

                            String filename = "UserDetails";
                            FileOutputStream outputStream;

                            try {

                                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                String[] userdetails = new String[2];
                                userdetails[0] = userId;
                                userdetails[1] = enteredPhoneNumber;
                                ObjectOutput s = new ObjectOutputStream(outputStream);
                                s.writeObject(userdetails);

                                outputStream.close();

                                Log.d("FileCreation","File has been created");
                            } catch (Exception e)
                            {
                                Log.d("FileCreation","File creation failed " + e.getMessage());
                                e.printStackTrace();
                            }

                            Toast.makeText(Login.this,"Successful " , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this, MemberFragment.class);
                            intent.putExtra("Phonenumber",enteredPhoneNumber);
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
    public void showDialogBox()
    {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.verifcation_layout, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    }












