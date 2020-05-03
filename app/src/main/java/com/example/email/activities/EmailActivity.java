package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.Message;
import com.example.email.model.items.Tag;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class EmailActivity extends AppCompatActivity {

    TextView subjectText,fromText,toText,dateText,contentText,toLongText,fromLongText,ccLongText;

    String subjectString,fromString,toString,dateString,contentString,toLongString,fromLongString,ccLongString;

    ArrayList<Tag> tags;
    LinearLayout detailToTexts;

    ImageView arrowIcon;

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
        getData();
        setData();


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
                    arrowIcon.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                }else{
                    detailToTexts.setVisibility(View.VISIBLE);
                    arrowIcon.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
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
            subjectString=m.getSubject();
            tags = m.getTags();
            fromString=m.getFrom();
            toString=m.getTo();
            //dateString=
            contentString=m.getContent();
            // toLongString
            // fromLongString
            // ccLongString;

        }else{
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }
    }
    private void setData(){
        subjectText.setText(subjectString);
        fromText.setText(fromString);
        toText.setText(toString);
        contentText.setText(contentString);

        if(tags!=null){
            for (Tag tag:tags) {
                Chip chip = new Chip(chipGroup.getContext());
                chip.setText(tag.getTagName());
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
