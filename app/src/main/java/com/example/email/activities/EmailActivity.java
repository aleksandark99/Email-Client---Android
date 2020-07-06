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
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.message.MessageService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    Retrofit retrofit;
    MessageService messageService;

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

        retrofit = RetrofitClient.getRetrofitInstance();
        messageService = retrofit.create(MessageService.class);


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
                startActivity(intent);

            }
        });
        replyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(EmailActivity.this,SendEmailActivity.class);
                intent.putExtra("message",mes);

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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);// set drawable icon
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
            dateString=m.getDate_time();
            dateString=dateString.substring(0,10)+" "+dateString.substring(11,16);
            contentString=m.getContent();
            try{
                To=m.getTo().get(0);

            }catch (Exception e){

            }
            attachments=m.getAttachments();

        }else{
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }
    }
    private void setData(){
        dateText.setText(dateString);
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
                chip.setCloseIconResource(R.drawable.ic_close);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pozovi na back za remove
                        Call<Boolean> call=messageService.removeTagToMessage(Repository.loggedUser.getId(),tag.getId(),mes,Repository.jwt);
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                chipGroup.removeView(v);

                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                        //remove tag from list
                    }
                });

                chipGroup.addView(chip);
            }
        }

        }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Call<Boolean> call=messageService.deleteMessage(Repository.loggedUser.getId(),mes, Repository.jwt);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toast.makeText(EmailActivity.this, "Message deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EmailActivity.this, EmailsActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                    }
                });


        }
        for (Tag t:Repository.loggedUser.getTags()
        ) {
            if(t.getId()==item.getItemId()){
                Toast.makeText(this, t.getTagName(), Toast.LENGTH_SHORT).show();

                Chip chip = new Chip(chipGroup.getContext());
                chip.setId(t.getId());
                chip.setText(t.getTagName());
                chip.setCloseIconResource(R.drawable.exit_icon);
                chip.setCloseIconVisible(true);
                int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                chip.setChipBackgroundColor(ColorStateList.valueOf(color));
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pozovi na back za remove
                        Call<Boolean> call=messageService.removeTagToMessage(Repository.loggedUser.getId(),t.getId(),mes,Repository.jwt);
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                chipGroup.removeView(v);

                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                        //remove tag from list
                    }
                });
                //add tag to list of tags
                //pozovi na back za add

                Call<Boolean> call=messageService.addTagToMessage(Repository.loggedUser.getId(),t.getId(),mes,Repository.jwt);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        chipGroup.addView(chip);

                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });

            }

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.email_menu_toolbar, menu);
        for (Tag t: Repository.loggedUser.getTags()  ) {
            MenuItem menuItem = menu.findItem(R.id.tagForMessageId).getSubMenu().add(Menu.NONE, t.getId(), Menu.NONE, t.getTagName());
        }
        return true;
    }
}
