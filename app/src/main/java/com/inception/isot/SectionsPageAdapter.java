package com.inception.isot;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gajanan on 06-08-2017.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();


    public SectionsPageAdapter(FragmentManager fm) {

        super(fm);
    }



    public void addFragment(Fragment fragment, String tile)
    {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(tile);
    }

    public void addFragment(int position, Fragment fragment, String title)
    {
        mFragmentList.set(position,fragment);
        mFragmentTitleList.set(position,title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }




}
