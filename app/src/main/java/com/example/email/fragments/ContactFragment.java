package com.example.email.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.email.R;
import com.example.email.model.Contact;
import com.example.email.repository.Repository;
import com.example.email.retrofit.contacts.ContactService;
import com.example.email.retrofit.contacts.RetrofitContactClient;
import com.example.email.utility.Helper;
import com.example.email.utility.PictureUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactFragment extends Fragment {

    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_GALLERY_PHOTO = 3;

    private static final String CONTACT_KEY_ID = "com.example.email.fragments.contact_id";

    private EditText editTextBoxName, editTextBoxLastname, editTextBoxEmail;
    private String filePath, tempFilePath;
    private String first_Name,last_Name, Email;

    private Contact mContact;
    private Button mCameraButton, mGalleryButton;
    private ImageView mPhotoView;
    private boolean photoTaken;


    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int contactId = getArguments().getInt(CONTACT_KEY_ID);
        fetchContact(contactId);
        //mContact = Repository.get(getActivity()).findContactById(contactId);


        if(savedInstanceState == null){
            filePath = mContact.getPhotoPath();
            first_Name = mContact.getFirstName(); last_Name = mContact.getLastName(); Email = mContact.getEmail();
        } else {
            String name = savedInstanceState.getString("firstName"); //editTextBoxName.setText(name);
            String lastName = savedInstanceState.getString("lastName"); //editTextBoxLastname.setText(lastName);
            String email = savedInstanceState.getString("email"); //editTextBoxEmail.setText(email);

            first_Name = name; last_Name = lastName; Email = email;

            boolean photoTaken = savedInstanceState.getBoolean("photoTaken"); this.photoTaken = photoTaken;

            if (this.photoTaken){

                filePath = savedInstanceState.getString("photoPath");

            } else{
                filePath = savedInstanceState.getString("photoPath");

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_contact, container, false);

        mPhotoView = root.findViewById(R.id.image_profile);

        editTextBoxName = root.findViewById(R.id.first_name_input_box_edit_box); editTextBoxName.setText(first_Name);
        editTextBoxLastname = root.findViewById(R.id.last_name_input_box_edit_box); editTextBoxLastname.setText(last_Name);
        editTextBoxEmail = root.findViewById(R.id.email_input_box_edit_box); editTextBoxEmail.setText(Email);

        int color = ((int)(Math.random()*16777215)) | (0xFF << 24);

        CardView cardViewTop = root.findViewById(R.id.card_view_top);
        cardViewTop.setCardBackgroundColor(color);

        CardView cardViewBottom = root.findViewById(R.id.card_view_bottom);
        cardViewBottom.setCardBackgroundColor(color);

        LinearLayout saveChanges = root.findViewById(R.id.linear_layout_bottom);
        saveChanges.setOnClickListener((View v) -> {

            mContact.setFirstName(editTextBoxName.getText().toString());
            mContact.setLastName(editTextBoxLastname.getText().toString());
            mContact.setEmail(editTextBoxEmail.getText().toString());

            if(photoTaken)  mContact.setPhotoPath(filePath);

            Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();
        });

        mCameraButton = root.findViewById(R.id.camera); mGalleryButton = root.findViewById(R.id.gallery);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = captureImage.resolveActivity(getActivity().getPackageManager()) != null;
        mCameraButton.setEnabled(canTakePhoto);

        mCameraButton.setOnClickListener((View v) -> {

            File f = Repository.get(getActivity()).getPhotoFile(mContact, getActivity());
            tempFilePath = f.getAbsolutePath();
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.example.email.fileprovider",
                    f);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(captureImage, REQUEST_TAKE_PHOTO);
        });

        final Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImage.setType("image/*");

        mGalleryButton.setOnClickListener((View v) -> {
            startActivityForResult(pickImage, REQUEST_GALLERY_PHOTO);
        });

        //if (filePath != null) Helper.displayImageIntoImageView(filePath, mPhotoView, getActivity());
        Helper.displayImageIntoImageView(filePath, mPhotoView, getActivity());
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("firstName", editTextBoxName.getText().toString());
        savedInstanceState.putString("lastName", editTextBoxLastname.getText().toString());
        savedInstanceState.putString("email", editTextBoxEmail.getText().toString());
        savedInstanceState.putBoolean("photoTaken", photoTaken);

        if (photoTaken)  savedInstanceState.putString("photoPath", filePath);
        else if (!photoTaken && mContact.getPhotoPath() != null) savedInstanceState.putString("photoPath", mContact.getPhotoPath());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            photoTaken = true;
            //updatePhotoView(120, 120);
            filePath = tempFilePath;
            Helper.displayImageIntoImageView(filePath, mPhotoView, getActivity());


        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_CANCELED){
            //photoTaken = false;
        } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == Activity.RESULT_CANCELED){
            //photoTaken = false;
        } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == Activity.RESULT_OK){
            photoTaken = true;

            Uri selectedImageUri = data.getData();
            String picturePath = Helper.getPicturePath(selectedImageUri, getActivity());

            Helper.displayImageIntoImageView(picturePath, mPhotoView, getActivity());

            filePath = picturePath;

        }

    }

    private void fetchContact(int idContact){
        //final ArrayList<Contact> contacts;
        Log.i("Dosao u", "fetchContact");
        Retrofit mRetrofit = RetrofitContactClient.getRetrofitInstance();
        ContactService mContactService = mRetrofit.create(ContactService.class);

        Call<Contact> call = mContactService.getContact(idContact);

        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {

                if (!response.isSuccessful()){
                    Log.i("GRESKA", String.valueOf(response.code()));
                }
                mContact = response.body();
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.i("FAILURE", t.toString());
            }
        });




    }

    public static ContactFragment newInstance(int idContact) {
        Bundle args = new Bundle();
        args.putInt(CONTACT_KEY_ID , idContact);
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
