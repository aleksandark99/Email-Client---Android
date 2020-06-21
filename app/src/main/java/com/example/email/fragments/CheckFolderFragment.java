package com.example.email.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.email.R;
import com.example.email.model.Folder;
import com.example.email.model.Message;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFolderFragment extends Fragment {

    private Spinner folderSpinner;
    private Button btnCancel, btnSave;

    private Message message;

    private ArrayList<Folder> folders;

    public CheckFolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_check_folder, container, false);

        //message = (Message) getArguments().getSerializable("checkedMessage");

        folderSpinner = view.findViewById(R.id.choseFolderSpinner);
        btnCancel = view.findViewById(R.id.btnCancelSpinner);
        btnSave = view.findViewById(R.id.btnSaveSpinner);

        ArrayAdapter<Folder> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner, folders);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        folderSpinner.setAdapter(adapter);


        return view;
    }
}
