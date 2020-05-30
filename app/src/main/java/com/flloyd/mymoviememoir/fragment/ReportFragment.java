package com.flloyd.mymoviememoir.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flloyd.mymoviememoir.R;
import com.flloyd.mymoviememoir.adapter.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ReportFragment extends Fragment {
        public ReportFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.report_fragment, container, false);
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter( getChildFragmentManager());
            ViewPager viewPager = view.findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = view.findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);
            return view;
        }
}
