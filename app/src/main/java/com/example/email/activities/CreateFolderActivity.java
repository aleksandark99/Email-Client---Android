package com.example.email.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.email.R;
import com.example.email.model.Folder;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.folders.FolderService;
import com.example.email.utility.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateFolderActivity extends AppCompatActivity {

    private Button btnCancel, btnSave;
    private ImageView imgClose;
    private EditText mFolderName;
    private Retrofit retrofit;
    private FolderService folderService;
    private Folder newFolder = new Folder();
    private Folder parentFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);

        if(getIntent().hasExtra("parent_folder")) {

            parentFolder = (Folder) getIntent().getSerializableExtra("parent_folder");

        }else{

            parentFolder = null;
        }

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnCreateFolder);
        imgClose = findViewById(R.id.closeAddFolder);

        mFolderName = findViewById(R.id.txtFolderName);

        retrofit = RetrofitClient.getRetrofitInstance();
        folderService = retrofit.create(FolderService.class);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String folderName = mFolderName.getText().toString().trim();

                int acc_id = (Helper.getActiveAccountId() != 0) ? Helper.getActiveAccountId() : 0;

                if(!folderName.isEmpty() && folderName != null){

                    if(folderName.equalsIgnoreCase("Sent") || folderName.equalsIgnoreCase("Drafts")
                    || folderName.equalsIgnoreCase("Trash") || folderName.equalsIgnoreCase("Favorites")){

                        showAlertDialog();
                        mFolderName.setText("");
                    }

                    newFolder.setName(folderName);
                    newFolder.setParent_folder(parentFolder);

                    Call<Folder> call = folderService.createFolder(newFolder, acc_id, Repository.jwt);

                    call.enqueue(new Callback<Folder>() {

                        @Override
                        public void onResponse(Call<Folder> call, Response<Folder> response) {

                            if (!response.isSuccessful()) {

                                Log.i("ERROR: ", String.valueOf(response.code()));

                                return;

                            }

                            newFolder = response.body();

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("newFolder", newFolder);

                            setResult(RESULT_OK, resultIntent);
                            finish();

                        }

                        @Override
                        public void onFailure(Call<Folder> call, Throwable t) {

                            Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
                        }
                    });

                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void showAlertDialog(){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("This folder name already exist!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

}
