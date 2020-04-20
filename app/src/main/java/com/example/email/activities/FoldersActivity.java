package com.example.email.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.email.R;
import com.example.email.adapters.FoldersAdapter;
import com.google.android.material.navigation.NavigationView;

public class FoldersActivity extends AppCompatActivity {

    String[] foldersName;

    int[] messageCount;

    int[] images = {R.drawable.ic_folder_purple};

    DrawerLayout drawerLayout;

    //NavigationView navigationView;

    RecyclerView recyclerView;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);


        foldersName = getResources().getStringArray(R.array.folders_name);
        messageCount = getResources().getIntArray(R.array.message_count);

        drawerLayout = findViewById(R.id.foldersDrawerLayout);

        //navigationView = findViewById(R.id.navView);

        recyclerView = findViewById(R.id.recViewFolders);

        toolbar = findViewById(R.id.foldersToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Folders");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        FoldersAdapter foldersAdapter = new FoldersAdapter(this, foldersName, messageCount, images);
        recyclerView.setAdapter(foldersAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}
