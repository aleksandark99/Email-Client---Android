package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.email.R;
import com.example.email.fragments.ContactFragment;
import com.example.email.model.Contact;
import com.example.email.repository.Repository;

import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private static final String CONTACT_KEY_ID = "ftn.sit.email.activities.contact_id";

    private ViewPager mViewPager;
    private List<Contact> mContacts;

    public static Intent newIntent(Context packageContext, int contact_id){
        Intent intent = new Intent(packageContext, ContactActivity.class);
        intent.putExtra(CONTACT_KEY_ID, contact_id);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        int contactId = getIntent().getIntExtra(CONTACT_KEY_ID, -1);

        mContacts = Repository.get(this).getContacts();

        mViewPager = (ViewPager) findViewById(R.id.activity_contact_pager_view_pager);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Contact contact = mContacts.get(position);

                return ContactFragment.newInstance(contact.getId());
            }

            @Override
            public int getCount() {
                return mContacts.size();
            }
        });

        mContacts.stream()
                .filter(contact -> contact.getId() == contactId)
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
