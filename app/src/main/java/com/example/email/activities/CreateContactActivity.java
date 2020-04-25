package com.example.email.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
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
    static final int REQUEST_GALLERY_PHOTO = 3;
    private static final String EXTRA_NEW_CONTACT_ID = "com.example.email.create_contact_activity.NEW_CONTACT_ID";

    private EditText editTextBoxName, editTextBoxLastname, editTextBoxEmail, editTextBoxInfo;

    private File mPhotoFile;
    private Button cameraButton, galleryButton;
    private CardView saveChanges;
    private ImageView mPhotoView;
    private int newId;
    private Contact newContact = new Contact();
    private boolean photoTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        newId = getIntent().getIntExtra(EXTRA_NEW_CONTACT_ID, -1);
        newContact.setId(newId);

        editTextBoxName = findViewById(R.id.first_name_input_box_edit_box);
        editTextBoxLastname = findViewById(R.id.last_name_input_box_edit_box);
        editTextBoxEmail = findViewById(R.id.email_input_box_edit_box);
        editTextBoxInfo = findViewById(R.id.info_input_box_edit_box);

        mPhotoView = findViewById(R.id.image_profile);
        cameraButton = findViewById(R.id.camera);

        if(savedInstanceState == null){
            mPhotoFile = Repository.get(this).getPhotoFile(newContact, this);
        } else {
            String name = savedInstanceState.getString("firstName"); editTextBoxName.setText(name);
            String lastName = savedInstanceState.getString("lastName"); editTextBoxLastname.setText(lastName);
            String email = savedInstanceState.getString("email"); editTextBoxEmail.setText(email);
            String info = savedInstanceState.getString("info"); editTextBoxInfo.setText(info);

            boolean photoTaken = savedInstanceState.getBoolean("photoTaken"); this.photoTaken = photoTaken;

            if (this.photoTaken){
                mPhotoFile = new File(savedInstanceState.getString("photoPath"));
                updatePhotoView(120, 120);
            } else  mPhotoFile = Repository.get(this).getPhotoFile(newContact, this);


        }


        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = captureImage.resolveActivity(getPackageManager()) != null;
        cameraButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
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

            newContact.setFirstname(editTextBoxName.getText().toString());
            newContact.setLastname(editTextBoxLastname.getText().toString());
            newContact.setEmail(editTextBoxEmail.getText().toString());

            if (photoTaken) newContact.setCurrentPhotoPath(mPhotoFile.getAbsolutePath());

            newContact.setAvatar(R.drawable.dummy_contact_photo);


            Repository.get(this).getContacts().add(newContact);

            Intent data = new Intent();
            data.putExtra("photoTaken", photoTaken);
            setResult(RESULT_OK, data);
            finish();
        });





    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("firstName", editTextBoxName.getText().toString());
        savedInstanceState.putString("lastName", editTextBoxLastname.getText().toString());
        savedInstanceState.putString("email", editTextBoxEmail.getText().toString());
        savedInstanceState.putString("info", editTextBoxInfo.getText().toString());
        savedInstanceState.putBoolean("photoTaken", photoTaken);
        if (photoTaken)  savedInstanceState.putString("photoPath", mPhotoFile.getAbsolutePath());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            updatePhotoView();
            photoTaken = true;
            //galleryAddPic();

        } else if (resultCode == RESULT_CANCELED){
            photoTaken = false;
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);

        } else {
            // Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), imageWidth, imageHeight);
            Bitmap bitmap =  PictureUtils.getScaledBitmap(mPhotoView, mPhotoFile.getAbsolutePath());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void updatePhotoView(int imageWidth, int imageHeight) {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);

        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), imageWidth, imageHeight);
            //Bitmap bitmap =  PictureUtils.getScaledBitmap(mPhotoView, mPhotoFile.getAbsolutePath());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
    }

    public static Intent newIntent(Context packageContext, int newContactId) {
        Intent i = new Intent(packageContext, CreateContactActivity.class);
        i.putExtra(EXTRA_NEW_CONTACT_ID, newContactId);
        return i;
    }







}
