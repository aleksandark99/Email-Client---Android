package com.example.email.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.Folder;
import com.example.email.model.Message;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.folders.FolderService;
import com.example.email.retrofit.message.MessageService;
import com.example.email.utility.Helper;

import java.util.ArrayList;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFolderFragment extends AppCompatDialogFragment {

    private Spinner folderSpinner;

    private Message message;

    private Folder folder;

    private ArrayList<Folder> folders;

    private static final int MOVE_OK = 10;
    private Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    private FolderService folderService = retrofit.create(FolderService.class);
    private MessageService messageService = retrofit.create(MessageService.class);

    private int acc_id = (Helper.getActiveAccountId() != 0) ? Helper.getActiveAccountId() : 0;

    private int mode, message_id, folder_id;

    public MoveMessageDialogListener interfaceCommunicator;

    public CheckFolderFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_check_folder, null);

        message = (Message) getArguments().getSerializable("checkedMessage");
        message_id = (int) message.getId();
        mode = getArguments().getInt("action_mode");

        loadAvailableFolders();

        builder.setView(view)
                .setTitle("Select folder")
                .setMessage("Choose the folder in which you want to move/copy message!")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mode == R.id.action_mode_move){

                            moveMessageToFolder(message_id, folder_id);

                        }else if(mode == R.id.action_mode_copy){

                        }

                    }
                });

        folderSpinner = view.findViewById(R.id.choseFolderSpinner);


        folderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                folder = (Folder) parent.getSelectedItem();
                folder_id = folder.getId();
                Toast.makeText(getActivity(), "You selected " + folder.getName() + " folder!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return builder.create();
    }


    private void loadAvailableFolders(){

        Call<Set<Folder>> call = folderService.getFoldersForChecking(acc_id, (int) message.getId(), Repository.jwt);

        call.enqueue(new Callback<Set<Folder>>() {
            @Override
            public void onResponse(Call<Set<Folder>> call, Response<Set<Folder>> response) {
                if (!response.isSuccessful()) {
                    Log.i("ERROR: ", String.valueOf(response.code()));
                    return;
                }
                folders = new ArrayList<>(response.body());
                ArrayAdapter<Folder> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner, folders);
                adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                folderSpinner.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Set<Folder>> call, Throwable t) {
                Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
            }
        });

    }


    private void moveMessageToFolder(int message_id, int folder_id){

        Call<Message> call = messageService.moveMessageToFolder(message_id, folder_id, acc_id, Repository.jwt);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (!response.isSuccessful()) {
                    Log.i("ERROR: ", String.valueOf(response.code()));
                    return;
                }

                Message message = response.body();
                if(message == null){
                    Toast.makeText(getActivity(), "The move operation is not succeed!", Toast.LENGTH_SHORT).show();

                }else{
                    interfaceCommunicator.onFinishedMovedMessageDialog(MOVE_OK, message_id);
                    dismiss();
                }
            }
            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            interfaceCommunicator = (MoveMessageDialogListener) context;

        }catch(ClassCastException ex){

            throw new ClassCastException(context.toString() + " must implement MoveMessageDialogListener!");
        }
    }

    public interface MoveMessageDialogListener{
        void onFinishedMovedMessageDialog(int code, int message_id);
    }
}
