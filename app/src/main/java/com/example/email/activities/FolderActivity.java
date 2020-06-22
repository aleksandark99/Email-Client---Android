package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.FolderAdapter;
import com.example.email.adapters.FoldersAdapter;
import com.example.email.fragments.CheckFolderFragment;
import com.example.email.fragments.EditFolderFragment;
import com.example.email.model.Folder;
import com.example.email.model.Message;
import com.example.email.model.interfaces.RecyclerClickListener;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.folders.FolderService;
import com.example.email.retrofit.message.MessageService;
import com.example.email.utility.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FolderActivity extends AppCompatActivity implements RecyclerClickListener, EditFolderFragment.EditFolderNameDialogListener {

    private static final int ADD_SUBFOLDER = 3;
    private static final int EDIT_SUBFOLDER = 4;
    private static final int EDIT_OK = 3;

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private FloatingActionButton btnAddSubFolder;

    private ActionMode mActionMode;

    private FolderAdapter folderAdapter;

    private Folder mFolder, previewFolder;

    private ArrayList<Folder> childFolders;

    private ArrayList<Message> folderMessages;


    private final Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
    private final FolderService folderService = mRetrofit.create(FolderService.class);
    private final MessageService messageService = mRetrofit.create(MessageService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        mFolder = (Folder) getIntent().getSerializableExtra("folder");


        /*  Hooks and Toolbar */

        toolbar = findViewById(R.id.folderToolbar);

        recyclerView = findViewById(R.id.recViewFolder);

        btnAddSubFolder = findViewById(R.id.btnAddSubFolder);

        setSupportActionBar(toolbar);

        String folderName = (!mFolder.getName().isEmpty()) ? mFolder.getName() : "";

        int folder_id = mFolder.getId();

        hideBtnAddFolder(folderName);

        getSupportActionBar().setTitle(folderName);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        folderAdapter = new FolderAdapter(this,this);
        loadChildFolders(folder_id);
        loadFolderMessages(folderName, folder_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddSubFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FolderActivity.this, CreateFolderActivity.class);
                intent.putExtra("parent_folder", mFolder);
                startActivityForResult(intent, ADD_SUBFOLDER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_SUBFOLDER){

            if(resultCode == RESULT_OK){

                Folder childFolder = (Folder) data.getSerializableExtra("newFolder");
                childFolders.add(childFolder);
            }
        }
        if(requestCode == EDIT_SUBFOLDER){

            Folder changedSubFolder = (Folder) data.getSerializableExtra("changedFolder");
            childFolders.remove(previewFolder);
            childFolders.add(changedSubFolder);
            folderAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void OnItemClick(View view, int position) {

        if(folderAdapter.getItemViewType(position) == FolderAdapter.TYPE_FOLDER) {

            Intent intent = new Intent(this, FolderActivity.class);

            previewFolder = childFolders.get(position);

            intent.putExtra("folder", previewFolder);

            startActivityForResult(intent, EDIT_SUBFOLDER);

        }

    }

    @Override
    public void OnLongItemClick(View view, int position) {

        if(folderAdapter.getItemViewType(position) == FolderAdapter.TYPE_EMAIL) {

            view.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    if (mActionMode != null) {

                        return false;
                    }

                    mActionMode = startSupportActionMode(mActionModeCallback);

                    return true;
                }

            });
        }

    }

    @Override
    public void onDeleteClick(View view, int position) {

        int folder_id = childFolders.get(position).getId();

        openDeleteDialog(folder_id, position);
    }

    private void removeItem(int position){
        childFolders.remove(position);
        folderAdapter.notifyItemRemoved(position);
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

            CheckFolderFragment fragment = new CheckFolderFragment();
            Bundle args = new Bundle();
            //args.putSerializable("checkedMessage", message);
            fragment.setArguments(args);


            switch(item.getItemId()){

                case R.id.action_mode_copy:

                case R.id.action_mode_move:

                    fragment.show(getSupportFragmentManager(), "check folder");

                    mode.finish();

                    return true;

                case R.id.action_mode_del:

                    Toast.makeText(FolderActivity.this, "Delete option selected", Toast.LENGTH_SHORT).show();

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

    private void hideBtnAddFolder(String folderName){

        if(folderName.equals("Sent") || folderName.equals("Drafts") ||
        folderName.equals("Trash") || folderName.equals("Favorites")){

            btnAddSubFolder.hide();

        }else{

            btnAddSubFolder.show();
        }
    }

    private void loadFolderMessages(String name, int folder_id){

        int acc_id = (Helper.getActiveAccountId() != 0) ? Helper.getActiveAccountId() : 0;

        if(name.equals("Trash")){
            loadInactiveMessages(acc_id);

        }else{
            loadFolderMessagesByRules(folder_id);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String folderName = mFolder.getName();

        if(folderName.equals("Sent") || folderName.equals("Drafts") ||
        folderName.equals("Trash") || folderName.equals("Favorites")){

            return false;

        }else{

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.folders_menu_toolbar, menu);

            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.add_rule_item:

                Intent intent = new Intent(this, CreateRulesActivity.class);
                intent.putExtra("folder_id", mFolder.getId());
                startActivity(intent);

                return true;

            case R.id.edit_folder_item:

                openEditDialog();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

    private void openEditDialog(){
        EditFolderFragment editFragment = new EditFolderFragment();
        Bundle args = new Bundle();
        args.putSerializable("folderToChange", mFolder);
        System.out.println("PRE POZIVANJA FRAGMENTA " + mFolder.isActive());
        editFragment.setArguments(args);
        editFragment.show(getSupportFragmentManager(), "edit folder");
    }

    @Override
    public void onFinishedEditDialog(int code, String name) {
        if(code == EDIT_OK){
            mFolder.setName(name);
            getSupportActionBar().setTitle(name);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadChildFolders(mFolder.getId());
        Intent intent = new Intent();
        intent.putExtra("changedFolder", mFolder);
        setResult(RESULT_OK, intent);
    }


    private void openDeleteDialog(int folder_id, int position){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure you want to delete folder?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int acc_id = (Helper.getActiveAccountId() != 0) ? Helper.getActiveAccountId() : 0;

                Call<ResponseBody> call = folderService.deleteFolder(folder_id, acc_id, Repository.jwt);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (!response.isSuccessful()) {
                            Log.i("Some error happened during folder delete!", String.valueOf(response.code()));
                            return;
                        }
                        if (response.code() == 200) {
                            removeItem(position);
                            Toast.makeText(getApplicationContext(), "You successfully delete folder!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
                    }
                });
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadChildFolders(int parent_folder_id){

        int acc_id = (Helper.getActiveAccountId() != 0) ? Helper.getActiveAccountId() : 0;

        Call<Set<Folder>> call = folderService.getSubFoldersByAccount(acc_id, parent_folder_id, Repository.jwt);

        call.enqueue(new Callback<Set<Folder>>() {
            @Override
            public void onResponse(Call<Set<Folder>> call, Response<Set<Folder>> response) {

                if (!response.isSuccessful()) {
                    Log.i("ERROR: ", String.valueOf(response.code()));
                    return;
                }

                childFolders = new ArrayList<>(response.body());
                folderAdapter.setData(childFolders);
                recyclerView.setAdapter(folderAdapter);
            }

            @Override
            public void onFailure(Call<Set<Folder>> call, Throwable t) {
                Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
            }
        });
    }

    private void loadFolderMessagesByRules(int folder_id){

        int acc_id = (Helper.getActiveAccountId() != 0) ? Helper.getActiveAccountId() : 0;

        Call<Set<Message>> call = messageService.getAllMessagesByRules(folder_id, acc_id, Repository.jwt);

        call.enqueue(new Callback<Set<Message>>() {
            @Override
            public void onResponse(Call<Set<Message>> call, Response<Set<Message>> response) {
                if (!response.isSuccessful()) {
                    Log.i("ERROR: ", String.valueOf(response.code()));
                    return;
                }

                folderMessages = new ArrayList<>(response.body());
                folderAdapter.setMessages(folderMessages);
                recyclerView.setAdapter(folderAdapter);
            }

            @Override
            public void onFailure(Call<Set<Message>> call, Throwable t) {
                Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
            }
        });
    }

    private void loadInactiveMessages(int account_id){

        Call<Set<Message>> call = messageService.getAllInactiveMessage(account_id, Repository.jwt);

        call.enqueue(new Callback<Set<Message>>() {
            @Override
            public void onResponse(Call<Set<Message>> call, Response<Set<Message>> response) {
                if (!response.isSuccessful()) {
                    Log.i("ERROR: ", String.valueOf(response.code()));
                    return;
                }

                folderMessages = new ArrayList<>(response.body());
                folderAdapter.setMessages(folderMessages);
                recyclerView.setAdapter(folderAdapter);
            }

            @Override
            public void onFailure(Call<Set<Message>> call, Throwable t) {
                Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
            }
        });
    }
}
