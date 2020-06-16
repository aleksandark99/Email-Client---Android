package com.example.email.activities;

import androidx.annotation.NonNull;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.ContactNavigationAdapter;
import com.example.email.adapters.ContactsAdapter;
import com.example.email.model.Contact;
import com.example.email.model.items.ContactNavItem;
import com.example.email.repository.Repository;
import com.example.email.retrofit.contacts.ContactService;
import com.example.email.retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_CODE = 1;

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

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

        toolbar = findViewById(R.id.customEmailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = findViewById(R.id.navViewContacts);
        mDrawerLayout = findViewById(R.id.contactsDrawerLayout);
        mNavigationView.bringToFront();


        drawerToggle =  new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        View headerName = mNavigationView.getHeaderView(0);
        TextView name = headerName.findViewById(R.id.name);
        name.setText(Repository.loggedUser.getUsername());



        mRecyclerView = (RecyclerView) findViewById(R.id.contacts_recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mContactsAdapter = new ContactsAdapter(this);

        //fetchAllContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Repository.loggedUserHaveAccount()){
            Toast.makeText(this, "Please create at least one account!", Toast.LENGTH_LONG).show();
            return true;
        } else {
            switch (item.getItemId()){

                case R.id.action_new:
                    Intent createNewContactIntent = new Intent(this, CreateContactActivity.class);
                    startActivityForResult(createNewContactIntent, REQUEST_CODE);
                    return false;

                default: return true;
            }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.messages_item:
                startActivity(new Intent(ContactsActivity.this, EmailsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.settings_item:
                startActivity(new Intent(ContactsActivity.this, SettingsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.folders_item:
                startActivity(new Intent(ContactsActivity.this, FoldersActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.profile_item:
                startActivity(new Intent(this, ProfileActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tags_item:
                startActivity(new Intent(this, TagsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
        }
        return true;
    }



    private void fetchAllContacts(){


        Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
        ContactService mContactService = mRetrofit.create(ContactService.class);

        Call<Set<Contact>> call = mContactService.getAllContactsForUser(Repository.loggedUser.getId(), Repository.jwt);

        call.enqueue(new Callback<Set<Contact>>() {

            @Override
            public void onResponse(Call<Set<Contact>> call, Response<Set<Contact>> response) {

                if (!response.isSuccessful()){
                    Log.i("ERROR", String.valueOf(response.code()));
                    return;
                }

                mContactsAdapter.setData(new ArrayList<>((Set<Contact>) response.body()));

                mRecyclerView.setAdapter(mContactsAdapter);
            }

            @Override
            public void onFailure(Call<Set<Contact>> call, Throwable t) {
                Log.i("ERROOOOOR", t.toString());
            }
        });
    }


}
