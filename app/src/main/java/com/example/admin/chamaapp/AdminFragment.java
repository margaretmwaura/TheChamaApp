package com.example.admin.chamaapp;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
public class AdminFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    public String type;
    private EditText leadership,Id;
    private FirebaseAuth mAuth;
    private String userId;
    private Button btnSubmit;
    private ProgressBar progressBar;

    public AdminFragment() {
        // Required empty public constructor
    }



    public static AdminFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AdminFragment fragment = new AdminFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_admin_mockup, container, false);

        /** TODO: Insert all the code from the NumberActivity’s onCreate() method after the setContentView method call */
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        if (mAuth.getCurrentUser() != null)
        {
            FirebaseUser user = mAuth.getCurrentUser();
            userId = user.getUid();
        }

        btnSubmit = (Button) rootView.findViewById(R.id.sign_up_button);
        btnSubmit.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v){

        progressBar.setVisibility(View.VISIBLE);
        leadership = (EditText) getActivity().findViewById(R.id.post);
        Id = (EditText) getActivity().findViewById(R.id.id);

        String post = leadership.getText().toString();
        int id = Integer.parseInt(Id.getText().toString());
        type = "Admin";


        if (TextUtils.isEmpty(post)) {
            Toast.makeText(getActivity().getApplicationContext(), "Enter your electoral post!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (id == 0) {
            Toast.makeText(getActivity().getApplicationContext(), "Enter the date which you were elected!", Toast.LENGTH_SHORT).show();
            return;
        }

        Admin maggie = new Admin(type, post, id);

        mref.child("users").child(userId).setValue(maggie).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(getActivity().getApplicationContext(), "Data has been added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),   TheNavigationDrawer.class);
                startActivity(intent);
            }

        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(getActivity().getApplicationContext(), "Data has been not been added please try again ", Toast.LENGTH_SHORT).show();
            }
        });




    }

}



