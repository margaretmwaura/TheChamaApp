package com.example.admin.chamaapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter
{

    private String email;
    private String tabTitles[] = new String[] { "Admin Account", "Member Account", };
    public SimpleFragmentPagerAdapter(FragmentManager fm,String email)
    {
        super(fm);
        this.email = email;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
            return AdminFragment.newInstance(0,email);
        }
        else {
            return MemberFragment.newInstance(1,email);
        }
    }

    @Override
    public int getCount()
    {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position

        return tabTitles[position];
    }


}
