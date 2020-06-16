package com.isel.sincroapp.ui.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {
    private final List<Tab> tabList = new ArrayList<>();

    TabAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        return tabList.get(position).getFragment();
    }

    public void addFragment(String title, Fragment fragment) {
        tabList.add(new Tab(title, fragment));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return tabList.size();
    }
}
