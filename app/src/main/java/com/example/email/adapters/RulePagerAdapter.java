package com.example.email.adapters;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.email.activities.CreateRulesActivity;
import com.example.email.fragments.DeleteFragment;
import com.example.email.fragments.MoveFragment;
import com.example.email.model.Rule;

import java.util.ArrayList;

public class RulePagerAdapter extends FragmentPagerAdapter {

    private int tabsNum;
    private int folder_id;

    public RulePagerAdapter(@NonNull FragmentManager fm, int  behavior, int tabs) {
        super(fm, behavior);

        this.tabsNum = tabs;
    }

    public void setFolderId(int id){
        folder_id = id;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:

                Fragment fragment = new MoveFragment();
                Bundle args = new Bundle();
                args.putInt("folderId", folder_id);
                fragment.setArguments(args);
                return fragment;

            case 1:

                Fragment deleteFragment = new DeleteFragment();
                Bundle deleteArgs = new Bundle();
                deleteArgs.putInt("folderId", folder_id);
                deleteFragment.setArguments(deleteArgs);
                return deleteFragment;

            default:

                return null;
        }
    }

    @Override
    public int getCount() {

        return tabsNum;
    }
}
