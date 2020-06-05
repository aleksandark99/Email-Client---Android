package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.Account;
import com.example.email.model.Login;
import com.example.email.model.LoginResponse;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.login.LoginService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
    private LoginService mLoginService = mRetrofit.create(LoginService.class);

    private String username, password;

    private Button LoginButton, Register;
    private EditText usernameEditText, passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        //____________________________________________________________________________________

        Repository.loggedUser = null;
        Repository.jwt = null;

        usernameEditText = findViewById(R.id.usernameFiled); passwordEditText = findViewById(R.id.passwordField);



        if (savedInstanceState != null){
            username = savedInstanceState.getString("username");
            password = savedInstanceState.getString("password");
        }

        Intent registerIntent = RegisterActivity.newIntent(this);
        Intent goToEmailsIntent = EmailsActivity.newIntent(this);

        LoginButton =(Button) findViewById(R.id.loginButton);
        LoginButton.setOnClickListener(v -> {

            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();


            /////test ovo neaaaaffffffffff
            if (isWhitespacesOnly(username) || isWhitespacesOnly(password)){
                Toast.makeText(getApplicationContext(), "Credentials cannot be whitespaces", Toast.LENGTH_SHORT).show();
                startActivity(goToEmailsIntent);

            } else {
                //call backend

                Call<LoginResponse> call = mLoginService.testLogin(new Login(username, password));

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Wrong credentials!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //extract user & token
                        LoginResponse r = response.body();
                        //setujemo ulogovanog user-a i token za njega
                        Repository.loggedUser = r.getUser();
                        Log.i("TAG", "USEEER: " + String.valueOf(r.getUser()));
                        String authToken =  "Bearer " + r.getJwt();
                        Repository.jwt = authToken;

                        //set previous account if exists
                        SharedPreferences pref = Repository.getSharedPreferences(getApplicationContext());
                        int idOfLastUsedAccount = pref.getInt(Repository.loggedUser.getUsername(), -1);

                        if (idOfLastUsedAccount != -1) Repository.setActiveAccountForLoginActivity(idOfLastUsedAccount);

                        //welcome toast

                        usernameEditText.setText(""); passwordEditText.setText("");
                        Log.i("USER",Repository.loggedUser.toString());
                        startActivity(goToEmailsIntent);

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Cannot login, pogledaj Konzolu", Toast.LENGTH_SHORT).show();
                        Log.i("ERRROOOOOOR prilikom login-a", t.toString());
                        return;
                    }
                });


            }
            //startActivity(goToEmailsIntent);

        });
        Register =(Button) findViewById(R.id.registerButton);
        Register.setOnClickListener(v -> {

            startActivity(registerIntent);

        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("username", usernameEditText.getText().toString());
        savedInstanceState.putString("password", passwordEditText.getText().toString());


    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, LoginActivity.class);
        return i;
    } // smart call intent


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private boolean isWhitespacesOnly(String text){
        return text.trim().isEmpty();
    }
}
