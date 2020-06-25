package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.email.fragments.CheckFolderFragment;
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

public class EmailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerClickListener, CheckFolderFragment.MoveMessageDialogListener {
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

    SwipeRefreshLayout swipeRefreshLayout;

    private ActionMode mActionMode;
    private Message selectedMessage;
    private static final int MOVE_OK = 10;
    private static final int COPY_OK = 11;

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
        swipeRefreshLayout=findViewById(R.id.swipeerrrr);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllMessagesForAccount(Helper.getActiveAccountId(),0);

            }
        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                Log.d("POZVANNNN","POZVANNNNNNNN");
//
//                if (!recyclerView.canScrollVertically(-1)) {
//              //      Toast.makeText(EmailsActivity.this, "First", Toast.LENGTH_LONG).show();
//                    getAllMessagesForAccount(Helper.getActiveAccountId(),0);
//                }
//            }
//        });


           retrofit = RetrofitClient.getRetrofitInstance();
            messageService = retrofit.create(MessageService.class);

            //mms=new ArrayList<>();
//            progressBar=findViewById(R.id.progressBar);
     //   progressBar.setVisibility(View.GONE);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByDate:
                Toast.makeText(EmailsActivity.this, "SortByDate", Toast.LENGTH_SHORT).show();
                messages.sort(Comparator.comparing(Message::getDate_time));
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

            case R.id.logOut:

                Intent logOut = new Intent(this, LoginActivity.class);
                logOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logOut);
                this.finish();
                break;

        }

        return true;

    }
    public void getAllMessagesForAccount(int id,int h){
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.bringToFront();
                Log.d("POZVANNNN","POZVANNNNNNNN");

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
//                    progressBar.setVisibility(View.GONE);
                    System.out.println(mms+"MMMMMMMMMMMMS");
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<Set<Message>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Eror PRILIKOM preuzimanja poruka POGLEDAJ KONZOLU", Toast.LENGTH_SHORT).show();
                Log.d("SSS",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);


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

        if(mActionMode != null){
            mActionMode.finish();}

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

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (mActionMode != null) {

                    return false;
                }
                selectedMessage = messages.get(position);

                mActionMode = startSupportActionMode(mActionModeCallback);

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

            int item_id = item.getItemId();

            CheckFolderFragment fragment = new CheckFolderFragment();
            Bundle args = new Bundle();
            args.putSerializable("checkedMessage", selectedMessage);

            switch(item_id){

                case R.id.action_mode_copy:

                    args.putInt("action_mode", R.id.action_mode_copy);
                    fragment.setArguments(args);
                    fragment.show(getSupportFragmentManager(), "check folder");

                    mode.finish();
                    return true;

                case R.id.action_mode_move:

                    args.putInt("action_mode", R.id.action_mode_move);
                    fragment.setArguments(args);
                    fragment.show(getSupportFragmentManager(), "check folder");

                    mode.finish();
                    return true;

                case R.id.action_mode_del:

                    openDeleteDialogMessage(selectedMessage);
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

    private void openDeleteDialogMessage(Message message){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure you want to delete message?");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Call<Boolean> call = messageService.deleteMessage(message, Repository.jwt);

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (!response.isSuccessful()) {
                            Log.i("Some error happened during message delete!", String.valueOf(response.code()));
                            return;
                        }
                        if(response.code() == 200){
                            messages.remove(message);
                            emailsAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "You have successfully delete message!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
                    }
                });
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }



    @Override
    public void onDeleteClick(View view, int position) {

    }

    @Override
    public void onFinishedMovedMessageDialog(int code, int message_id) {

        if(code == MOVE_OK){
            moveMessage(message_id);
            Toast.makeText(getApplicationContext(), "You have successfully moved message!", Toast.LENGTH_SHORT).show();

        }else if(code == COPY_OK){
            Toast.makeText(getApplicationContext(), "You have successfully copied message!", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveMessage(int message_id){
        for(Message m : messages){
            if(m.getId() == message_id){
                messages.remove(m);
                emailsAdapter.notifyDataSetChanged();
            }
        }
    }
}
