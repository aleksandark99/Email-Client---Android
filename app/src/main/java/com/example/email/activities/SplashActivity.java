package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.email.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SLEEP_TIME = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Intent loginIntent = LoginActivity.newIntent(this);
        Intent loginIntent = LoginActivity.newIntent(this);
        Thread timer = new Thread(() -> {
            try {
                if (isNetworkAvailableAndConnected()) {
                    Thread.sleep(SLEEP_TIME);
                    startActivity(loginIntent);
                    finish();
                } else showToast("Check internet connection");
            } catch (InterruptedException ie) {ie.printStackTrace();} catch (Exception e){e.printStackTrace();}
        });
        timer.start();
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

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    private void showToast(String toast)
    {
        runOnUiThread(() -> Toast.makeText(this, toast, Toast.LENGTH_SHORT).show());
    }
}
