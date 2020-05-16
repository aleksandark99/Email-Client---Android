package com.example.email.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.ContactNavigationAdapter;
import com.example.email.adapters.ContactsAdapter;
import com.example.email.model.Contact;
import com.example.email.model.items.ContactNavItem;
import com.example.email.repository.Repository;
import com.example.email.retrofit.contacts.ContactService;
import com.example.email.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;



    private RecyclerView mRecyclerView;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<ContactNavItem> mNavItems = new ArrayList<ContactNavItem>();


    private ContactsAdapter mContactsAdapter;

    private ArrayList<Contact> mContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacts);

        prepareDrawerItems();
        drawerList = (ListView) findViewById(R.id.drawerList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerList.setAdapter(new ContactNavigationAdapter(this, mNavItems));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
        }

       drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer) {
            //Called when a drawer has settled in a completely closed state
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
            //Called when a drawer has settled in a completely open state.
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);

        mRecyclerView = (RecyclerView) findViewById(R.id.contacts_recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mContactsAdapter = new ContactsAdapter(this);

        fetchAllContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                Intent createNewContactIntent = new Intent(this, CreateContactActivity.class);
                startActivityForResult(createNewContactIntent, REQUEST_CODE);
                return false;

            default: return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            fetchAllContacts();
        } else if (resultCode == RESULT_CANCELED) {
            return;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void prepareDrawerItems(){
        //Item for Create Email Activity
        mNavItems.add(new ContactNavItem(getString(R.string.inbox),getString(R.string.inbox_description), R.drawable.ic_inbox_black_24dp));
        mNavItems.add(new ContactNavItem(getString(R.string.search_contacts), getString(R.string.search_contacts_description),  R.drawable.ic_contacts_search));
        mNavItems.add(new ContactNavItem(getString(R.string.folder), getString(R.string.folder_description),  R.drawable.ic_folder_black_24dp));
        mNavItems.add(new ContactNavItem(getString(R.string.settings), getString(R.string.settings_description),  R.drawable.ic_settings_applications_black_24dp));
    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchAllContacts();
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


    private class DrawerItemClickListener implements ListView.OnItemClickListener {


        public DrawerItemClickListener(){

        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Code to run when an item in the navigation drawer gets clicked
            String message = "";
            switch (position){
                case 1:
                    message = "Open inbox...";
                    break;
                case 2:
                    message = "Search contacts...";
                    break;
                case 3:
                    message = "Open folders...";
                    break;
                case 4:
                    message = "Open settings...";
                    break;

            }
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        }
    };

    private void fetchAllContacts(){


        Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
        ContactService mContactService = mRetrofit.create(ContactService.class);

        Call<List<Contact>> call = mContactService.getAllContactsForUser(Repository.loggedUser.getId(), Repository.jwt);

        call.enqueue(new Callback<List<Contact>>() {

            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {

                if (!response.isSuccessful()){
                    Log.i("ERROR", String.valueOf(response.code()));
                    return;
                }

                ArrayList<Contact> cnts = (ArrayList<Contact>) response.body();
                mContactsAdapter.setData(cnts);

                mRecyclerView.setAdapter(mContactsAdapter);
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.i("ERROOOOOR", t.toString());
            }
        });
    }


}
