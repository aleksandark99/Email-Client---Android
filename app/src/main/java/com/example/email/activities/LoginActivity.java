package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.email.R;

public class LoginActivity extends AppCompatActivity {

    Button LoginButton;
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent registerIntent = RegisterActivity.newIntent(this);
        Intent goToEmailsIntent = EmailsActivity.newIntent(this);

        LoginButton =(Button) findViewById(R.id.loginButton);
        LoginButton.setOnClickListener(v -> {
            //start EmailsActivity ili onu sa listom svih activitija za sada
            //treba da dodje provera podataka itd itd...
            startActivity(goToEmailsIntent);
        });
        Register =(Button) findViewById(R.id.registerButton);
        Register.setOnClickListener(v -> {
            startActivity(registerIntent);

        });
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
}