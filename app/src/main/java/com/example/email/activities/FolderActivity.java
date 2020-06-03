package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.FolderAdapter;
import com.example.email.model.Folder;
import com.example.email.model.Message;
import com.example.email.model.Rule;
import com.example.email.model.interfaces.RecyclerClickListener;
import com.example.email.model.items.Tag;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity implements RecyclerClickListener {

    int[] images = {R.drawable.ic_folder_purple};

    Toolbar toolbar;

    RecyclerView recyclerView;

    ActionMode mActionMode;

    FolderAdapter folderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);


        /*  Those objects will be later removed. Their purpose are for testing FolderAdapter */


        ArrayList<Message> messages = new ArrayList<>();
        ArrayList<Message> messagesInner = new ArrayList<>();
        ArrayList<Folder> innerFolders = new ArrayList<>();
        ArrayList<Folder> testFolders = new ArrayList<>();

        Message m1 = new Message();
        Message m2 = new Message();

        Tag t1 = new Tag(1, "tag1");
        Tag t2 = new Tag(2, "tag2");

        ArrayList<Tag> tags = new ArrayList<>();

        m1.setFrom("m1From");
        m2.setFrom("m2From");

        m1.setSubject("m1Subject");
        m2.setSubject("m2Subject");

        m1.setUnread(true);
        m2.setUnread(false);

        tags.add(t1);
        tags.add(t2);

        m1.setTags(tags);
        m2.setTags(tags);

        m1.setContent("Content za m1");
        m2.setContent("Content za m23");

        messages.add(m1);
        messages.add(m2);
        messagesInner.add(m1);

        Folder f2 = new Folder(3, "Folder 3", new ArrayList<Message>(), null, new Rule());
        testFolders.add(f2);

        Folder f1 = new Folder(2, "Inner Folder", messagesInner, testFolders, new Rule());
        innerFolders.add(f1);

        Folder folder = new Folder(1, "TestFolder", messages, innerFolders, new Rule());


        /*  Hooks and Toolbar */

        toolbar = findViewById(R.id.folderToolbar);

        recyclerView = findViewById(R.id.recViewFolder);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(folder.getName());

        ArrayList<Folder> childFolders = folder.getChildFolders();

        ArrayList<Message> folderMessages = folder.getMessages();


        folderAdapter = new FolderAdapter(this, images, childFolders, folderMessages, this);


        recyclerView.setAdapter(folderAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void OnItemClick(View view, int position) {

    }

    @Override
    public void OnLongItemClick(View view, int position) {


        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if(mActionMode != null){

                    return false;
                }

                mActionMode = startSupportActionMode(mActionModeCallback);

                Menu actionMenu = mActionMode.getMenu();

                if(folderAdapter.getItemViewType(position) == FolderAdapter.TYPE_FOLDER){

                    actionMenu.findItem(R.id.action_mode_move).setVisible(false);
                    actionMenu.findItem(R.id.action_mode_copy).setVisible(false);

                }else if(folderAdapter.getItemViewType(position) == FolderAdapter.TYPE_EMAIL){

                    actionMenu.findItem(R.id.action_mode_add_rule).setVisible(false);
                }

                return true;
            }

        });

    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
            mode.setTitle("Choose the option");

            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch(item.getItemId()){

                case R.id.action_mode_copy:

                    Toast.makeText(FolderActivity.this, "Copy option selected", Toast.LENGTH_SHORT).show();

                    mode.finish();

                    return true;

                case R.id.action_mode_move:

                    Toast.makeText(FolderActivity.this, "Move option selected", Toast.LENGTH_SHORT).show();

                    mode.finish();

                    return true;

                case R.id.action_mode_del:

                    Toast.makeText(FolderActivity.this, "Delete option selected", Toast.LENGTH_SHORT).show();

                    mode.finish();

                    return true;

                case R.id.action_mode_add_rule:

                    Intent intent = new Intent(FolderActivity.this, CreateRulesActivity.class);

                    startActivity(intent);

                    mode.finish();

                    return true;

                default: return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            mActionMode = null;
        }
    };
}
