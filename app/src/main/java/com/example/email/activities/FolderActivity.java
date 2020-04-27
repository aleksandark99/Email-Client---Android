package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.email.R;
import com.example.email.adapters.FolderAdapter;
import com.example.email.model.Folder;
import com.example.email.model.Message;
import com.example.email.model.Rule;
import com.example.email.model.items.Tag;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {

    int[] images = {R.drawable.ic_folder_purple};

    Toolbar toolbar;

    RecyclerView recyclerView;


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


        FolderAdapter folderAdapter = new FolderAdapter(this, images, childFolders, folderMessages);

        recyclerView.setAdapter(folderAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
