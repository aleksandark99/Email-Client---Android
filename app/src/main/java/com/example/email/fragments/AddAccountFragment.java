package com.example.email.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.email.R;
import com.example.email.model.Account;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.account.AccountService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AddAccountFragment extends DialogFragment {

    private ImageView btnClose;
    private Button btnSaveAccount, btnCancel;
    private EditText txtSmtpServerAddress,txtSmtpPort, txtInServerAddress, txtInServerPort ,  txtEmail, txtDisplayName, txtPassword;
    private CheckBox btnImap, btnPop3, btnYes, btnNo;

    private String smtpServerAddress, inServerAddress, email, username, password, displayname;
    private int smtpPort, inServerType, inServerPort ;
    private boolean authentication, Imap, Pop3, authNotChoosen;

    private final Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
    private final AccountService mAccountService = mRetrofit.create(AccountService.class);


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

                Account newAccount = getAccount();

                if (newAccount != null){
                    // send to backend;
                    Call<Account> call = mAccountService.addAccount(newAccount, Repository.loggedUser.getId(), Repository.jwt);

                    call.enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {

                            if (!response.isSuccessful()){
                                Toast.makeText(getActivity(), "Response not successful", Toast.LENGTH_SHORT).show();
                                Log.i("GRESKA U AddAccountFragment-u", String.valueOf(response.code()));
                            }
                            Account newAcc = response.body();
                            if (newAcc == null){
                                Toast.makeText(getActivity(), "Please choose different email address!", Toast.LENGTH_SHORT).show();
                            } else{
                                Repository.loggedUser.getAccounts().add(newAcc);
                                Toast.makeText(getActivity(), "Account saved", Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().beginTransaction().remove(AddAccountFragment.this).commit();
                            }
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {
                            Toast.makeText(getActivity(), "Please choose different email address!", Toast.LENGTH_SHORT).show();
                            Log.i("FAILURE", t.toString());
                            return;
                        }
                    });
                } else {
                    //nothing
                }
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


    private Account getAccount(){
        Account newAccount = new Account();

        if (txtSmtpServerAddress.getText().toString().length() >= 6 && txtSmtpServerAddress.getText().toString().substring(0, 5).equals("smtp.")){
            newAccount.setSmtpAddress(txtSmtpServerAddress.getText().toString());
        }else{
            Toast.makeText(getActivity(), "SMTP server address not properly formed!", Toast.LENGTH_SHORT).show();
            return null;
        }

        try{
            int x = Integer.valueOf(txtSmtpPort.getText().toString());
            if ( x <= 0){
                Toast.makeText(getActivity(), "SMTP server port must be postive integer!", Toast.LENGTH_SHORT).show();
                return null;
            }
            newAccount.setSmtpPort(Integer.valueOf(txtSmtpPort.getText().toString()));
        }catch (Exception e){
            Toast.makeText(getActivity(), "SMTP server port must be postive integer!", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (btnImap.isChecked()){
            //newAccount.setInServerType(1); //IMAP
            if (txtInServerAddress.getText().toString().length() >= 5 && txtInServerAddress.getText().toString().substring(0, 5).equals("imap.")){
                newAccount.setInServerType(1);
                newAccount.setInServerAddress(txtInServerAddress.getText().toString());
            } else {
                Toast.makeText(getActivity(), "Receiving server address not properly formed!", Toast.LENGTH_SHORT).show();
                return null;
            }

        } else if (btnPop3.isChecked()) {
            //newAccount.setInServerType(0); //POP
            if(txtInServerAddress.getText().toString().length() >= 4 && txtInServerAddress.getText().toString().substring(0,4).equals("pop.")){
                newAccount.setInServerType(0);
                newAccount.setInServerAddress(txtInServerAddress.getText().toString());
            } else {
                Toast.makeText(getActivity(), "Receiving server address not properly formed!", Toast.LENGTH_SHORT).show();
                return null;
            }

        } else {
            //nothing is checked
            Toast.makeText(getActivity(), "Please choose incoming server type!", Toast.LENGTH_SHORT).show();
            return null;
        }

        try{
            int x = Integer.valueOf(txtInServerPort.getText().toString());
            if ( x <= 0){
                Toast.makeText(getActivity(), "Receiving server port must be postive integer!", Toast.LENGTH_SHORT).show();
                return null;
            }
            newAccount.setInServerPort(Integer.valueOf(txtInServerPort.getText().toString()));
        }catch (Exception e){
            Toast.makeText(getActivity(), "Receiving server port must be postive integer!", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (isWhitespacesOnly(txtEmail.getText().toString())){
            Toast.makeText(getActivity(), "Email address cannot be whitespace!", Toast.LENGTH_SHORT).show();
            return null;
        } else newAccount.setUsername(txtEmail.getText().toString());


        if (isWhitespacesOnly(txtDisplayName.getText().toString())){
            Toast.makeText(getActivity(), "Display name cannot be whitespace!", Toast.LENGTH_SHORT).show();
            return null;
        } else newAccount.setDisplayName(txtDisplayName.getText().toString());

        if (isWhitespacesOnly(txtPassword.getText().toString())){
            Toast.makeText(getActivity(), "Password cannot be whitespace!", Toast.LENGTH_SHORT).show();
            return null;
        } else newAccount.setPassword(txtPassword.getText().toString());

        if (btnYes.isChecked()){
            newAccount.setAuthentication(true);
        } else if (btnNo.isChecked()){
            newAccount.setAuthentication(false);
        } else if (!btnNo.isChecked() && !btnYes.isChecked()) {
            Toast.makeText(getActivity(), "Please set authentication!", Toast.LENGTH_SHORT).show();
            return null;
        }

        return newAccount;
    }

    private boolean isWhitespacesOnly(String text){
        return text.trim().isEmpty();
    }
}
