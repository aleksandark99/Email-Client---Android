package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.Tag;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.tags.TagsService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TagsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navTags;

    private ChipGroup tagsChipGroup;
    private Button add, cancel;
    private EditText chipText;
    private ArrayList<Tag> tags, ttt;
    View forDelete;
    private final Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    private final TagsService tagsService = retrofit.create(TagsService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        Context ctx = getApplicationContext();
//        tags = Repository.get(this).getMyTags();
        ttt = new ArrayList<>(Repository.loggedUser.getTags());
        tags = new ArrayList<Tag>();
        for (Tag t : ttt) {
            if (t.isActive()) {
                tags.add(t);
            }
        }

        toolbar = findViewById(R.id.customEmailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//Removes Title from toolbar
        getSupportActionBar().setTitle("Inbox");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navTags = findViewById(R.id.navViewTags);
        drawerLayout = findViewById(R.id.tagsDrawerLayout);
        navTags.bringToFront();

        ActionBarDrawerToggle tg = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(tg);
        tg.syncState();

        navTags.setNavigationItemSelectedListener(this);
        View headerName = navTags.getHeaderView(0);
        TextView name = headerName.findViewById(R.id.name);
        name.setText("LoggedUser Name");
        //


        tagsChipGroup = findViewById(R.id.tagsChipGroup);
        add = findViewById(R.id.addTagButton);
        cancel = findViewById(R.id.btnCancelTagChange);
        chipText = findViewById(R.id.tagField);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipText.setText("");
                add.setText("Add new tag");
                forDelete = null;
                cancel.setVisibility(View.GONE);
            }
        });
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        /// ovo uraditi tek ako prodje na bekendu
                        Chip c = (Chip) forDelete;
                        String idd = Integer.toString(c.getId());
                        Toast.makeText(ctx, idd + "sadsasdasad", Toast.LENGTH_SHORT).show();

                        Call<ResponseBody> call = tagsService.deleteTag(forDelete.getId(), Repository.jwt);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    tagsChipGroup.removeView(forDelete);
                                    tags.removeIf(tag -> (tag.getTagName().equals(c.getText().toString())));
                                    Repository.get(ctx).removeTag(c.getText().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                        // takodje removati iz iz repository liste
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(TagsActivity.this, "Delete canceled", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chipText.getText().toString().equals("") && add.getText().toString().equals("Add new tag")) {
                    Chip chip = new Chip(tagsChipGroup.getContext());
                    chip.setText(chipText.getText().toString());
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);
                    int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(color));

                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forDelete = v;
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        }
                    });

                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancel.setVisibility(View.VISIBLE);

                            chipText.setText(chip.getText().toString());
                            add.setText("CONFIRM CHANGE");
                            forDelete = v;

                        }
                    });
                    Tag t = new Tag();
                    t.setTagName(chip.getText().toString());
                    Call<Tag> call = tagsService.addNewTag(t, Repository.loggedUser.getId(), Repository.jwt);
                    call.enqueue(new Callback<Tag>() {
                        @Override
                        public void onResponse(Call<Tag> call, Response<Tag> response) {

                            Tag rt = response.body();

                            t.setId(rt.getId());
                            chip.setId(rt.getId());

                            Repository.loggedUser.getTags().add(t);
                            tagsChipGroup.addView(chip);
                        }

                        @Override
                        public void onFailure(Call<Tag> call, Throwable t) {

                        }
                    });


                    chipText.setText("");

                } else {
                    if (!chipText.getText().toString().equals("") && add.getText().toString().equals("CONFIRM CHANGE")) {
                        Toast.makeText(TagsActivity.this, "Change", Toast.LENGTH_SHORT).show();
                        Chip c = (Chip) forDelete;

                        Repository.get(ctx).editTag(c.getText().toString(), chipText.getText().toString());

                        c.setText(chipText.getText().toString());

                        chipText.setText("");
                        add.setText("Add new tag");
                        cancel.setVisibility(View.GONE);


                    } else {
                        Toast.makeText(TagsActivity.this, Integer.toString(tags.size()), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // dodavanje tagova iz repoa
        if (tags.size() > 0) {
            for (Tag t : tags) {
                Chip chip = new Chip(tagsChipGroup.getContext());
                chip.setText(t.getTagName());
                chip.setCloseIconResource(R.drawable.exit_icon);
                chip.setCloseIconVisible(true);
                int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                chip.setChipBackgroundColor(ColorStateList.valueOf(color));
                chip.setId(t.getId());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forDelete = v;
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });

                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel.setVisibility(View.VISIBLE);

                        chipText.setText(chip.getText().toString());
                        add.setText("CONFIRM CHANGE");
                        forDelete = v;

                    }
                });
                tagsChipGroup.addView(chip);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.contacts_item:

                startActivity(new Intent(this, ContactsActivity.class));

                break;

            case R.id.settings_item:

                startActivity(new Intent(this, SettingsActivity.class));

                break;

            case R.id.folders_item:

                startActivity(new Intent(this, FoldersActivity.class));
                break;

            case R.id.profile_item:

                startActivity(new Intent(this, ProfileActivity.class));

                break;
            case R.id.tags_item:
                startActivity(new Intent(this, TagsActivity.class));
                break;
            case R.id.messages_item:
                startActivity(new Intent(this, EmailsActivity.class));
                break;
        }

        return true;

    }
}
