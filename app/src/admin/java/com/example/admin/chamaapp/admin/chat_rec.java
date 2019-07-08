package com.example.admin.chamaapp.admin;
import android.view.View;
import android.widget.TextView;
import com.example.admin.chamaapp.R;

import androidx.recyclerview.widget.RecyclerView;


public class chat_rec extends RecyclerView.ViewHolder
{
    TextView leftText,rightText;

    public chat_rec(View itemView){
        super(itemView);

        leftText = (TextView)itemView.findViewById(R.id.leftText);
        rightText = (TextView)itemView.findViewById(R.id.rightText);


    }
}
