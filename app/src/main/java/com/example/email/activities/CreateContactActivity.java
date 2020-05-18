package com.example.email.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.example.email.R;
import com.example.email.model.Contact;
import com.example.email.repository.Repository;
import com.example.email.retrofit.contacts.ContactService;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.utility.Helper;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateContactActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_GALLERY_PHOTO = 3;
    private static final String EXTRA_NEW_CONTACT_ID = "com.example.email.create_contact_activity.NEW_CONTACT_ID";

    private EditText editTextBoxName, editTextBoxLastname, editTextBoxDisplayName, editTextBoxEmail, editTextBoxInfo;

    private String filePath, tempFilePath;
    private Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
    private ContactService mContactService = mRetrofit.create(ContactService.class);

    private Button cameraButton, galleryButton;
    private CardView saveChanges;
    private ImageView mPhotoView;
    private Contact newContact = new Contact();
    private boolean photoTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        editTextBoxName = findViewById(R.id.first_name_input_box_edit_box);
        editTextBoxLastname = findViewById(R.id.last_name_input_box_edit_box);
        editTextBoxDisplayName = findViewById(R.id.display_name_input_box_edit_box);
        editTextBoxEmail = findViewById(R.id.email_input_box_edit_box);
        editTextBoxInfo = findViewById(R.id.info_input_box_edit_box);

        mPhotoView = findViewById(R.id.image_profile);
        cameraButton = findViewById(R.id.camera); galleryButton = findViewById(R.id.gallery);

        if(savedInstanceState == null){

        } else {
            String name = savedInstanceState.getString("firstName"); editTextBoxName.setText(name);
            String lastName = savedInstanceState.getString("lastName"); editTextBoxLastname.setText(lastName);
            String displayName = savedInstanceState.getString("displayName"); editTextBoxDisplayName.setText(displayName);
            String email = savedInstanceState.getString("email"); editTextBoxEmail.setText(email);
            String info = savedInstanceState.getString("notes"); editTextBoxInfo.setText(info);

            boolean photoTaken = savedInstanceState.getBoolean("photoTaken"); this.photoTaken = photoTaken;

            if (this.photoTaken){

                filePath = savedInstanceState.getString("photoPath");
                Helper.displayImageIntoImageView(filePath, mPhotoView, this);

            }
        }

        final Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImage.setType("image/*");

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = captureImage.resolveActivity(getPackageManager()) != null;
        cameraButton.setEnabled(canTakePhoto);

        cameraButton.setOnClickListener((View v) -> {
            File f = Repository.get(this).getPhotoFile(newContact, this);
            tempFilePath = f.getAbsolutePath();
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.email.fileprovider",
                    f);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            startActivityForResult(captureImage, REQUEST_TAKE_PHOTO);
        });

        galleryButton.setOnClickListener((View v) -> {
            startActivityForResult(pickImage, REQUEST_GALLERY_PHOTO);
        });

        saveChanges = findViewById(R.id.card_view_bottom);
        saveChanges.setOnClickListener(v -> {

            newContact.setFirstName(editTextBoxName.getText().toString());
            newContact.setLastName(editTextBoxLastname.getText().toString());
            newContact.setDisplayName(editTextBoxDisplayName.getText().toString());
            newContact.setNote(editTextBoxInfo.getText().toString());
            newContact.setEmail(editTextBoxEmail.getText().toString());

            if (photoTaken) newContact.setPhotoPath(filePath);

            Call<Contact> call = mContactService.addContact(newContact, Repository.loggedUser.getId(), Repository.jwt);

            call.enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Didn't hit API for creating contact, some error ocured", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //newContact.setId(response.body());
                    newContact = response.body();
                    Repository.loggedUser.getContacts().add(newContact);
                    Intent data = new Intent();
                    data.putExtra("photoTaken", photoTaken);
                    setResult(RESULT_OK, data);
                    finish();
                }

                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Cannot save contact GRESKA, pogledaj Konzolu", Toast.LENGTH_SHORT).show();
                    Log.i("Error pri dodavanju novog kontakta", t.toString());
                    return;
                }
            });
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("firstName", editTextBoxName.getText().toString());
        savedInstanceState.putString("lastName", editTextBoxLastname.getText().toString());
        savedInstanceState.putString("email", editTextBoxEmail.getText().toString());
        savedInstanceState.putString("displayName", editTextBoxDisplayName.getText().toString());
        savedInstanceState.putString("notes", editTextBoxInfo.getText().toString());
        savedInstanceState.putBoolean("photoTaken", photoTaken);
        if (photoTaken)  savedInstanceState.putString("photoPath", filePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            photoTaken = true;
            filePath = tempFilePath;
            Helper.displayImageIntoImageView(filePath, mPhotoView, this);

        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_CANCELED){

        } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_OK){
            photoTaken = true;

            Uri selectedImageUri = data.getData();
            String picturePath = Helper.getPicturePath(selectedImageUri, this);
            Helper.displayImageIntoImageView(picturePath, mPhotoView, this);
            filePath = picturePath;

        } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_CANCELED){

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
