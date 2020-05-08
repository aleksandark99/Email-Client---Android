package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.email.R;
import com.example.email.fragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    NavigationView navigationView;

    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /* Hooks */

        toolbar = findViewById(R.id.settingsToolbar);

        navigationView = findViewById(R.id.navView);

        drawerLayout = findViewById(R.id.settingsDrawerLayout);


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Settings");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Navigation Drawer menu*/

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentSettingsContainer, new SettingsFragment()).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId){


            case R.id.messages_item:

                startActivity(new Intent(SettingsActivity.this, EmailsActivity.class));

                break;

            case R.id.contacts_item:

                startActivity(new Intent(SettingsActivity.this, ContactsActivity.class));

                break;


            case R.id.folders_item:

                startActivity(new Intent(SettingsActivity.this, FoldersActivity.class));

                break;

            case R.id.profile_item:

                startActivity(new Intent(this, ProfileActivity.class));

                break;
        }

        return true;

    }
}
