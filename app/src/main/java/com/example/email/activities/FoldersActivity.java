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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.email.R;
import com.example.email.adapters.FoldersAdapter;
import com.example.email.model.Folder;
import com.example.email.model.interfaces.RecyclerClickListener;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.folders.FolderService;
import com.example.email.utility.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FoldersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerClickListener {

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    RecyclerView recyclerView;

    FoldersAdapter foldersAdapter;

    Toolbar toolbar;

    FloatingActionButton btnAddFolder;

    ArrayList<Folder> folders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foldersAdapter = new FoldersAdapter(this, this);

        fillFoldersData();

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

            case R.id.profile_item:

                startActivity(new Intent(this, ProfileActivity.class));

                break;
        }

        return true;

    }

    /* Later adjust this method, now it is react on each item click */

    @Override
    public void OnItemClick(View view, int position) {

        Intent intent = new Intent(this, FolderActivity.class);

        intent.putExtra("folder", folders.get(position));

        System.out.println("INTENNNNNNNNNNNNNNNNNNNNNNNNNNNNNNT " + folders.get(position).getName());

        startActivity(intent);

    }

    @Override
    public void OnLongItemClick(View view, int position) {

        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }


    private void fillFoldersData(){

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();

        FolderService folderService = retrofit.create(FolderService.class);

        int acc_id = (Helper.getActiveAccountId() != 0) ? Helper.getActiveAccountId() : 0;

        Call<Set<Folder>> call = folderService.getFoldersByAccount(acc_id, Repository.jwt);

        call.enqueue(new Callback<Set<Folder>>() {

            @Override
            public void onResponse(Call<Set<Folder>> call, Response<Set<Folder>> response) {

                if (!response.isSuccessful()) {

                    Log.i("ERROR: ", String.valueOf(response.code()));

                    return;

                }
                    folders = new ArrayList<>(response.body());

                    foldersAdapter.setData(folders);

                    recyclerView.setAdapter(foldersAdapter);
                }

            @Override
            public void onFailure(Call<Set<Folder>> call, Throwable t) {

                Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
            }
        });



    }



}
