package com.example.email.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

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

public class EditFolderFragment extends AppCompatDialogFragment {

    private static final int EDIT_FOLDER_NAME = 3;

    private EditText txtFolderName;
    private Folder folderToChange;
    private Retrofit retrofit;
    private FolderService folderService;

    public EditFolderNameDialogListener interfaceCommunicator;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_folder_layout, null);

        builder.setView(view)
                .setTitle("Change folder name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String changedFolderName = txtFolderName.getText().toString().trim();

                        int acc_id = (Helper.getActiveAccountId() != 0) ? Helper.getActiveAccountId() : 0;

                        if(!changedFolderName.isEmpty() && folderToChange != null){

                            if(changedFolderName.equalsIgnoreCase("Sent") || changedFolderName.equalsIgnoreCase("Drafts")
                                    || changedFolderName.equalsIgnoreCase("Trash") || changedFolderName.equalsIgnoreCase("Favorites")){

                                showAlertDialog();
                                txtFolderName.setText(folderToChange.getName());
                            }

                            folderToChange.setName(changedFolderName);

                            Call<Folder> call = folderService.updateFolder(folderToChange, acc_id, Repository.jwt);

                            call.enqueue(new Callback<Folder>() {

                                @Override
                                public void onResponse(Call<Folder> call, Response<Folder> response) {

                                    if (!response.isSuccessful()) {

                                        Log.i("ERROR: ", String.valueOf(response.code()));

                                        return;

                                    }

                                    //I must notify FolderActivity and then FoldersActivity about changes I have made

                                    Folder updateFolder = response.body();

                                    if(updateFolder == null){
                                        Toast.makeText(getActivity(), "Choose different folder name!", Toast.LENGTH_SHORT).show();

                                    }else{
                                        interfaceCommunicator.onFinishedEditDialog(EDIT_FOLDER_NAME,  updateFolder.getName());
                                        dismiss();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Folder> call, Throwable t) {

                                    Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
                                }
                            });
                        }
                    }
                });

        txtFolderName = view.findViewById(R.id.editFolderName);

        retrofit = RetrofitClient.getRetrofitInstance();
        folderService = retrofit.create(FolderService.class);

        folderToChange = (Folder) getArguments().getSerializable("folderToChange");

        txtFolderName.setText(folderToChange.getName());

        return builder.create();
    }

    private void showAlertDialog(){

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            interfaceCommunicator = (EditFolderNameDialogListener) context;

        }catch(ClassCastException ex){

            throw new ClassCastException(context.toString() + " must implement EditFolderNameDialogListener!");
        }
    }

    public interface EditFolderNameDialogListener {
        void onFinishedEditDialog(int code, String name);
    }
}
