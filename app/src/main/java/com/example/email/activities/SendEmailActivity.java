package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.AttachmentAdapter;
import com.example.email.model.Account;
import com.example.email.model.Attachment;
import com.example.email.model.Message;
import com.example.email.model.Tag;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.message.MessageService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

    private final Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
    private final MessageService mMessageService = mRetrofit.create(MessageService.class);

    private Uri URI;
    private String dataString;

    private RecyclerView recyclerView;
    private AttachmentAdapter attachmentAdapter;

    private Attachment a;

    private ArrayList<Attachment> attachments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachments=new ArrayList<Attachment>();
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getData();


        setData();

        recyclerView=findViewById(R.id.recycler_view_attachmentsSend);
        attachmentAdapter=new AttachmentAdapter(attachments,this);
        recyclerView.setAdapter(attachmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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
            if (toArrayString!=null && toArrayString.size()>0) {
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
            if(fromMessageString!=null&& !fromMessageString.equals("")){
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
                openFolder();

                return true;

            case R.id.cancelEmail:
                Toast.makeText(this, "cancelEmail", Toast.LENGTH_SHORT).show();
                Log.d("DDD", String.valueOf(attachmentAdapter.getAttachments().size()));
                return true;

            case R.id.sendEmailConfirm:
                //Toast.makeText(this, "sendEmailConfirm", Toast.LENGTH_SHORT).show();
                if (accountIsSelected()){
                    if (!isToAddressEmpty()){
                        Message newMessage = createMessageFromData();
                        sendMessage(newMessage);

                    } else Toast.makeText(this, "Please add at least one email address to which you want to send new email!", Toast.LENGTH_SHORT).show();

                }else Toast.makeText(this, "Please select email from which you want to send new email!", Toast.LENGTH_SHORT).show();
                return true;

                    


        }
        //za "submnu za tagove on add tags se otvara lista sa tagovima a ovde se na osnovu id-a za svaku stavku dodaje cip
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
                break;

            }

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.send_email_menu_toolbar, menu);
        for (Tag t: Repository.loggedUser.getTags()  ) {
            if(t.isActive()) {
                MenuItem menuItem = menu.findItem(R.id.addTagId).getSubMenu().add(Menu.NONE, t.getId(), Menu.NONE, t.getTagName());
            }
            //themeMenu.add(Menu.NONE, t.getId(), Menu.NONE, t.getTagName());
        }


        return true;
    }

    private Message createMessageFromData(){

        Message newMessage = new Message("This constructor is only for test, this string doesn't do anything useful...");

        Account account=Repository.activeAccount;

        newMessage.setAccount(account);

        String from = ((Chip) chipGroupFrom.getChildAt(0)).getText().toString();  newMessage.setFrom(from);

        ArrayList<String> toAddress = extractArrayListFromChipGroup(chipGroupTo); newMessage.setTo(toAddress);

        if (chipGroupCC.getChildCount() > 0) { ArrayList<String> ccAddress = extractArrayListFromChipGroup(chipGroupCC); newMessage.setCc(ccAddress);}

        if (chipGroupBCC.getChildCount() > 0) { ArrayList<String> bccAddress = extractArrayListFromChipGroup(chipGroupBCC); newMessage.setBcc(bccAddress);}

        if (chipGroupTags.getChildCount() > 0) { ArrayList<Tag> tags = extractTagsFromChipGroup();  newMessage.setTags(tags);}

        String textSubject = subject.getText().toString(); newMessage.setSubject(textSubject);

        String textContent = content.getText().toString(); newMessage.setContent(textContent);

        newMessage.setDate(LocalDateTime.now()); newMessage.setUnread(false);

        newMessage.setAttachments(attachmentAdapter.getAttachments());
        
        return newMessage;
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


    private void sendMessage(Message newMessage){
        Call<Boolean> call = mMessageService.sendNewMessage(Repository.loggedUser.getId(),newMessage, Repository.findIdOfSendingAccoount(((Chip) chipGroupFrom.getChildAt(0)).getText().toString()), Repository.jwt);

        call.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (!response.isSuccessful()){
                    Log.i("ERROR KOD Slanja nove poruke POGLEDAJ KONZOLU", String.valueOf(response.code()));
                    return;
                }
                if (response.code() == 200){
                    boolean resultOfSendingNewMessage = response.body();
                    if (resultOfSendingNewMessage) {
                        Toast.makeText(getApplicationContext(), "Message successfully sent!", Toast.LENGTH_LONG).show();
                        SendEmailActivity.this.finish();
                    } else Toast.makeText(getApplicationContext(), "Error on server, please check all receiving addresses!", Toast.LENGTH_LONG).show();

                }else Toast.makeText(getApplicationContext(), "Error on server, please check all receiving addresses!", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROOOOOR PRILIKOM Slanja nove poruke POGLEDAJ KONZOLU", Toast.LENGTH_SHORT).show();
                Log.i("ERROOOOOR PRILIKOM Slanja nove poruke POGLEDAJ KONZOLU", t.toString());
            }
        });
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

    public void openFolder() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 10);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                             Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 10
                && resultCode == RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            byte[]  uriData=null;

            if (resultData != null) {
                uri = resultData.getData();
                // Perform operations on the document using its URI.
                try {
                    InputStream iStream =   getContentResolver().openInputStream(uri);
                    byte[] inputData = getBytes(iStream);
                    Log.d("DDDD", Arrays.toString(Base64.getEncoder().encode(inputData)));

                    Log.d("DDDD", String.valueOf(uri));
                    ContentResolver cR = this.getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(uri));
                    Log.d("DDDD", type);
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    Log.d("DDDD", cursor.getString(nameIndex));
                    String base64Encoded = Base64.getEncoder().encodeToString(inputData);
                    Log.d("DDDD", base64Encoded);



                    Attachment attachment=new Attachment();
                     String encoded = Base64.getEncoder().encodeToString(inputData);

                     System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ");
                     System.out.println(inputData.toString());
                    attachment.setData(encoded);

//                     attachment.setData(inputData);
                     attachment.setMime_type(type);
                     attachment.setName( cursor.getString(nameIndex));
                     a=attachment;
                     attachments.add(attachment);
                     attachmentAdapter.notifyDataSetChanged();
//                     attachmentAdapter.setData(attachments);

                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("USAO","BBBBBBBBBBBBBBBBBBBB");


        try {
            Log.d("USAO","1");

            Log.d("USAO","2");

            Message mesForDraft=createDraftMessage();
            Log.d("USAO","3");

            Toast.makeText(this, mesForDraft.getContent(), Toast.LENGTH_SHORT).show();
                Call<ResponseBody> call = mMessageService.moveMessageToDraft(Repository.loggedUser.getAccounts().iterator().next().getId(),mesForDraft,Repository.jwt);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("AAAAAAAAAAAAAA","BBBBBBBBBBBBBBBBBBBB");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("ccc","FAILLL");

                    }
                });

            }catch (Exception e){

            }




    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    private Message createDraftMessage(){
        Message newMessage = new Message("This constructor is only for test, this string doesn't do anything useful...");

        try{
            Account account=Repository.activeAccount;

            newMessage.setAccount(account);

        }catch (Exception e){

        }

        try{        String textSubject = subject.getText().toString(); newMessage.setSubject(textSubject);


        }catch (Exception e){
            String textSubject="";
        }

        try{        ArrayList<String> toAddress = extractArrayListFromChipGroup(chipGroupTo); newMessage.setTo(toAddress);


        }catch (Exception e){

        }

        try{        String from = ((Chip) chipGroupFrom.getChildAt(0)).getText().toString();  newMessage.setFrom(from);

        }catch (Exception e){
            String from="";

        }


        if (chipGroupCC.getChildCount() > 0) { ArrayList<String> ccAddress = extractArrayListFromChipGroup(chipGroupCC); newMessage.setCc(ccAddress);}

        if (chipGroupBCC.getChildCount() > 0) { ArrayList<String> bccAddress = extractArrayListFromChipGroup(chipGroupBCC); newMessage.setBcc(bccAddress);}

        if (chipGroupTags.getChildCount() > 0) { ArrayList<Tag> tags = extractTagsFromChipGroup();  newMessage.setTags(tags);}


        try{
            String textContent = content.getText().toString(); newMessage.setContent(textContent);

        }catch (Exception e){
            String textContent="";

        }
        newMessage.setDate(LocalDateTime.now());

//        try{
//            newMessage.setDate(LocalDateTime.now());
//
//
//
//        }catch (Exception e){
//
//        }
        newMessage.setUnread(false);
        try{

        }catch (Exception e){
            newMessage.setAttachments(attachmentAdapter.getAttachments());

        }

        return newMessage;
    }








}





