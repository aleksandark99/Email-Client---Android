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
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EmailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView ;
    private Toolbar toolbar;
    DrawerLayout emailsDrawerLayour;
    NavigationView navEmail;
    MenuItem search;
    SearchView searchView;
    EmailsAdapter emailsAdapter;

    ArrayList<Message> messages;
    Message m1;
    Message m2;
    Message m3;
    Tag t1 ;
    Tag t2 ;
    Tag t3 ;
    ArrayList<Tag> tags1 ;
    ArrayList<Tag> tags2 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);
        recyclerView = findViewById(R.id.emailsRecyclerView);
        toolbar = findViewById(R.id.customEmailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navEmail=findViewById(R.id.navViewEmails);

        emailsDrawerLayour = findViewById(R.id.emailsDrawerLayout);

        navEmail.bringToFront();

        ActionBarDrawerToggle tg= new ActionBarDrawerToggle(this,emailsDrawerLayour,toolbar,R.string.open_drawer,R.string.close_drawer);
        emailsDrawerLayour.addDrawerListener(tg);
        tg.syncState();
        //
        navEmail.setNavigationItemSelectedListener(this);



        View headerName = navEmail.getHeaderView(0);

        TextView name = headerName.findViewById(R.id.name);

        name.setText("LoggedUser Name");
        //

        //
        messages=new ArrayList<Message>();
         m1=new Message();
         m2=new Message();
         m3= new Message();
         t1 = new Tag(1,"t1");
         t2 = new Tag(2,"t2");
         t3 = new Tag(3,"t3");
         tags1 = new ArrayList<Tag>();
         tags2 = new ArrayList<Tag>();
        /////
    m1.setFrom("m1From");
    m2.setFrom("m2From");
    m3.setFrom("m2From");
    m1.setSubject("m1Subject");
    m2.setSubject("m2Subject");
    m3.setSubject("m3Subject");
    m1.setUnread(true);
    m2.setUnread(false);
    m3.setUnread(true);

    tags1.add(t1);
    tags1.add(t3);
    tags2.add(t1);
    tags2.add(t2);
    tags2.add(t3);
    m1.setTags(tags1);
    m2.setTags(tags2);
    m3.setTags(tags2);
    m1.setContent("Content za m1");
    m2.setContent("Content za m2");
    m3.setContent("Content za m3");

    messages.add(m1);
    messages.add(m2);
    messages.add(m3);
///ADAPTER

        //Ovo je samo za sad dok ne odradimo back za dobavljanje poruka pa cemo
        // ovo messages dobijati vadjenjem iz intenta a pri pozivu samog activitija stavljati u intent tu listu
         emailsAdapter = new EmailsAdapter(this,messages);

        // EmailsAdapter emailsAdapter = new EmailsAdapter(this,messages);
        recyclerView.setAdapter(emailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.emails_menu_toolbar,menu);
        search=menu.findItem(R.id.searchIconId);
        searchView= (SearchView) search.getActionView();
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

        switch (itemId){

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
