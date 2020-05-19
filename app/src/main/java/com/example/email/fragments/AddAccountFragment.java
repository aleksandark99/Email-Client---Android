package com.example.email.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.email.R;

import java.util.Objects;

public class AddAccountFragment extends DialogFragment {

    private ImageView btnClose;
    private Button btnSaveAccount, btnCancel;
    private EditText txtEmail, txtServerAddress, txtPort, txtPassword, txtDisplayName;
    private CheckBox btnYes, btnNo;



    public AddAccountFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.add_account, container, false);

        rootView.setBackgroundColor(Color.WHITE);

        btnClose = rootView.findViewById(R.id.btnCloseAddAccount);
        btnSaveAccount = rootView.findViewById(R.id.btnAddNewAccount);
        btnCancel = rootView.findViewById(R.id.btnCancelAccount);
        txtServerAddress = rootView.findViewById(R.id.text_serverAddress);
        txtEmail = rootView.findViewById(R.id.text_email);
        txtPort = rootView.findViewById(R.id.text_port);
        txtPassword = rootView.findViewById(R.id.text_password);
        txtDisplayName = rootView.findViewById(R.id.text_displayName);
        btnYes = rootView.findViewById(R.id.btnYes);
        btnNo = rootView.findViewById(R.id.btnNo);


        //Close the fragment

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                closeFragment(AddAccountFragment.this);

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeFragment(AddAccountFragment.this);

            }
        });


        btnSaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if request status is ok, then close the fragment and set email in #selectedEmail EditText

            }
        });


        btnYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    btnNo.setChecked(false);
                }
            }
        });

        btnNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    btnYes.setChecked(false);
                }
            }
        });


        return rootView;
    }

    private void closeFragment(Fragment fragment){

        requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();

    }

}
