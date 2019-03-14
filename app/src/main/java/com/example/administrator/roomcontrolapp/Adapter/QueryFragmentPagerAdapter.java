package com.example.administrator.roomcontrolapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class QueryFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> queryFragments;

    public QueryFragmentPagerAdapter(FragmentManager fm,List<Fragment> queryFragments) {
        super(fm);
        this.queryFragments = queryFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return queryFragments.get(position);
    }

    @Override
    public int getCount() {
        return queryFragments.size();
    }
}
