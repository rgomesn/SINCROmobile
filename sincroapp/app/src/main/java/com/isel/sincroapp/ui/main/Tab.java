package com.isel.sincroapp.ui.main;

import androidx.fragment.app.Fragment;

public class Tab {
    private String title;
    private Fragment fragment;

    public Tab(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
