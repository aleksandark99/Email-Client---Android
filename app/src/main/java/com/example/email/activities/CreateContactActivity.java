package com.example.email.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.Contact;
import com.example.email.repository.Repository;
import com.example.email.utility.PictureUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

public class CreateContactActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 2;

    private EditText inputEmail;
    private TextView displayEmail;
    private File mPhotoFile;
    private String name, lastname, email, info;
    private Button cameraButton, galleryButton;
    private CardView saveChanges;
    private ImageView mPhotoView;
    private Contact newContact = new Contact();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        mPhotoFile = Repository.get(this).getPhotoFile(newContact, this);

        inputEmail = findViewById(R.id.email_input_box);
        displayEmail = findViewById(R.id.email_display);

        mPhotoView = findViewById(R.id.image_profile);
        cameraButton = findViewById(R.id.camera);


        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(getPackageManager()) != null;
        cameraButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Log.d("TAG", "canTakePhoto = true");
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.email.fileprovider",
                    mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }

        cameraButton.setOnClickListener((View v) -> {
            startActivityForResult(captureImage, REQUEST_TAKE_PHOTO);
        });


        saveChanges = findViewById(R.id.card_view_bottom);
        saveChanges.setOnClickListener(v -> {

            TextInputLayout firstName = findViewById(R.id.first_name_input_box);
            this.name = firstName.getEditText().getText().toString();

            TextInputLayout lastName = findViewById(R.id.last_name_input_box);
            this.lastname = lastName.getEditText().getText().toString();

            TextInputLayout email = findViewById(R.id.email_input_box_layout);
            this.email = email.getEditText().getText().toString();

            TextInputLayout info = findViewById(R.id.info);
            this.info = info.getEditText().getText().toString();
        });

        inputEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                displayEmail.setText(s);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            updatePhotoView();
            //galleryAddPic();
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
            //set dummy
        } else {
            // Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), imageWidth, imageHeight);
            Bitmap bitmap =  PictureUtils.getScaledBitmap(mPhotoView, mPhotoFile.getAbsolutePath());
            mPhotoView.setImageBitmap(bitmap);
        }
    }



/*    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            try {
                mPhotoFile = mContact.createImageFile(storageDir);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (mPhotoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.email.fileprovider",
                        mPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        } else {
            Toast.makeText(getActivity(), "Camera not working", Toast.LENGTH_LONG).show();
        }
    }*/





}
