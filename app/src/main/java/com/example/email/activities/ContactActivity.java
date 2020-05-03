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

        //mContacts = Repository.get(this).getContacts();



        mViewPager = (ViewPager) findViewById(R.id.activity_contact_pager_view_pager);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                fetchAllContacts();
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

    private void fetchAllContacts(){
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
                Log.i("Kod", "liste, trebao bi da vrati");
                ArrayList<Contact> cnts = (ArrayList<Contact>) response.body();
               //mContactsAdapter.setData(cnts);

                mContacts = cnts;
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.i("ERROOOOOR", t.toString());
            }
        });


    }


}
