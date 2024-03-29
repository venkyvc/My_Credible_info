package com.thesparksfoundation.phase2.task2.mycredibleinfo;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class CustomAdapter extends FragmentStatePagerAdapter {

    private static final int ITEMS = 3;
    private List<Fragment> mFragmentList;
    private List<String> mFragmentsTitleList;

    public CustomAdapter(FragmentManager fm, List<Fragment> fragments, List<String> title){
        super(fm);
        this.mFragmentList = fragments;
        this.mFragmentsTitleList = title;
    }

    @Override
    public Fragment getItem(int i){
        return mFragmentList.get(i);
    }

    @Override
    public int getCount(){
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return mFragmentsTitleList.get(position);
    }
}
