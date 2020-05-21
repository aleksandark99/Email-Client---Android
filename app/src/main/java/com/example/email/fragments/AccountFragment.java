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
    private EditText txtSmtpServerAddress,txtSmtpPort, txtInServerAddress, txtInServerPort ,  txtEmail, txtDisplayName, txtPassword;
    private CheckBox btnImap, btnPop3, btnYes, btnNo;

    public AccountFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = getArguments().getParcelable(ACCOUNT_KEY);



    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_account, container, false);
        rootView.setBackgroundColor(Color.WHITE);

        btnClose = rootView.findViewById(R.id.btnCloseAddAccount);
        btnSaveAccount = rootView.findViewById(R.id.btnAddNewAccount);
        btnCancel = rootView.findViewById(R.id.btnCancelAccount);

        txtSmtpServerAddress = rootView.findViewById(R.id.smtpServerAddress);
        txtSmtpPort = rootView.findViewById(R.id.smtpPort);
        btnImap = rootView.findViewById(R.id.imap);
        btnPop3 = rootView.findViewById(R.id.pop3);
        txtInServerAddress = rootView.findViewById(R.id.inServerAddress);
        txtInServerPort = rootView.findViewById(R.id.inServerPort);
        txtEmail = rootView.findViewById(R.id.email);
        txtDisplayName = rootView.findViewById(R.id.display_name);
        txtPassword = rootView.findViewById(R.id.password);
        btnYes = rootView.findViewById(R.id.btnYes);
        btnNo = rootView.findViewById(R.id.btnNo);

        return  rootView;
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.imap:
                if(checked) return;
                btnPop3.setChecked(false);
                break;
            case R.id.pop3:
                if(checked) return;
                btnImap.setChecked(false);
                break;
                // Cheese me
            case R.id.btnYes:
                if(checked) return;
                btnNo.setChecked(false);
                break;
            case R.id.btnNo:
                if(checked) return;
                btnYes.setChecked(false);
                break;
            default: return;
        }
    }


    public static AccountFragment newInstance(Account account) {
        Bundle args = new Bundle();
        args.putParcelable(ACCOUNT_KEY, account);
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
