package com.example.email.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.email.R;
import com.example.email.model.Account;

public class AccountFragment extends Fragment {

    private static final String ACCOUNT_KEY = "com.example.email.fragments.account";
    private Account mAccount;

    private ImageView btnClose;
    private Button btnSaveAccount, btnCancel;
    private EditText txtEmail, txtServerAddress, txtPort, txtPassword, txtDisplayName;
    private CheckBox btnYes, btnNo;

    public AccountFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = getArguments().getParcelable(ACCOUNT_KEY);

    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_account, container, false);


        rootView.setBackgroundColor(Color.WHITE);

        btnClose = rootView.findViewById(R.id.btnCloseAddAccount);
        btnSaveAccount = rootView.findViewById(R.id.btnAddNewAccount);
        btnCancel = rootView.findViewById(R.id.btnCancelAccount);
        txtServerAddress = rootView.findViewById(R.id.text_serverAddress);
        txtEmail = rootView.findViewById(R.id.text_email); txtServerAddress.setText(mAccount.getEmail());
        txtPort = rootView.findViewById(R.id.text_port);
        txtPassword = rootView.findViewById(R.id.text_password);
        txtDisplayName = rootView.findViewById(R.id.text_displayName);
        btnYes = rootView.findViewById(R.id.btnYes);
        btnNo = rootView.findViewById(R.id.btnNo);

        return  rootView;
    }


    public static AccountFragment newInstance(Account account) {
        Bundle args = new Bundle();
        args.putParcelable(ACCOUNT_KEY, account);
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
