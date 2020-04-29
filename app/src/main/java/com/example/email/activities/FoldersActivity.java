package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.email.MainActivity;
import com.example.email.R;
import com.example.email.adapters.FoldersAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class FoldersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FoldersAdapter.OnNoteListener {

    String[] foldersName;

    int[] messageCount;

    int[] images = {R.drawable.ic_folder_purple};

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    RecyclerView recyclerView;

    Toolbar toolbar;

    FloatingActionButton btnAddFolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        /*  String resources */

        foldersName = getResources().getStringArray(R.array.folders_name);
        messageCount = getResources().getIntArray(R.array.message_count);

        /* Hooks */

        drawerLayout = findViewById(R.id.foldersDrawerLayout);

        navigationView = findViewById(R.id.navView);

        recyclerView = findViewById(R.id.recViewFolders);

        toolbar = findViewById(R.id.foldersToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Folders");

        btnAddFolder = findViewById(R.id.btnAddFolder);

        /* Navigation Drawer menu */

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        /* Setting up NavDrawer's header name */

        View headerName = navigationView.getHeaderView(0);

        TextView name = headerName.findViewById(R.id.name);

        name.setText("LoggedUser Name");


        /*  Adapters for RecycleView */

        FoldersAdapter foldersAdapter = new FoldersAdapter(this, foldersName, messageCount, images, this::onNoteClick);
        recyclerView.setAdapter(foldersAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddFolder.setOnClickListener(v -> {

            Intent intent = new Intent(FoldersActivity.this, CreateFolderActivity.class);
            startActivity(intent);

        });
    }

    @Override
    public void onBackPressed() {

        //This avoid closing the app on back button pressed
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        }
        else {

            super.onBackPressed();
        }

    }



    /* Make an intents for each item clicked in NavDrawer */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId){

            case R.id.messages_item:

                startActivity(new Intent(FoldersActivity.this, EmailsActivity.class));

                break;

            case R.id.contacts_item:

                startActivity(new Intent(FoldersActivity.this, ContactsActivity.class));

                break;

            case R.id.settings_item:

                startActivity(new Intent(FoldersActivity.this, SettingsActivity.class));

                break;
        }

        return true;

    }

    /* Later adjust this method, now it is react on each item click */
    @Override
    public void onNoteClick(int position) {

        Intent intent = new Intent(this, FolderActivity.class);

        startActivity(intent);
    }
}
