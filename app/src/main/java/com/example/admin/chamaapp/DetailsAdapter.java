package com.example.admin.chamaapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>
{

    List<Member> membersList = new ArrayList<>();
    Context mContext;
    private OnItemClickListener onItemClickListener;

    public DetailsAdapter(Context context)
    {
        mContext = context;
    }
    public void setClickListener(OnItemClickListener itemClickListener)
    {
        this.onItemClickListener = itemClickListener;
    }
    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
//        this is for getting the id of the layout with the data textView
        int id = R.layout.recycler_view_item;
//        This is for inflating of the layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(id,parent,shouldAttachToParentImmediately);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position)
    {
        Member maggie = membersList.get(position);
        String emailAddress = maggie.getEmailAddress();
        int contribution = maggie.getContribution();
        int attendance = maggie.getAttendance();
        int membershipId = maggie.getMembershipID();

        holder.emailAddress.setText(emailAddress);
        holder.contribution.setText(String.valueOf(contribution));
        holder.attendance.setText(String.valueOf(attendance));
        holder.membershipId.setText(String.valueOf(membershipId));
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    public void setMembersList(List<Member> membersList)
    {
        this.membersList = membersList;
//        Without it no code shows
        notifyDataSetChanged();
    }
    class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView emailAddress, contribution, attendance,membershipId;
        public DetailsViewHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);
            emailAddress = itemView.findViewById(R.id.member_emailAddress);
            contribution = itemView.findViewById(R.id.contribution_textview);
            attendance = itemView.findViewById(R.id.attendance);
            membershipId = itemView.findViewById(R.id.membershipID_textview);


        }

        @Override
        public void onClick(View v)
        {
              onItemClickListener.onClick(v,getAdapterPosition());
        }
    }
}
