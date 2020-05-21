package com.example.email.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
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


public class AddAccountFragment extends DialogFragment {

    private ImageView btnClose;
    private Button btnSaveAccount, btnCancel;
    private EditText txtSmtpServerAddress,txtSmtpPort, txtInServerAddress, txtInServerPort ,  txtEmail, txtDisplayName, txtPassword;
    private CheckBox btnImap, btnPop3, btnYes, btnNo;

    private String smtpServerAddress, inServerAddress, email, username, password, displayname;
    private int smtpPort, inServerType, inServerPort ;
    private boolean authentication, Imap, Pop3, authNotChoosen;


    public AddAccountFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            String smtpServerAdr = savedInstanceState.getString("smtpAddress");
            int smtpPrt = savedInstanceState.getInt("smtpPort");
            int inServerTyp = savedInstanceState.getInt("inServerType");
            String inServerAdr = savedInstanceState.getString("inServerAddress");
            int inServerPrt = savedInstanceState.getInt("inServerPort");
            String eml = savedInstanceState.getString("email");
            String dsplNm = savedInstanceState.getString("displayName");
            String pwd = savedInstanceState.getString("password");
            boolean auth = savedInstanceState.getBoolean("authentication");
            int notChoAuth = savedInstanceState.getInt("authenticationNotChoose");

            smtpServerAddress = smtpServerAdr;
            smtpPort = smtpPrt;

            if (inServerTyp == 1){
                Imap = true; Pop3 = false;
            } else if (inServerTyp == 0){
                Imap = false;
                Pop3 = true;
            } else  Imap = false; Pop3 = false;

            inServerAddress = inServerAdr;

            inServerPort = inServerPrt;
            email = eml;
            displayname = dsplNm;
            password = pwd;

            if (notChoAuth == -1){
                authNotChoosen = true;
            } else {
                authentication = auth;
            }

        } else {
            smtpServerAddress = "";
            inServerAddress = "";
            email = "";
            displayname = "";
            password = "";
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.add_account, container, false);

        rootView.setBackgroundColor(Color.WHITE);

        btnClose = rootView.findViewById(R.id.btnCloseAddAccount);
        btnSaveAccount = rootView.findViewById(R.id.btnAddNewAccount);
        btnCancel = rootView.findViewById(R.id.btnCancelAccount);

        txtSmtpServerAddress = rootView.findViewById(R.id.smtpServerAddress); txtSmtpServerAddress.setText(smtpServerAddress);

        txtSmtpPort = rootView.findViewById(R.id.smtpPort);
        txtSmtpPort.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (smtpPort != 0){
            txtSmtpPort.setText(String.valueOf(smtpPort));
        }

        btnImap = rootView.findViewById(R.id.imap); btnImap.setChecked(Imap);
        btnPop3 = rootView.findViewById(R.id.pop3); btnPop3.setChecked(Pop3);

        txtInServerAddress = rootView.findViewById(R.id.inServerAddress); txtInServerAddress.setText(inServerAddress);
        txtInServerPort = rootView.findViewById(R.id.inServerPort);
        txtInServerPort.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (inServerPort != 0){
            txtInServerPort.setText(String.valueOf(inServerPort));
        }

        txtEmail = rootView.findViewById(R.id.email); txtEmail.setText(email);
        txtDisplayName = rootView.findViewById(R.id.display_name); txtEmail.setText(displayname);
        txtPassword = rootView.findViewById(R.id.password); txtPassword.setText(password);

        btnYes = rootView.findViewById(R.id.btnYes);
        btnNo = rootView.findViewById(R.id.btnNo);

        if (authNotChoosen){

        } else if (authentication){
            btnYes.setChecked(true);
        } else {
            btnNo.setChecked(true);
        }




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

        btnImap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) btnPop3.setChecked(false);
            }
        });

        btnPop3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) btnImap.setChecked(false);
            }
        });

        btnYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)btnNo.setChecked(false);
            }
        });

        btnNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) btnYes.setChecked(false);
            }
        });

        return rootView;
    }



    private void closeFragment(Fragment fragment){

        requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("smtpAddress", txtSmtpServerAddress.getText().toString());

        try {
            outState.putInt("smtpPort", Integer.valueOf(txtSmtpPort.getText().toString()));
        } catch (Exception e){
            outState.putInt("smtpPort",0);
        }


        if (btnImap.isChecked()) outState.putInt("inServerType", 1);
        else if (btnPop3.isChecked()) outState.putInt("inServerType", 0);
        else  if (!btnImap.isChecked() && !btnPop3.isChecked()) outState.putInt("inServerType", -1);

        outState.putString("inServerAddress", txtInServerAddress.getText().toString());

        try{
            outState.putInt("inServerPort", Integer.valueOf(txtInServerPort.getText().toString()));
        }catch (Exception e){
            outState.putInt("inServerPort", 0);
        }




        outState.putString("email", txtEmail.getText().toString());
        outState.putString("displayname", txtDisplayName.getText().toString());
        outState.putString("password", txtPassword.getText().toString());

        if (btnYes.isChecked()) outState.putBoolean("authentication", true);
        else if (btnNo.isChecked()) outState.putBoolean("authentication", false);
        else  if (!btnNo.isChecked() && !btnYes.isChecked()) outState.putInt("authenticationNotChoose", -1);

    }
}
