package com.example.admin.chamaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContributionAdapter extends RecyclerView.Adapter<ContributionAdapter.contributionViewHolder>
{
    List<Contribution> contributionList = new ArrayList<>();
    Context mContext;

    public ContributionAdapter(Context context)
    {
        this.mContext = context;
    }
    @NonNull
    @Override
    public contributionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
//        this is for getting the id of the layout with the data textView
        int id = R.layout.contribution_list;
//        This is for inflating of the layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(id,parent,shouldAttachToParentImmediately);
        return new contributionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull contributionViewHolder holder, int position)
    {
       Contribution contribution = contributionList.get(position);
        holder.userContributionTextView.setText("Jan contribution " + String.valueOf(contribution.getJan()));
        holder.userContributionTextView.append("\n" + "Feb contribution " +String.valueOf(contribution.getFeb()));
        holder.userContributionTextView.append("\n" + "Mar contribution " + String.valueOf(contribution.getMarch()));
        holder.userContributionTextView.append("\n" + "Apr contribution " + String.valueOf(contribution.getApril()));
        holder.userContributionTextView.append("\n" + "May contribution " + String.valueOf(contribution.getMayy()));
        holder.userContributionTextView.append("\n" + "Jun contribution " + String.valueOf(contribution.getJune()));
        holder.userContributionTextView.append("\n" + "Jul contribution " + String.valueOf(contribution.getJuly()));
        holder.userContributionTextView.append("\n" + "Aug contribution " + String.valueOf(contribution.getaugust()));
        holder.userContributionTextView.append("\n" + "Sep contribution " + String.valueOf(contribution.getSeptemeber()));
        holder.userContributionTextView.append("\n" + "Oct contribution " + String.valueOf(contribution.getOctober()));
        holder.userContributionTextView.append("\n" + "Nov contribution " + String.valueOf(contribution.getNovember()));
        holder.userContributionTextView.append("\n" + "Dec contribution " + String.valueOf(contribution.getDecember()));
    }

    @Override
    public int getItemCount() {
        return contributionList.size();
    }

    public void setContributionList(List<Contribution> contributionList)
    {
        this.contributionList = contributionList;
        notifyDataSetChanged();
    }
    class contributionViewHolder extends RecyclerView.ViewHolder
    {
        private TextView userPhoneNumber;
        private TextView userContributionTextView;
        public contributionViewHolder(View itemView)
        {
            super(itemView);
            userPhoneNumber = itemView.findViewById(R.id.phoneNumber_textView);
            userContributionTextView = itemView.findViewById(R.id.contribution_textView);
        }
    }
}
