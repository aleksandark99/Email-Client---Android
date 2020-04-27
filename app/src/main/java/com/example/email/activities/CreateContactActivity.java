package com.example.email.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.email.R;
import com.example.email.model.Contact;
import com.example.email.repository.Repository;
import com.example.email.utility.Helper;
import com.example.email.utility.PictureUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CreateContactActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        //____________________________________________________________________________________


        newId = getIntent().getIntExtra(EXTRA_NEW_CONTACT_ID, -1);
        newContact.setId(newId);

        editTextBoxName = findViewById(R.id.first_name_input_box_edit_box);
        editTextBoxLastname = findViewById(R.id.last_name_input_box_edit_box);
        editTextBoxEmail = findViewById(R.id.email_input_box_edit_box);
        editTextBoxInfo = findViewById(R.id.info_input_box_edit_box);

        mPhotoView = findViewById(R.id.image_profile);
        cameraButton = findViewById(R.id.camera); galleryButton = findViewById(R.id.gallery);

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
                //updatePhotoView(120, 120);
            } else  mPhotoFile = Repository.get(this).getPhotoFile(newContact, this);


        }



        final Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImage.setType("image/*");



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

        galleryButton.setOnClickListener((View v) -> {
            startActivityForResult(pickImage, REQUEST_GALLERY_PHOTO);
        });


        saveChanges = findViewById(R.id.card_view_bottom);
        saveChanges.setOnClickListener(v -> {

            newContact.setFirstname(editTextBoxName.getText().toString());
            newContact.setLastname(editTextBoxLastname.getText().toString());
            newContact.setEmail(editTextBoxEmail.getText().toString());

            //newContact.setCurrentPhotoPath(mPhotoFile.getAbsolutePath());
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
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            photoTaken = true;
            Helper.displayImageIntoImageView(mPhotoFile.getAbsolutePath(), mPhotoView, this);
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_CANCELED){
            photoTaken = false;
        } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_OK){
            photoTaken = true;
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            //Uri imageUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Helper.displayImageIntoImageView(picturePath, mPhotoView, this);

            mPhotoFile = new File(picturePath);

                /*Uri imgUri = data.getData();

                try (InputStream inputStream = this.getContentResolver().openInputStream(imgUri);
                     FileOutputStream stream = new FileOutputStream(mPhotoFile)) {
                    stream.write(getBytes(inputStream));
                } catch (IOException e){e.printStackTrace();};

                updatePhotoView();*/
        } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_CANCELED){
            photoTaken = false;
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);

        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), 120, 120);
            //Bitmap bitmap =  PictureUtils.getScaledBitmap(mPhotoView, mPhotoFile.getAbsolutePath());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

/*    private void updatePhotoView(int imageWidth, int imageHeight) {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);

        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), imageWidth, imageHeight);
            //Bitmap bitmap =  PictureUtils.getScaledBitmap(mPhotoView, mPhotoFile.getAbsolutePath());
            mPhotoView.setImageBitmap(bitmap);
        }
    }*/

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


/*    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 8192;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }*/


}
