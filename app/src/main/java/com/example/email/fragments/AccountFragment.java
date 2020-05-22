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
import androidx.fragment.app.Fragment;

import com.example.email.R;
import com.example.email.model.Account;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.account.AccountService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountFragment extends Fragment {

    private static final String ACCOUNT_KEY = "com.example.email.fragments.account";
    private Account mAccount;


    private ImageView btnClose;
    private Button btnSaveAccount, btnDeleteAccount;
    private EditText txtSmtpServerAddress,txtSmtpPort, txtInServerAddress, txtInServerPort ,  txtEmail, txtDisplayName, txtPassword;
    private CheckBox btnImap, btnPop3, btnYes, btnNo;

    private String smtpServerAddress, inServerAddress, email, username, password, displayname;
    private int smtpPort, inServerType, inServerPort ;
    private boolean authentication, Imap, Pop3, authNotChoosen;

    private final Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
    private final AccountService mAccountService = mRetrofit.create(AccountService.class);

    public AccountFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = getArguments().getParcelable(ACCOUNT_KEY);

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
            smtpServerAddress = mAccount.getSmtpAddress();
            smtpPort = mAccount.getSmtpPort();
            inServerType = mAccount.getInServerType(); //1 ILI 0

            if (inServerType == 1) Imap = true;
            else if (inServerType == 0) Pop3 = true;

            inServerPort = mAccount.getInServerPort();
            inServerAddress = mAccount.getInServerAddress();
            email = mAccount.getUsername();
            displayname = mAccount.getDisplayName();
            password = mAccount.getPassword();

            authentication = mAccount.isAuthentication();
        }

    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_account, container, false);
        rootView.setBackgroundColor(Color.WHITE);

        btnClose = rootView.findViewById(R.id.btnCloseAccount);
        btnSaveAccount = rootView.findViewById(R.id.btnSaveChanges);
        btnDeleteAccount = rootView.findViewById(R.id.btnDeleteAccount);

        txtSmtpServerAddress = rootView.findViewById(R.id.smtpServerAddress); txtSmtpServerAddress.setText(smtpServerAddress);

        txtSmtpPort = rootView.findViewById(R.id.smtpPort);
        txtSmtpPort.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (smtpPort != 0){
            txtSmtpPort.setText(String.valueOf(smtpPort));
        }

        btnImap = rootView.findViewById(R.id.imap);
        btnPop3 = rootView.findViewById(R.id.pop3);
        if (inServerType == 1){
             btnImap.setChecked(Imap);
        } else if (inServerType == 0){
             btnPop3.setChecked(Pop3);
        }


        txtInServerAddress = rootView.findViewById(R.id.inServerAddress); txtInServerAddress.setText(inServerAddress);
        txtInServerPort = rootView.findViewById(R.id.inServerPort);
        txtInServerPort.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (inServerPort != 0){
            txtInServerPort.setText(String.valueOf(inServerPort));
        }

        txtEmail = rootView.findViewById(R.id.email); txtEmail.setText(email);
        txtDisplayName = rootView.findViewById(R.id.display_name); txtDisplayName.setText(displayname);
        txtPassword = rootView.findViewById(R.id.password); txtPassword.setText(password);

        btnYes = rootView.findViewById(R.id.btnYes);
        btnNo = rootView.findViewById(R.id.btnNo);

        if (authNotChoosen){

        } else if (authentication){
            btnYes.setChecked(true);
        } else {
            btnNo.setChecked(true);
        }



        btnSaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Account newAccount = getAccount();

                if (newAccount != null){
                    // send to backend;
                    newAccount.setId(mAccount.getId());
                    Call<Account> call = mAccountService.updateAccount(newAccount, Repository.loggedUser.getId(), mAccount.getId(),  Repository.jwt);

                    call.enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {

                            if (!response.isSuccessful()){
                                Toast.makeText(getActivity(), "Response not successful", Toast.LENGTH_SHORT).show();
                                Log.i("GRESKA U AccountFragment-u", String.valueOf(response.code()));
                            }
                            Account updatedAcc = response.body();
                            if (updatedAcc == null){
                                Toast.makeText(getActivity(), "Please choose different email address!", Toast.LENGTH_SHORT).show();
                            } else{
                                Repository.loggedUser.getAccounts().remove(mAccount);
                                Repository.loggedUser.getAccounts().add(updatedAcc);
                                Toast.makeText(getActivity(), "Account updated", Toast.LENGTH_LONG).show();
                                //getActivity().getSupportFragmentManager().beginTransaction().remove(AccountFragment.this).commit();
                                getActivity().finish();
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

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Call<ResponseBody> call = mAccountService.deleteAccount(Repository.loggedUser.getId(), mAccount.getId(), Repository.jwt);

                call.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (!response.isSuccessful()){
                            Log.i("ERROR KOD BRISANJA ACCOUNTA POGLEDAJ KONZOLU", String.valueOf(response.code()));
                            return;
                        }
                        if (response.code() == 200){
                            Repository.loggedUser.getAccounts().remove(mAccount);
                            Toast.makeText(getActivity(), "Account deleted!", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }else Toast.makeText(getActivity(), "Account is not deleted, error on server!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), "ERROOOOOR PRILIKOM BRISANJA account-a POGLEDAJ KONZOLU", Toast.LENGTH_SHORT).show();
                        Log.i("ERROOOOOR PRILIKOM BRISANJA Accounta POGLEDAJ KONZOLU", t.toString());
                    }
                });
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

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().finish();
            }
        });


        return  rootView;
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
        else if (!btnImap.isChecked() && !btnPop3.isChecked()) outState.putInt("inServerType", -1);

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




    public static AccountFragment newInstance(Account account) {
        Bundle args = new Bundle();
        args.putParcelable(ACCOUNT_KEY, account);
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
