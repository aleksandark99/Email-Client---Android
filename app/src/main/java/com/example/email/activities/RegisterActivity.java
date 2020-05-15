package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.email.R;

public class RegisterActivity extends AppCompatActivity {



    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, RegisterActivity.class);
        return i;
    }
    private String firstname, lastname, username, password, confirmedPassword;

    private Button LoginButton, RegisterConfirmButton;
    private EditText firstnameText, lastnameText, usernameText, passwordText, passwordConfirmText;


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
           // startActivity(loginIntent);
            finish();
        });


        RegisterConfirmButton.setOnClickListener(v -> {

            firstname =  firstnameText.getText().toString();
            lastname = lastnameText.getText().toString();
            username = usernameText.getText().toString();
            password = passwordText.getText().toString();
            confirmedPassword = passwordConfirmText.getText().toString();




            // ako su username i password ispunjavaju uslove pozovi metodu za kreiranje acc-a
            // i prebaci ga na login activity
            //usernameText.setError("Zauzeto korisnicko ime"); // ovako se raise errors sa podacima
            //startActivity(loginIntent);

            if (isWhitespacesOnly(firstname) || isWhitespacesOnly(lastname) || isWhitespacesOnly(username) || isWhitespacesOnly(password)){
                Toast.makeText(this,"Credentials cannot be whitespaces!", Toast.LENGTH_SHORT).show();
            } else if (!Equals(password, confirmedPassword)){
                Toast.makeText(this,"Passwords must be equal!", Toast.LENGTH_SHORT).show();
            } else {
                //call backend
                finish();
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
