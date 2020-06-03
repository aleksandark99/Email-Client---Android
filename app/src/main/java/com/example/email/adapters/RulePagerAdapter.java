package com.example.email.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.email.fragments.DeleteFragment;
import com.example.email.fragments.MoveFragment;

public class RulePagerAdapter extends FragmentPagerAdapter {

    private int tabsNum;

    public RulePagerAdapter(@NonNull FragmentManager fm, int  behavior, int tabs) {
        super(fm, behavior);

        this.tabsNum = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:

                return new MoveFragment();

            case 1:

                return new DeleteFragment();

            default:

                return null;
        }
    }

    @Override
    public int getCount() {

        return tabsNum;
    }
}
