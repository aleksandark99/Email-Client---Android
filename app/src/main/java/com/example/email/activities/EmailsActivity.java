package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.email.R;
import com.example.email.adapters.EmailsAdapter;
import com.example.email.model.Message;
import com.example.email.model.items.Tag;
import com.example.email.repository.Repository;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EmailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    private Toolbar toolbar;
    DrawerLayout emailsDrawerLayour;
    NavigationView navEmail;
    MenuItem search;
    SearchView searchView;
    EmailsAdapter emailsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);

        recyclerView = findViewById(R.id.emailsRecyclerView);

        toolbar = findViewById(R.id.customEmailsToolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);//Removes Title from toolbar
        getSupportActionBar().setTitle("Inbox");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navEmail = findViewById(R.id.navViewEmails);
        emailsDrawerLayour = findViewById(R.id.emailsDrawerLayout);
        navEmail.bringToFront();

        ActionBarDrawerToggle tg = new ActionBarDrawerToggle(this, emailsDrawerLayour, toolbar, R.string.open_drawer, R.string.close_drawer);
        emailsDrawerLayour.addDrawerListener(tg);
        tg.syncState();

        navEmail.setNavigationItemSelectedListener(this);
        View headerName = navEmail.getHeaderView(0);
        TextView name = headerName.findViewById(R.id.name);
        name.setText("LoggedUser Name");


///ADAPTER


        emailsAdapter = new EmailsAdapter(this, Repository.get(this).getMessages());
        recyclerView.setAdapter(emailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration); // ove dve linije samo za dekoraciju nista vise
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.emails_menu_toolbar, menu);
        search = menu.findItem(R.id.searchIconId);
        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                emailsAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return true;
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, EmailsActivity.class);
        return i;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.contacts_item:

                startActivity(new Intent(EmailsActivity.this, ContactsActivity.class));

                break;

            case R.id.settings_item:

                startActivity(new Intent(EmailsActivity.this, SettingsActivity.class));

                break;

            case R.id.folders_item:

                startActivity(new Intent(EmailsActivity.this, FoldersActivity.class));

                break;
        }

        return true;

    }
}
