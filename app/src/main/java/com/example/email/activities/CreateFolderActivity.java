package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.email.R;

public class CreateFolderActivity extends AppCompatActivity {

    Button btnCancel;
    ImageView imgClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnCancel = findViewById(R.id.btnCancel);
        imgClose = findViewById(R.id.closeAddFolder);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
