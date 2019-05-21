package com.example.admin.chamaapp;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends AppCompatActivity implements View.OnClickListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    public String type;
    private EditText leadership,Id;
    private FirebaseAuth mAuth;
    private String userId;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private String email;
    public AdminFragment() {
        // Required empty public constructor
    }
//
//
//
//    public static AdminFragment newInstance(int page,String email) {
//        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, page);
//        args.putString("EmailAddress",email);
//        AdminFragment fragment = new AdminFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.activity_admin_mockup, container, false);
//
//        /** TODO: Insert all the code from the NumberActivity’s onCreate() method after the setContentView method call */
//        if(getArguments() != null)
//        {
//            email = getArguments().getString("EmailAddress");
//        }
//        Toast.makeText(getActivity().getApplicationContext(),"This is the email Address " + email, Toast.LENGTH_LONG).show();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mref = mFirebaseDatabase.getReference();
//        mAuth = FirebaseAuth.getInstance();
//        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
//        if (mAuth.getCurrentUser() != null)
//        {
//            FirebaseUser user = mAuth.getCurrentUser();
//            userId = user.getUid();
//        }
//
//        btnSubmit = (Button) rootView.findViewById(R.id.sign_up_button);
//        btnSubmit.setOnClickListener(this);
//        return rootView;
//    }

    private String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mockup);

        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("Phonenumber");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();

//        mref.child("users").removeValue();


        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        if (mAuth.getCurrentUser() != null)
        {
            FirebaseUser user = mAuth.getCurrentUser();
            userId = user.getUid();
        }

        btnSubmit = (Button)findViewById(R.id.sign_up_button);
        btnSubmit.setOnClickListener(this);
        //This is the code for changing the color of the navigation bar
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
        //End of code for making the navigation bar colored


        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
//End of the code for making the navigation bar completely transparent
        //Not to be deleted under any circumstances




//blurring the background image
//        LinearLayout mContainerView = (LinearLayout) findViewById(R.id.signup_admin);
//        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
//        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
//        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
//End of code of blurring the background image



//        This code is for animating the circle
//        Will get to it later


//        Circle circle = (Circle) findViewById(R.id.circle);
//
//        CircleAngleAnimation animation = new CircleAngleAnimation(circle, 320);
//        animation.setDuration(10000);
//        circle.startAnimation(animation);
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            //This single line of code sets the status bar to alert
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }


    }

    //The building of the method for making the status bar completely transparent

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onClick(View v){

        progressBar.setVisibility(View.VISIBLE);
        leadership = (EditText)findViewById(R.id.post);
        Id = (EditText)findViewById(R.id.id);

        String post = leadership.getText().toString();
        int id = Integer.parseInt(Id.getText().toString());
        type = "Admin";


        if (TextUtils.isEmpty(post)) {
            Toast.makeText(this, "Enter your electoral post!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (id == 0)
        {
            Toast.makeText(this, "Enter the date which you were elected!", Toast.LENGTH_SHORT).show();
            return;
        }
        Contribution contribution = new Contribution();

        Admin maggie = new Admin(type, post, id,phonenumber,contribution);

        mref.child("database").child("users").child("Admin").child(userId).setValue(maggie).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(AdminFragment.this, "Data has been added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminFragment.this,TheNavigationDrawer.class);
                startActivity(intent);
            }

        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(AdminFragment.this, "Data has been not been added please try again ", Toast.LENGTH_SHORT).show();
            }
        });




    }

}



