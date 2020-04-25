package com.example.email.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.ContactNavigationAdapter;
import com.example.email.adapters.ContactsAdapter;
import com.example.email.model.Contact;
import com.example.email.model.items.ContactNavItem;
import com.example.email.repository.Repository;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final String NEW_CONTACT_KEY = "com.example.email.contacts_activity.NEW_CONTACT_KEY";

    private RecyclerView mRecyclerView;
    private DrawerLayout drawerLayout;
    private RelativeLayout drawerPane;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<ContactNavItem> mNavItems = new ArrayList<ContactNavItem>();
    private Repository mRepository = Repository.get(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacts);

        prepareDrawerItems();


        drawerList = (ListView) findViewById(R.id.drawerList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerPane = (RelativeLayout) findViewById(R.id.drawerPane);

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

       drawerList.setOnItemClickListener(new DrawerItemClickListener(this));

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
        ContactsAdapter adapter = new ContactsAdapter(mRepository.getContacts(),this);
        mRecyclerView.setAdapter(adapter);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
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
                Toast.makeText(this, "Creats new contact", Toast.LENGTH_SHORT).show();
                Intent createNewContactIntent = CreateContactActivity.newIntent(this, mRepository.newId());
                startActivityForResult(createNewContactIntent, REQUEST_CODE);
                return false;

            default: return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){

            //boolean photoTaken = data.getBooleanExtra("photoTaken", false);
            mRecyclerView.setAdapter( new ContactsAdapter(mRepository.getContacts(),this));

        } else if (resultCode == RESULT_CANCELED) {
            Log.i("Tag", "vratio");
            return;
        }

        //mRecyclerView.setAdapter( new ContactsAdapter(mRepository.getContacts(),this));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
// Sync the toggle state after onRestoreInstanceState has occurred.
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
        //3 more items...
    }




    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mRecyclerView.setAdapter(new ContactsAdapter(mRepository.getContacts(),this));

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

        private Context c;

        public DrawerItemClickListener(Context c){
            this.c = c;
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
}
