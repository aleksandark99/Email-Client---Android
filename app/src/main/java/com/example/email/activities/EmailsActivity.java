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
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.EmailsAdapter;
import com.example.email.model.Message;
import com.example.email.repository.Repository;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Comparator;

public class EmailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    private Toolbar toolbar;
    DrawerLayout emailsDrawerLayour;
    NavigationView navEmail;
    MenuItem search, sort;
    ArrayList<Message> messages;

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
        //name.setText(Repository.loggedUser.getUsername());

//        TextView name = headerName.findViewById(R.id.name);
//        name.setText(Repository.loggedUser.getUsername());



///ADAPTER

        messages = Repository.get(this).getMessages();
        emailsAdapter = new EmailsAdapter(this, messages);
        recyclerView.setAdapter(emailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration); // ove dve linije samo za dekoraciju nista vise

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByDate:
                Toast.makeText(EmailsActivity.this, "SortByDate", Toast.LENGTH_SHORT).show();
                messages.sort(Comparator.comparing(Message::getDateReceived));
                emailsAdapter.notifyDataSetChanged();

                return true;
            case R.id.sortBySender:
                Toast.makeText(EmailsActivity.this, "sortBySender", Toast.LENGTH_SHORT).show();
                messages.sort(Comparator.comparing(Message::getFrom));
                emailsAdapter.notifyDataSetChanged();

                return true;

            case R.id.sortBySubject:
                Toast.makeText(EmailsActivity.this, "sortBySubject", Toast.LENGTH_SHORT).show();
                messages.sort(Comparator.comparing(Message::getSubject));
                emailsAdapter.notifyDataSetChanged();

                return true;
            case R.id.sendNewMailId:
                Toast.makeText(EmailsActivity.this, "sasadasdsda", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(this, SendEmailActivity.class));
                return true;

        }


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

            case R.id.profile_item:

                startActivity(new Intent(this, ProfileActivity.class));

                break;
            case R.id.tags_item:
                startActivity(new Intent(this, TagsActivity.class));
                break;

        }

        return true;

    }
}
