package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.User;
import com.example.email.retrofit.contacts.ContactService;
import com.example.email.retrofit.contacts.RetrofitContactClient;
import com.example.email.retrofit.register.RegisterService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {



    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, RegisterActivity.class);
        return i;
    }
    private String firstname, lastname, username, password, confirmedPassword;

    private Button LoginButton, RegisterConfirmButton;
    private EditText firstnameText, lastnameText, usernameText, passwordText, passwordConfirmText;

    private Retrofit mRetrofit = RetrofitContactClient.getRetrofitInstance();
    private RegisterService mRegisteService = mRetrofit.create(RegisterService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstnameText=findViewById(R.id.firstNameText);
        lastnameText=findViewById(R.id.lastNameText);
        usernameText=findViewById(R.id.usernameText);
        passwordText=findViewById(R.id.passwordField);
        passwordConfirmText=findViewById(R.id.passwordConfirmField);

        LoginButton = (Button) findViewById(R.id.loginButtonReg);
        RegisterConfirmButton = (Button) findViewById(R.id.registerConfirmButton);

        if (savedInstanceState != null){
            firstname = savedInstanceState.getString("firstname");
            lastname = savedInstanceState.getString("lastname");
            username = savedInstanceState.getString("username");
            confirmedPassword = savedInstanceState.getString("confirmedPassword");
        }

        LoginButton.setOnClickListener(v -> {
            finish();
        });


        RegisterConfirmButton.setOnClickListener(v -> {

            firstname =  firstnameText.getText().toString();
            lastname = lastnameText.getText().toString();
            username = usernameText.getText().toString();
            password = passwordText.getText().toString();
            confirmedPassword = passwordConfirmText.getText().toString();


            if (isWhitespacesOnly(firstname) || isWhitespacesOnly(lastname) || isWhitespacesOnly(username) || isWhitespacesOnly(password)){
                Toast.makeText(this,"Credentials cannot be whitespaces!", Toast.LENGTH_SHORT).show();
            } else if (!Equals(password, confirmedPassword)){
                Toast.makeText(this,"Passwords must be equal!", Toast.LENGTH_SHORT).show();
            } else {
                //call backend

                Call<Boolean> call = mRegisteService.registerUser(new User(firstname, lastname, username, password));

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Cannot save user didn't hit API", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean isUserAdded = response.body();

                        if(isUserAdded) {
                          Toast.makeText(getApplicationContext(), "User "+ username +" registered", Toast.LENGTH_SHORT).show();
                          finish();
                        } else Toast.makeText(getApplicationContext(), "User already exists with chosen username or password", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Cannot save user GRESKA, pogledaj Konzolu", Toast.LENGTH_SHORT).show();
                        Log.i("Error pri dodavanju novog user", t.toString());
                        return;
                    }
                });

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("firstname", firstnameText.getText().toString());
        savedInstanceState.putString("lastname", lastnameText.getText().toString());
        savedInstanceState.putString("password", passwordText.getText().toString());
        savedInstanceState.putString("confirmedPassword", passwordConfirmText.getText().toString());

    }

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

    private boolean Equals(String s1, String s2){
        return s1.equals(s2);
    }
}
