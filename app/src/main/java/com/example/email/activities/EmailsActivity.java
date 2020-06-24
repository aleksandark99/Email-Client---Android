package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.EmailsAdapter;
import com.example.email.model.Contact;
import com.example.email.model.Message;
import com.example.email.model.interfaces.RecyclerClickListener;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.message.MessageService;
import com.example.email.utility.Helper;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerClickListener {
    RecyclerView recyclerView;
    private Toolbar toolbar;
    DrawerLayout emailsDrawerLayour;
    NavigationView navEmail;
    MenuItem search, sort;
    ArrayList<Message> messages;

    SearchView searchView;
    EmailsAdapter emailsAdapter;
    int hack;

    ArrayList<Message> mms;

    Retrofit retrofit;
    MessageService messageService;
    ProgressBar progressBar;

    @Override
    protected void onResume() {
        super.onResume();
        if(Helper.getActiveAccountId()!=0 && hack==0){
            getAllMessagesForAccount(Helper.getActiveAccountId(),hack);
            hack=1;

        }else if (Helper.getActiveAccountId()!=0 ) {
            getAllMessagesForAccount(Helper.getActiveAccountId(),hack);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);
        recyclerView = findViewById(R.id.emailsRecyclerView);
        toolbar = findViewById(R.id.customEmailsToolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);//Removes Title from toolbar
        getSupportActionBar().setTitle("Inbox");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navEmail = findViewById(R.id.navViewEmails);
        emailsDrawerLayour = findViewById(R.id.emailsDrawerLayout);
        navEmail.bringToFront();

        ActionBarDrawerToggle tg = new ActionBarDrawerToggle(this, emailsDrawerLayour, toolbar, R.string.open_drawer, R.string.close_drawer);
        emailsDrawerLayour.addDrawerListener(tg);
        tg.syncState();

        navEmail.setNavigationItemSelectedListener(this);
        View headerName = navEmail.getHeaderView(0);

        TextView name = headerName.findViewById(R.id.name);
        //name.setText(Repository.loggedUser.getUsername());

//        TextView name = headerName.findViewById(R.id.name);
//        name.setText(Repository.loggedUser.getUsername());




        //Repository.get(this).setMessages(getAllMessagesForAccount(1));
        //messages = Repository.get(this).getMessages();
//        emailsAdapter = new EmailsAdapter(this, messages);
//        recyclerView.setAdapter(emailsAdapter);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration); // ove dve linije samo za dekoraciju nista vise
        emailsAdapter = new EmailsAdapter(this,this);
//        if(Helper.getActiveAccountId()!=0){
//            getAllMessagesForAccount(Helper.getActiveAccountId());
//
//        }
        hack=0;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(-1)) {
              //      Toast.makeText(EmailsActivity.this, "First", Toast.LENGTH_LONG).show();
                    getAllMessagesForAccount(Helper.getActiveAccountId(),0);
                }
            }
        });


           retrofit = RetrofitClient.getRetrofitInstance();
            messageService = retrofit.create(MessageService.class);

            //mms=new ArrayList<>();
            progressBar=findViewById(R.id.progressBar);
     //   progressBar.setVisibility(View.GONE);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByDate:
                Toast.makeText(EmailsActivity.this, "SortByDate", Toast.LENGTH_SHORT).show();
                messages.sort(Comparator.comparing(Message::getDateReceived));
                emailsAdapter.setData(messages);

                emailsAdapter.notifyDataSetChanged();

                return true;
            case R.id.sortBySender:
                Toast.makeText(EmailsActivity.this, "sortBySender", Toast.LENGTH_SHORT).show();
                messages.sort(Comparator.comparing(Message::getFrom));
                emailsAdapter.setData(messages);

                emailsAdapter.notifyDataSetChanged();

                return true;

            case R.id.sortBySubject:
                Toast.makeText(EmailsActivity.this, "sortBySubject", Toast.LENGTH_SHORT).show();
                messages.sort(Comparator.comparing(Message::getSubject));
                emailsAdapter.setData(messages);
                emailsAdapter.notifyDataSetChanged();

                return true;
            case R.id.sendNewMailId:
                Toast.makeText(EmailsActivity.this, "sasadasdsda", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(this, SendEmailActivity.class));
                return true;

        }


        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.emails_menu_toolbar, menu);
        search = menu.findItem(R.id.searchIconId);
        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                emailsAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return true;
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, EmailsActivity.class);
        return i;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.contacts_item:

                startActivity(new Intent(EmailsActivity.this, ContactsActivity.class));
                emailsDrawerLayour.closeDrawer(Gravity.LEFT);

                break;

            case R.id.settings_item:

                startActivity(new Intent(EmailsActivity.this, SettingsActivity.class));
                emailsDrawerLayour.closeDrawer(Gravity.LEFT);

                break;

            case R.id.folders_item:

                startActivity(new Intent(EmailsActivity.this, FoldersActivity.class));
                emailsDrawerLayour.closeDrawer(Gravity.LEFT);

                break;

            case R.id.profile_item:

                startActivity(new Intent(this, ProfileActivity.class));
                emailsDrawerLayour.closeDrawer(Gravity.LEFT);

                break;
            case R.id.tags_item:
                startActivity(new Intent(this, TagsActivity.class));
                emailsDrawerLayour.closeDrawer(Gravity.LEFT);

                break;

        }

        return true;

    }
    public void getAllMessagesForAccount(int id,int h){
//        Intent intent = getIntent();
//        String isFromLogin="";
//        if(intent.getStringExtra("from")!=null){
//             isFromLogin=intent.getStringExtra("from");
//        }
//        if(isFromLogin.equals("LoginActivity")){
//            Toast.makeText(this, "From Login", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, "NOTTTT Login", Toast.LENGTH_SHORT).show();
//
//        }
      //  progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();

        ArrayList<Message> messages=new ArrayList<Message>();
        Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
        MessageService messageService=mRetrofit.create(MessageService.class);
        Call<Set<Message>> call;
        if(h==0){
            call=messageService.getAllMessages(id, Repository.jwt);

        }else{
            call=messageService.getAllMessagesFromBack(id, Repository.jwt);
            Toast.makeText(this, "Poovi drugu metodu", Toast.LENGTH_SHORT).show();
        }

        call.enqueue(new Callback<Set<Message>>() {
            @Override
            public void onResponse(Call<Set<Message>> call, Response<Set<Message>> response) {
                if (response.code() == 200){

                    mms=new ArrayList<>((Set<Message>) response.body());
                    emailsAdapter.setData(mms);
                    recyclerView.setAdapter(emailsAdapter);
//                    setMessages(new ArrayList<>((Set<Message>) response.body()));
                    setMessages(mms);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Set<Message>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Eror PRILIKOM preuzimanja poruka POGLEDAJ KONZOLU", Toast.LENGTH_SHORT).show();
                Log.d("SSS",t.getMessage());

            }
        });

//        return messages;
//        progressBar.setVisibility(View.GONE);

    }
    private void setMessages(ArrayList<Message> m){
        this.messages=m;
    }

    @Override
    public void OnItemClick(View view, int position) {
        System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
        mms.get(position).setUnread(false);
        Call<Boolean> call=messageService.makeMessageRead(mms.get(position), Repository.jwt);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                    }
                });
        Intent intent = new Intent(this, EmailActivity.class);
        intent.putExtra("message", mms.get(position));//Message.class je seriazable
        startActivity(intent);
    }

    @Override
    public void OnLongItemClick(View view, int position) {

    }

    @Override
    public void onDeleteClick(View view, int position) {

    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Repository.activeAccount=null;
//    }
}
