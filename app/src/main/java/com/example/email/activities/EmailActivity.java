package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.AttachmentAdapter;
import com.example.email.adapters.EmailAttachmentAdapter;
import com.example.email.model.Attachment;
import com.example.email.model.Message;
import com.example.email.model.Tag;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class EmailActivity extends AppCompatActivity {

    TextView subjectText,fromText,toText,dateText,contentText,toLongText,fromLongText,ccLongText;

    String subjectString,fromString,dateString,contentString,fromLongString,To;

    ArrayList<String> toLongString,ccLongString;
    Message mes;
    ArrayList<Tag> tags;
    LinearLayout detailToTexts,toLongLinearLayout,ccLongLinearLayout;
    Button replyAll,reply,forward;
    ImageView arrowIcon;
    ArrayList<Attachment> attachments;

    private RecyclerView recyclerView;
    private EmailAttachmentAdapter attachmentAdapter;

    ChipGroup chipGroup;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        subjectText = findViewById(R.id.emailSubjectText);
        fromText = findViewById(R.id.emailFromText);
        toText = findViewById(R.id.emailToText);
        dateText = findViewById(R.id.emailDateText);
        contentText = findViewById(R.id.emailContentText);
        toLongText = findViewById(R.id.EmailToLong);
        fromLongText = findViewById(R.id.EmailFromLong);
        ccLongText = findViewById(R.id.EmailCcLong);
        detailToTexts = findViewById(R.id.detailToTexts);
        arrowIcon = findViewById(R.id.arrowPicture);
        chipGroup =findViewById(R.id.ChipGroupSingleEmail);
        toLongLinearLayout =findViewById(R.id.toLongLinearLayout);
        ccLongLinearLayout = findViewById(R.id.ccLongLinearLayout);
        getData();
        setData();

        recyclerView=findViewById(R.id.email_attachment_recycler);
        attachmentAdapter= new EmailAttachmentAdapter(attachments,this);
        recyclerView.setAdapter(attachmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        reply=findViewById(R.id.replyButton);
        replyAll=findViewById(R.id.replyAllButton);
        forward=findViewById(R.id.forwardButton);


        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(EmailActivity.this,SendEmailActivity.class);

                mes.setTo(null);
                mes.setCc(null);
                intent.putExtra("message",mes);
//                intent.putExtra("from", fromString);
//                intent.putExtra("subject",subjectString);
//                intent.putExtra("content",contentString);
//                intent.putExtra("tags",tags);
                startActivity(intent);

            }
        });
        replyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(EmailActivity.this,SendEmailActivity.class);
                intent.putExtra("message",mes);
//                intent.putExtra("To",toLongString);
//                intent.putExtra("CC",ccLongString);
//
//                intent.putExtra("subject",subjectString);
//                intent.putExtra("content",contentString);
//                intent.putExtra("tags",tags);
                startActivity(intent);
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(EmailActivity.this,SendEmailActivity.class);
                mes.setFrom("");
                mes.setTo(null);
                mes.setCc(null);
                intent.putExtra("message",mes);

                //                intent.putExtra("subject",subjectString);
//                intent.putExtra("content",contentString);
//                intent.putExtra("tags",tags);
                startActivity(intent);

            }
        });


        toText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailToTexts.getVisibility()==View.VISIBLE){
                    detailToTexts.setVisibility(View.GONE);
                    arrowIcon.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                }else{
                    detailToTexts.setVisibility(View.VISIBLE);
                    arrowIcon.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                }

            }
        });
        arrowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailToTexts.getVisibility()==View.VISIBLE){
                    detailToTexts.setVisibility(View.GONE);
                    arrowIcon.setImageResource(R.drawable.arrowdown);
                }else{
                    detailToTexts.setVisibility(View.VISIBLE);
                    arrowIcon.setImageResource(R.drawable.arrowup);
                }
            }
        });

        toolbar = findViewById(R.id.customEmailsToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//Removes Title from toolbar
       // getSupportActionBar().setTitle("Inbox");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void getData(){
        if(getIntent().hasExtra("message")){
            Message m = (Message)getIntent().getSerializableExtra("message");
            mes=m;
            subjectString=m.getSubject();
            tags = m.getTags();
            fromString=m.getFrom();
            toLongString=m.getTo();
            ccLongString=m.getCc();
            //dateString=
            contentString=m.getContent();
            To=m.getTo().get(0);
            attachments=m.getAttachments();

        }else{
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }
    }
    private void setData(){
        subjectText.setText(subjectString);
        fromText.setText(fromString);
        contentText.setText(contentString);
        if (toLongString.size()>0){
            toText.setText(toLongString.get(0));

            for (String s:toLongString) {
                TextView t = new TextView(this);
                t.setText(s);
                toLongLinearLayout.addView(t);
            }
        }
        if (ccLongString.size()>0){

            for (String s:ccLongString) {
                TextView t = new TextView(this);
                t.setText(s);
                ccLongLinearLayout.addView(t);
            }
        }

        if(tags.size()>0){// ovde verovatno treba tags.size()>0 al me mrzi da proverim
            for (Tag tag:tags) {
                Chip chip = new Chip(chipGroup.getContext());
                chip.setText(tag.getTagName());
                int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                chip.setChipBackgroundColor(ColorStateList.valueOf(color));
//            Integer img = R.drawable.ic_lens_black_24dp;
                //  chip.setChipIconResource(img);
                chipGroup.addView(chip);
            }
        }

        }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.email_menu_toolbar, menu);


        return true;
    }
}
