package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.email.R;
import com.example.email.fragments.ContactFragment;
import com.example.email.fragments.PagerAdapter;
import com.example.email.model.Contact;
import com.example.email.repository.Repository;
import com.example.email.retrofit.contacts.ContactService;
import com.example.email.retrofit.contacts.RetrofitContactClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactActivity extends AppCompatActivity {

    private static final String CONTACT_KEY = "ftn.sit.email.activities.contact";
    private static final String CONTACTS_LIST_KEY = "ftn.sit.email.activities.contacts_list";

    private ViewPager mViewPager;
    private List<Contact> mContacts;
    private Contact mContact;
    //private FragmentStatePagerAdapter mPagerAdapter;

    public static Intent newIntent(Context packageContext, int position, ArrayList<Contact> contacts){
        Intent intent = new Intent(packageContext, ContactActivity.class);
        intent.putExtra(CONTACT_KEY, position);
        //intent.putExtra(CONTACTS_LIST_KEY, contacts);
        intent.putParcelableArrayListExtra(CONTACTS_LIST_KEY, contacts);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //int contactId = getIntent().getIntExtra(CONTACT_KEY_ID, 0);
        int pos =   getIntent().getIntExtra(CONTACT_KEY, 0);

        mContacts = getIntent().getParcelableArrayListExtra(CONTACTS_LIST_KEY);
        mContact = mContacts.get(pos);

        if (mContacts.get(0) == null) {
            Log.i("OVO", "NE RADI NESTO");
        }else if  ( mContacts.get(0) instanceof Contact && mContacts.get(0).getId() == 1 ){
            Log.i("OVO", "RADI NESTO");
        } else {
            Log.i("e OVO", "stvarno NE RADI NESTO");
        }



/*        Log.i("velicina liste je", String.valueOf(mContacts.size()));
        Log.i("String rep", mContact.toString());
        if (mContact.getPhotoPath() == null) Log.i("OGROMAN", "BUG");
        Log.i("PUTAAAANJA u ContactActivity objekta", String.valueOf(mContact.getPhotoPath()));

        if (mContact == null) Log.i("mContact", "is NULL");
        if (mContacts == null) Log.i("mContacts", "is NULL");*/
        //mContacts = Repository.get(this).getContacts();



        mViewPager = (ViewPager) findViewById(R.id.activity_contact_pager_view_pager);
        //mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        //fetchAllContacts();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                //fetchAllContacts();
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

    /*private void fetchAllContacts(){
        //final ArrayList<Contact> contacts;
        Log.i("Dosao u", "fetchAllContacts");
        Retrofit mRetrofit = RetrofitContactClient.getRetrofitInstance();
        ContactService mContactService = mRetrofit.create(ContactService.class);

        Call<List<Contact>> call = mContactService.getAllContacts();

        call.enqueue(new Callback<List<Contact>>() {

            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {

                if (!response.isSuccessful()){
                    Log.i("ERROR", String.valueOf(response.code()));
                    return;
                }
                Log.i("IZ ContactActivity", "dobio response");
                ArrayList<Contact> cnts = (ArrayList<Contact>) response.body();
                mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), cnts);

                mViewPager.setAdapter(mPagerAdapter);


                //mContacts = cnts;
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.i("ERROOOOOR", t.toString());
            }
        });


    }*/


}
