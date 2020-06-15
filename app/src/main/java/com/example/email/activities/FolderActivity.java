package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.adapters.FolderAdapter;
import com.example.email.fragments.EditFolderFragment;
import com.example.email.model.Folder;
import com.example.email.model.Message;
import com.example.email.model.Rule;
import com.example.email.model.interfaces.RecyclerClickListener;
import com.example.email.model.Tag;
import com.example.email.repository.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity implements RecyclerClickListener, EditFolderFragment.EditFolderNameDialogListener {

    private static final int ADD_SUBFOLDER = 3;
    private static final int EDIT_SUBFOLDER = 4;

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private FloatingActionButton btnAddSubFolder;

    private ActionMode mActionMode;

    private FolderAdapter folderAdapter;

    private Folder mFolder, previewFolder;

    private ArrayList<Folder> childFolders;

    private ArrayList<Message> folderMessages;

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

        hideBtnAddFolder(folderName);

        getSupportActionBar().setTitle(folderName);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        childFolders = new ArrayList<>(mFolder.getChildFolders());

        folderMessages = new ArrayList<>(mFolder.getMessages());

        folderAdapter = new FolderAdapter(this, childFolders, folderMessages, this);

        recyclerView.setAdapter(folderAdapter);

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


        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if(mActionMode != null){

                    return false;
                }

                mActionMode = startSupportActionMode(mActionModeCallback);

                Menu actionMenu = mActionMode.getMenu();

                if(folderAdapter.getItemViewType(position) == FolderAdapter.TYPE_FOLDER){

                    actionMenu.findItem(R.id.action_mode_move).setVisible(false);
                    actionMenu.findItem(R.id.action_mode_copy).setVisible(false);

                }else if(folderAdapter.getItemViewType(position) == FolderAdapter.TYPE_EMAIL){

                    actionMenu.findItem(R.id.action_mode_add_rule).setVisible(false);
                }

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

            switch(item.getItemId()){

                case R.id.action_mode_copy:

                    Toast.makeText(FolderActivity.this, "Copy option selected", Toast.LENGTH_SHORT).show();

                    mode.finish();

                    return true;

                case R.id.action_mode_move:

                    Toast.makeText(FolderActivity.this, "Move option selected", Toast.LENGTH_SHORT).show();

                    mode.finish();

                    return true;

                case R.id.action_mode_del:

                    Toast.makeText(FolderActivity.this, "Delete option selected", Toast.LENGTH_SHORT).show();

                    mode.finish();

                    return true;

                case R.id.action_mode_add_rule:

                    Intent intent = new Intent(FolderActivity.this, CreateRulesActivity.class);

                    startActivity(intent);

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

            case R.id.delete_folder_item:

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
        editFragment.setArguments(args);
        editFragment.show(getSupportFragmentManager(), "edit folder");
    }

    @Override
    public void onFinishedEditDialog(int code, String name) {
        if(code == 3){
            mFolder.setName(name);
            getSupportActionBar().setTitle(name);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.putExtra("changedFolder", mFolder);
        setResult(RESULT_OK, intent);
    }
}
