package com.flloyd.mymoviememoir.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.flloyd.mymoviememoir.fragment.BarFragment;
import com.flloyd.mymoviememoir.fragment.PieChartFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private static final String[] TABTITLE = new String[]{"Watched By Location","Watched By Months"};

    public SectionsPagerAdapter( FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new PieChartFragment();
                break;
            case  1:
                fragment = new BarFragment();
                break;
        }
           return  fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TABTITLE[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}