package com.example.admin.chamaapp;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>
{

    List<Member> membersList = new ArrayList<>();
    Context mContext;
    private OnItemClickListenerWithType onItemClickListener;
    public String member = "member";

    public DetailsAdapter(Context context)
    {
        mContext = context;
    }
    public void setClickListener(OnItemClickListenerWithType itemClickListener)
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
        String phonenumber = maggie.getPhonenumber();

        holder.emailAddress.setText("Owner " + phonenumber);
        holder.membershipId.setText(String.valueOf(maggie.getMembershipID()));
        Contribution contribution = maggie.getContribution();
        int total = getTotalContribution(contribution);
        holder.contribution.setText(String.valueOf(total));
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

        TextView emailAddress, contribution,membershipId;
        public DetailsViewHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);
            emailAddress = itemView.findViewById(R.id.member_phonenumber);
            contribution = itemView.findViewById(R.id.contribute_textView_details);
            membershipId = itemView.findViewById(R.id.user_id_textview);


        }

        @Override
        public void onClick(View v)
        {
              onItemClickListener.onClick(v,getAdapterPosition(),member);
        }
    }

    public int getTotalContribution(Contribution contribution)
    {
        int total = contribution.getJan() + contribution.getFeb() + contribution.getMarch() + contribution.getApril()+
                contribution.getMayy()+ contribution.getJune() + contribution.getJuly() + contribution.getaugust()+
                contribution.getSeptemeber()+ contribution.getOctober()+ contribution.getNovember()+ contribution.getDecember();

        return total;
    }




}
