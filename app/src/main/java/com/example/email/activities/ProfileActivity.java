package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    ImageButton btnAddAccount;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    AddAccountFragment addAccount;

    private EditText mFirstnameEditText ,mLastnameEditText,  mUsernameEditText , mPasswordEditText;

    private String firstname, lastname, username, password;

    private Button saveChanges, editAccounts;


    private Context c = this;

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

/*                ArrayList<Account> l = new ArrayList<Account>(); l.add(new Account()); l.add(new Account());
                ViewAdapter viewAdapter = new ViewAdapter(getApplicationContext(), l);
                ViewPager viewPager = findViewById(R.id.view_pager);

                viewPager.setAdapter(viewAdapter);
                mDotsIndicator.setViewPager(viewPager);*/
                ArrayList<Account> l = new ArrayList<Account>(); l.add(new Account(1, "aaaa")); l.add(new Account(2, "bbb"));
                startActivity(AccountActivity.newIntent(c, 0, l));
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
                            Toast.makeText(getApplicationContext(), "User credentials saved!", Toast.LENGTH_SHORT).show();

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
}
