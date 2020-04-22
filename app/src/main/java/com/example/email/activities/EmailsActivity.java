package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.email.R;
import com.example.email.adapters.EmailsAdapter;
import com.example.email.model.Message;
import com.example.email.model.items.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EmailsActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
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
        EmailsAdapter emailsAdapter = new EmailsAdapter(this,messages);
        recyclerView.setAdapter(emailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, EmailsActivity.class);
        return i;
    }
}
