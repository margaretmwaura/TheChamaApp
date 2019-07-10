package com.example.admin.chamaapp.View;



import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.chamaapp.Model.Contribution;
import com.example.admin.chamaapp.Model.Member;
import com.example.admin.chamaapp.Presenter.MemberFragmentPresenter;
import com.example.admin.chamaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class MemberFragment extends AppCompatActivity implements View.OnClickListener, MemberFragmentPresenter.View {

    public static final String ARG_PAGE = "ARG_PAGE";
    private FirebaseAuth mAuth;
    private String userId;
    private String type;
    private int memberId = 0;
    private EditText Id;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private String email,phonenumber;
    public MemberFragment() {
        // Required empty public constructor
    }

    private String emailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_mockup);
        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("Phonenumber");

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

        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            //This single line of code sets the status bar to alert
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
        {
            FirebaseUser user = mAuth.getCurrentUser();
            userId = user.getUid();
        }
        btnSubmit = (Button)findViewById(R.id.sign_up_button);
        btnSubmit.setOnClickListener(this);

    }

    //The building of the method for making the status bar completely transparent

    public static void setWindowFlag(MemberFragment activity, final int bits, boolean on) {
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
    public void onClick(View v)
    {
        progressBar.setVisibility(View.VISIBLE);
        type = "Member";
        Id = (EditText) findViewById(R.id.id);
        memberId = Integer.parseInt(Id.getText().toString());

        if (memberId == 0)
        {
            Toast.makeText(MemberFragment.this, "Enter your membershipId!", Toast.LENGTH_SHORT).show();
            return;
        }
        Contribution contribution = new Contribution();
        Member maggie = new Member(memberId, type,phonenumber,contribution);




    }

    @Override
    public void startNavigationDrawer()
    {
        Intent intent = new Intent(MemberFragment.this, TheNavigationDrawer.class);
        startActivity(intent);
    }
}
