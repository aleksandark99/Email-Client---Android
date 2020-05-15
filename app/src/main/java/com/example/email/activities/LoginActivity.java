package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.Login;
import com.example.email.retrofit.contacts.RetrofitClient;
import com.example.email.retrofit.login.LoginService;

import okhttp3.ResponseBody;
import retrofit2.Call;
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



            if (isWhitespacesOnly(username) || isWhitespacesOnly(password)){
                Toast.makeText(getApplicationContext(), "Credentials cannot be whitespaces", Toast.LENGTH_SHORT).show();
            } else {
                //call backend
            }

            //Call<ResponseBody> call = mLoginService.testLogin(new Login(us))


            //startActivity(goToEmailsIntent);

            // ovo ako su neispravni podaci
            //

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
