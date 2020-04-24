package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.email.R;

public class RegisterActivity extends AppCompatActivity {

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, RegisterActivity.class);
        return i;
    }

    Button LoginButton;
    Button RegisterConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent loginIntent = LoginActivity.newIntent(this);
        LoginButton = (Button) findViewById(R.id.loginButtonReg);
        LoginButton.setOnClickListener(v -> {
            startActivity(loginIntent);


        });

        RegisterConfirmButton = (Button) findViewById(R.id.registerConfirmButton);
        RegisterConfirmButton.setOnClickListener(v -> {


            // ako su username i password ispunjavaju uslove pozovi metodu za kreiranje acc-a
            // i prebaci ga na login activity
            startActivity(loginIntent);


        });


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
}
