package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.fragments.AddAccountFragment;
import com.example.email.model.Account;
import com.example.email.model.LoginResponse;
import com.example.email.model.User;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton btnAddAccount;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private AddAccountFragment addAccount;

    private EditText mFirstnameEditText ,mLastnameEditText,  mUsernameEditText , mPasswordEditText;

    private TextView mTextViewAccount;

    private String firstname, lastname, username, password;

    private Button saveChanges, editAccounts;

    private Spinner spinner;

    private ArrayAdapter<String> accountsAdapter;

    private String[] emails;
    int count=0;
    private final Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
    private final UserService mUserService = mRetrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirstnameEditText = findViewById(R.id.firstname);
        mLastnameEditText = findViewById(R.id.lastname);
        mUsernameEditText = findViewById(R.id.username);
        mPasswordEditText = findViewById(R.id.password);
        mTextViewAccount = (TextView) findViewById(R.id.account_display);

        emails = getStringArray();

        if (Repository.activeAccount == null){
            mTextViewAccount.setText(R.string.choose_account);
        } else mTextViewAccount.setText(Repository.activeAccount.getUsername());

        spinner = findViewById(R.id.accounts);

        accountsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emails);

        spinner.setAdapter(accountsAdapter);

        spinner.setSelection(findPositionOfActiveAccount(), false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (count >= 1){
                    if ( position != 0){
                        Repository.setNewActiveAccount(emails[position]);
                        //Write to shared pref
                        SharedPreferences pref = Repository.getSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = pref.edit();

                        //overwrites old value with same key...
                        editor.putInt(Repository.loggedUser.getUsername(), Repository.activeAccount.getId());
                        editor.apply();
                        mTextViewAccount.setText(emails[position]);
                    } else{
                        Repository.activeAccount = null;
                        mTextViewAccount.setText(R.string.choose_account);
                    }
                }
                count++;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        if (savedInstanceState != null){
            firstname = savedInstanceState.getString("firstname");
            lastname = savedInstanceState.getString("lastname");
            username = savedInstanceState.getString("username");
            password = savedInstanceState.getString("password");
        } else {
            firstname = Repository.loggedUser.getFirstName();
            lastname = Repository.loggedUser.getLastName();
            username = Repository.loggedUser.getUsername();
            password = Repository.loggedUser.getPassword();
        }

        mFirstnameEditText.setText(firstname); mLastnameEditText.setText(lastname);
        mUsernameEditText.setText(username); mPasswordEditText.setText(password);

        btnAddAccount = findViewById(R.id.btnAddAccount);
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                addAccount = new AddAccountFragment();

                transaction.replace(R.id.add_account_fragment, addAccount).addToBackStack(null);
                transaction.commit();

            }
        });

        editAccounts = findViewById(R.id.edit_account);
        editAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opens view pager

                if (Repository.loggedUserHaveAccount()) {
                    startActivity(AccountActivity.newIntent(ProfileActivity.this, 0, new ArrayList<Account>(Repository.loggedUser.getAccounts())));
                } else Toast.makeText(ProfileActivity.this, "Please create an account", Toast.LENGTH_LONG).show();

            }
        });

        saveChanges = findViewById(R.id.save_changes);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Call<LoginResponse> call = mUserService.updateUser(new User(Repository.loggedUser.getId(), mFirstnameEditText.getText().toString(), mLastnameEditText.getText().toString(), mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString(), "ROLE_USER"), Repository.jwt);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (!response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Username already exist!", Toast.LENGTH_SHORT).show();
                            Log.i("GRESKA U ProfileActivity", String.valueOf(response.code()));
                            return;
                        }

                        LoginResponse lr = response.body();
                        if (lr == null){
                            Toast.makeText(getApplicationContext(), "Username already exist!", Toast.LENGTH_SHORT).show();
                        } else {

                            lr.getUser().setContacts(Repository.loggedUser.getContacts());
                            Repository.loggedUser = lr.getUser();
                            String token = lr.getJwt();
                            Repository.jwt = "Bearer " + token;
                            Toast.makeText(getApplicationContext(), "User credentials saved!", Toast.LENGTH_SHORT).show();;
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                        Log.i("FAILURE", t.toString());
                    }
                });
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("firstname", mFirstnameEditText.getText().toString());
        savedInstanceState.putString("lastname", mLastnameEditText.getText().toString());
        savedInstanceState.putString("username", mUsernameEditText.getText().toString());
        savedInstanceState.putString("password", mPasswordEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String[] getStringArray(){
        String[] arr = new String[Repository.loggedUser.getAccounts().size()+1];
        arr[0] = "No account selected";
        int index = 1;
        for (Account a : Repository.loggedUser.getAccounts()){
            arr[index] = a.getUsername();
            index++;
        }
        return arr;
    }

    private int findPositionOfActiveAccount(){
        if (Repository.activeAccount == null) return 0;
        for (int i = 0; i<emails.length; i++){
            if (emails[i].equals(Repository.activeAccount.getUsername())) return i;
        }
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        emails = getStringArray();
        accountsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emails);
        accountsAdapter.notifyDataSetChanged();
        spinner.setAdapter(accountsAdapter);
        count = 0;
        if (Repository.activeAccount != null){
            //set appropriate value in spinner
           spinner.setSelection(findPositionOfActiveAccount());
        } else mTextViewAccount.setText("Please choose an account!");
    }
}
