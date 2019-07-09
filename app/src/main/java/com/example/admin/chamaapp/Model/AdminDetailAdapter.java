package com.example.admin.chamaapp.Model;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.chamaapp.Presenter.OnItemClickListenerWithType;
import com.example.admin.chamaapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AdminDetailAdapter extends RecyclerView.Adapter<AdminDetailAdapter.AdminDetailViewHolder>
{
    List<Admin> AdminList = new ArrayList<>();
    Context mContext;
    private OnItemClickListenerWithType onItemClickListener;
    private String admin = "Admin";

    public AdminDetailAdapter(Context context)
    {
        mContext = context;
    }
    public void setClickListener(OnItemClickListenerWithType itemClickListener)
    {
        this.onItemClickListener = itemClickListener;
    }
    @Override
    public AdminDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
//        this is for getting the id of the layout with the data textView
        int id = R.layout.recycler_view_item;
//        This is for inflating of the layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(id,parent,shouldAttachToParentImmediately);
        return new AdminDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminDetailViewHolder holder, int position)
    {
        Admin maggie = AdminList.get(position);
        String phonenumber = maggie.getPhonenumber();

        holder.emailAddress.setText("Owner " + phonenumber);
        holder.membershipId.setText(String.valueOf(maggie.getID()));
        Contribution contribution = maggie.getContribution();
        int total = getTotalContribution(contribution);
        holder.contribution.setText(String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return AdminList.size();
    }

    public void setAdminList(List<Admin> membersList)
    {
        this.AdminList = membersList;
//        Without it no code shows
        notifyDataSetChanged();
    }
    class AdminDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView emailAddress, contribution,membershipId;
        public AdminDetailViewHolder(View itemView)
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
            onItemClickListener.onClick(v,getAdapterPosition(),admin);
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
