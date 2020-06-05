package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.Account;
import com.example.email.model.Message;
import com.example.email.model.items.Tag;
import com.example.email.repository.Repository;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.Inflater;

public class SendEmailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private  Message mes;
    private ChipGroup chipGroupTo,chipGroupCC,chipGroupBCC,chipGroupTags, chipGroupEmails, chipGroupFrom;
    private EditText toSend,ccSend,bccSend,subject,content;
    //private TextView;
    private ImageView expand, expandFrom;
    private LinearLayout cc,bcc, fromLinearLayout;

    private ArrayList<String> toArrayString,ccArayString;
    private String contentString,subjectString,fromMessageString;
    private ArrayList<Tag> tags;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        cc=findViewById(R.id.ccSendEmail);
        bcc=findViewById(R.id.bccSendEmail);
        fromLinearLayout = findViewById(R.id.fromLinearLayout);
        toSend=findViewById(R.id.toSendId111);
        toSend.requestFocus();
        //fromEmailAddressTextView = findViewById(R.id.idFromSendEmail);
        subject=findViewById(R.id.subjectTextId );
        content=findViewById(R.id.contentTextId);

        ccSend=findViewById(R.id.ccSendId);
        bccSend=findViewById(R.id.bccSendId);


        chipGroupCC=findViewById(R.id.ccChipGroup);
        chipGroupBCC=findViewById(R.id.bccChipGroup);
        chipGroupTo=findViewById(R.id.ToChipGroup);
        chipGroupEmails = findViewById(R.id.emailsChipGroup);
        chipGroupFrom = findViewById(R.id.fromChipGroup);

        chipGroupTags=findViewById(R.id.tagsCheapGroupSendEmail);
        expand=findViewById(R.id.imageView); expandFrom = findViewById(R.id.expandFrom);

        expandFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fromLinearLayout.getVisibility() == View.GONE){

                    Repository.loggedUser.getAccounts().forEach(a -> {
                        Chip chip = new Chip(chipGroupEmails.getContext());
                        chip.setText(a.getUsername());
                        chip.setOnClickListener(new MyChipListener(a.getUsername()));
                        chipGroupEmails.addView(chip);
                    });
                    fromLinearLayout.setVisibility(View.VISIBLE);
                    expandFrom.setImageResource(R.drawable.arrowup);

                } else if (fromLinearLayout.getVisibility() == View.VISIBLE){
                    chipGroupEmails.removeAllViews();
                    fromLinearLayout.setVisibility(View.GONE);
                    expandFrom.setImageResource(R.drawable.arrowdown);
                }






                //Chip chip = new Chip(parentView.getContext());

                //chipGroup.addView(chip);
            }
        });


        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cc.getVisibility()==View.VISIBLE && bcc.getVisibility()==View.VISIBLE){
                    cc.setVisibility(View.GONE);
                    bcc.setVisibility(View.GONE);
                    expand.setImageResource(R.drawable.arrowdown);
                }else{
                    cc.setVisibility(View.VISIBLE);
                    bcc.setVisibility(View.VISIBLE);
                    expand.setImageResource(R.drawable.arrowup);
                }
            }
        });

        toSend.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_UP&& !toSend.getText().toString().equals("")){
                    Chip chip = new Chip(chipGroupTo.getContext());
                    chip.setText(toSend.getText().toString());
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);

                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupTo.removeView(v);
                        }
                    });


                    chipGroupTo.addView(chip);
                    toSend.setText("");
                    return  true;
                }
                return false;
            }
        });
        toSend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !toSend.getText().toString().equals("")) {
                    Toast.makeText(SendEmailActivity.this, "SSS", Toast.LENGTH_SHORT).show();
                    Chip chip = new Chip(chipGroupTo.getContext());
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);
                    chip.setText(toSend.getText().toString());
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupTo.removeView(v);
                        }
                    });

                    chipGroupTo.addView(chip);
                    toSend.setText("");
                }
            }
        });

        ccSend.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_UP&& !ccSend.getText().toString().equals("")){
                    Chip chip = new Chip(chipGroupCC.getContext());
                    chip.setText(ccSend.getText().toString());
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);

                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupCC.removeView(v);
                        }
                    });


                    chipGroupCC.addView(chip);
                    ccSend.setText("");
                    return  true;
                }
                return false;
            }
        });
        ccSend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !ccSend.getText().toString().equals("")) {
                    Toast.makeText(SendEmailActivity.this, "SSS", Toast.LENGTH_SHORT).show();
                    Chip chip = new Chip(chipGroupCC.getContext());
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);
                    chip.setText(ccSend.getText().toString());
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupCC.removeView(v);
                        }
                    });

                    chipGroupCC.addView(chip);
                    ccSend.setText("");
                }
            }
        });

        bccSend.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_UP&& !bccSend.getText().toString().equals("")){
                    Chip chip = new Chip(chipGroupBCC.getContext());
                    chip.setText(bccSend.getText().toString());
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);

                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupBCC.removeView(v);
                        }
                    });


                    chipGroupBCC.addView(chip);
                    bccSend.setText("");
                    return  true;
                }
                return false;
            }
        });
        bccSend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !bccSend.getText().toString().equals("")) {
                    Toast.makeText(SendEmailActivity.this, "SSS", Toast.LENGTH_SHORT).show();
                    Chip chip = new Chip(chipGroupBCC.getContext());
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);
                    chip.setText(bccSend.getText().toString());
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupBCC.removeView(v);
                        }
                    });

                    chipGroupBCC.addView(chip);
                    bccSend.setText("");
                }
            }
        });



        toolbar=findViewById(R.id.customEmailsToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//Removes Title from toolbar
        // getSupportActionBar().setTitle("Inbox");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getData();


        //setData();



    }
    private void getData(){
        if(getIntent().hasExtra("message")){
            Message m = (Message)getIntent().getSerializableExtra("message");
            mes=m;
            contentString= m.getContent();
            subjectString=m.getSubject();
            tags=m.getTags();
            toArrayString=m.getTo();
            ccArayString=m.getCc();
            fromMessageString=m.getFrom();
        }
    }
    private void setData(){
        if(getIntent().hasExtra("message")) {
            subject.setText(subjectString);
            content.setText(contentString);
            if (tags.size() > 0) {
                for (Tag t : tags
                ) {
                    Chip chip = new Chip(chipGroupTags.getContext());
                    chip.setText(t.getTagName());
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);
                    int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(color));
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupTags.removeView(v);
                            //remove tag from list
                        }
                    });
                    //add tag to list of tags
                    chipGroupTags.addView(chip);
                }
            }
            if (toArrayString!=null) {
                for (String t : toArrayString
                ) {
                    Chip chip = new Chip(chipGroupTo.getContext());
                    chip.setText(t);
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);
//                int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
//                chip.setChipBackgroundColor(ColorStateList.valueOf(color));
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupTo.removeView(v);
                            //remove tag from list
                        }
                    });
                    //add tag to list of tags
                    chipGroupTo.addView(chip);

                }
            }
            if (ccArayString!=null) {
                for (String t : ccArayString
                ) {
                    Chip chip = new Chip(chipGroupCC.getContext());
                    chip.setText(t);
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);
//                int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
//                chip.setChipBackgroundColor(ColorStateList.valueOf(color));
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroupCC.removeView(v);
                            //remove tag from list
                        }
                    });
                    //add tag to list of tags
                    chipGroupCC.addView(chip);

                }
            }
            if(fromMessageString!=null){
                Chip chip = new Chip(chipGroupTo.getContext());
                chip.setText(fromMessageString);
                chip.setCloseIconResource(R.drawable.exit_icon);
                chip.setCloseIconVisible(true);

                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroupTo.removeView(v);
                        //remove tag from list
                    }
                });
                //add tag to list of tags
                chipGroupTo.addView(chip);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTagId:
                Toast.makeText(this, "AddTag", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.addAttachment:
                Toast.makeText(this, "addAttachment", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.cancelEmail:
                Toast.makeText(this, "cancelEmail", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.sendEmailConfirm:
                //Toast.makeText(this, "sendEmailConfirm", Toast.LENGTH_SHORT).show();
                if (accountIsSelected()){
                    if (!isToAddressEmpty()){
                        Message newMessage = createMessageFromData();
                        //sendMessage(newMessage);

                    } else Toast.makeText(this, "Please add at least one email address to which you want to send new email!", Toast.LENGTH_SHORT).show();

                }else Toast.makeText(this, "Please select email from which you want to send new email!", Toast.LENGTH_SHORT).show();
                return true;

                    


        }
        for (Tag t:Repository.loggedUser.getTags()
             ) {
            if(t.getId()==item.getItemId()){
                Toast.makeText(this, t.getTagName(), Toast.LENGTH_SHORT).show();

                Chip chip = new Chip(chipGroupTags.getContext());
                chip.setId(t.getId());
                chip.setText(t.getTagName());
                chip.setCloseIconResource(R.drawable.exit_icon);
                chip.setCloseIconVisible(true);
                int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                chip.setChipBackgroundColor(ColorStateList.valueOf(color));
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroupTags.removeView(v);
                        //remove tag from list
                    }
                });
                //add tag to list of tags
                chipGroupTags.addView(chip);

            }

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.send_email_menu_toolbar, menu);

 //       menu.addSubMenu(Menu.NONE, R.id.addTagId, Menu.NONE,"asdasdsad");


//        SubMenu themeMenu = menu.findItem(R.id.addTagId).getSubMenu();
//        themeMenu.clear();


        for (Tag t: Repository.loggedUser.getTags()  ) {

            MenuItem menuItem = menu.findItem(R.id.addTagId).getSubMenu().add(Menu.NONE, t.getId(), Menu.NONE, t.getTagName());

            //themeMenu.add(Menu.NONE, t.getId(), Menu.NONE, t.getTagName());
        }


//        themeMenu.add(0, 1, Menu.NONE, "Automatic");
//        themeMenu.add("SSSSS");
//        themeMenu.add("DDDD");

//        themeMenu.add(0, R.id.theme_auto, Menu.NONE, "Automatic");
//        themeMenu.add(0, R.id.theme_day, Menu.NONE, "Default");
//        themeMenu.add(0, , Menu.NONE, "Night");
//        themeMenu.add(0, , Menu.NONE, "Battery Saving");

        return true;
    }

    private Message createMessageFromData(){

        Message newMessage = new Message("This constructor is only for test, this string doesn't do anything...");

        String from = ((Chip) chipGroupFrom.getChildAt(0)).getText().toString();  newMessage.setFrom(from);

        ArrayList<String> toAddress = extractArrayListFromChipGroup(chipGroupTo); newMessage.setTo(toAddress);

        if (chipGroupCC.getChildCount() > 0) { ArrayList<String> ccAddress = extractArrayListFromChipGroup(chipGroupCC); newMessage.setCc(ccAddress);}

        if (chipGroupBCC.getChildCount() > 0) { ArrayList<String> bccAddress = extractArrayListFromChipGroup(chipGroupBCC); newMessage.setBcc(bccAddress);}

        if (chipGroupTags.getChildCount() > 0) { ArrayList<Tag> tags = extractTagsFromChipGroup();  newMessage.setTags(tags);}

        String textSubject = subject.getText().toString(); newMessage.setSubject(textSubject);

        String textContent = content.getText().toString(); newMessage.setContent(textContent);






        Log.i("new messag", String.valueOf(newMessage));


        return null;
    }

    public ArrayList<Tag> extractTagsFromChipGroup(){
        return IntStream.rangeClosed(0, chipGroupTags.getChildCount()-1).boxed()
                .map(idChip -> ((Chip) chipGroupTags.getChildAt(idChip)).getId() )
                .map(idTag -> Repository.findTagById(idTag))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<String> extractArrayListFromChipGroup(ChipGroup chipGroup){
        return IntStream.rangeClosed(0, chipGroup.getChildCount()-1).boxed()
                .map(idChip -> ((Chip) chipGroup.getChildAt(idChip)).getText().toString())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean accountIsSelected(){
        int atLeastOneAddress = chipGroupFrom.getChildCount();
        return atLeastOneAddress > 0;
    }

    private boolean isToAddressEmpty(){
        int atLeastOneAddress = chipGroupTo.getChildCount();
        return atLeastOneAddress > 0 ? false : true;
    }

    class MyChipListener implements View.OnClickListener{

        private Chip mChip;

        public MyChipListener(String email){
            this.mChip = new Chip(chipGroupFrom.getContext());
            mChip.setText(email);
            mChip.setCloseIconResource(R.drawable.exit_icon);
            mChip.setCloseIconVisible(true);

            mChip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipGroupFrom.removeView(v);

                }
            });

        }

        @Override
        public void onClick(View v) {

            chipGroupFrom.removeAllViews();
            chipGroupFrom.addView(mChip);

        }
    }


}
