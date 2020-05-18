package com.example.email.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.email.R;
import com.example.email.fragments.ContactFragment;
import com.example.email.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private static final String CONTACT_KEY = "ftn.sit.email.activities.contact";
    private static final String CONTACTS_LIST_KEY = "ftn.sit.email.activities.contacts_list";

    private ViewPager mViewPager;
    private List<Contact> mContacts;
    private Contact mContact;

    public static Intent newIntent(Context packageContext, int position, ArrayList<Contact> contacts){
        Intent intent = new Intent(packageContext, ContactActivity.class);
        intent.putExtra(CONTACT_KEY, position);
        intent.putParcelableArrayListExtra(CONTACTS_LIST_KEY, contacts);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        int pos =   getIntent().getIntExtra(CONTACT_KEY, 0);

        mContacts = getIntent().getParcelableArrayListExtra(CONTACTS_LIST_KEY);
        mContact = mContacts.get(pos);

        mViewPager = (ViewPager) findViewById(R.id.activity_contact_pager_view_pager);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Contact contact = mContacts.get(position);
                return ContactFragment.newInstance(contact);
            }

            @Override
            public int getCount() {
                return mContacts.size();
            }
        });

        mContacts.stream()
                .filter(contact -> contact.getId() == mContact.getId())
                .forEach(contact -> mViewPager.setCurrentItem(mContacts.indexOf(contact)));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
