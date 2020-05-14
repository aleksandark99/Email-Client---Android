package com.example.email.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.email.model.Contact;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Contact> mContacts;

    public PagerAdapter(FragmentManager fm, ArrayList<Contact> contacts){
        //BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContacts = contacts;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Contact contact = mContacts.get(position);

        return ContactFragment.newInstance(contact);
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    public void setData(ArrayList<Contact> contacts){
        this.mContacts = contacts;
    }
}
