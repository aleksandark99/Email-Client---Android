package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.email.R;
import com.example.email.fragments.AddAccountFragment;

public class ProfileActivity extends AppCompatActivity {

    ImageButton btnAddAccount;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    AddAccountFragment addAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

    }
}
