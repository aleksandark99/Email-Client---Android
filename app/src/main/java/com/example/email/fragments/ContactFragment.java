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
import com.example.email.utility.Helper;
import com.example.email.utility.PictureUtils;

import java.io.File;
import java.io.IOException;

public class ContactFragment extends Fragment {

    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_GALLERY_PHOTO = 3;



    private static final String CONTACT_KEY_ID = "com.example.email.fragments.contact_id";

    private EditText editTextBoxName, editTextBoxLastname, editTextBoxEmail;
    private String filePath, tempFilePath;
    private String first_Name,last_Name, Email;

    private Contact mContact;
    private Button mCameraButton, mGalleryButton;
    //private File mPhotoFile;
    private ImageView mPhotoView;
    private boolean photoTaken;


    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int contactId = getArguments().getInt(CONTACT_KEY_ID);
        mContact = Repository.get(getActivity()).findContactById(contactId);

       // if (mContact.getCurrentPhotoPath() != null ) mPhotoFile = new File(mContact.getCurrentPhotoPath());
       // else mPhotoFile = Repository.get(getActivity()).getPhotoFile(mContact, getActivity());

        if(savedInstanceState == null){
            /*if (mContact.getCurrentPhotoPath() != null ) mPhotoFile = new File(mContact.getCurrentPhotoPath());
            else mPhotoFile = Repository.get(getActivity()).getPhotoFile(mContact, getActivity());*/
            filePath = mContact.getCurrentPhotoPath();
            first_Name = mContact.getFirstname(); last_Name = mContact.getLastname(); Email = mContact.getEmail();
        } else {
            String name = savedInstanceState.getString("firstName"); //editTextBoxName.setText(name);
            String lastName = savedInstanceState.getString("lastName"); //editTextBoxLastname.setText(lastName);
            String email = savedInstanceState.getString("email"); //editTextBoxEmail.setText(email);

            first_Name = name; last_Name = lastName; Email = email;

            boolean photoTaken = savedInstanceState.getBoolean("photoTaken"); this.photoTaken = photoTaken;

            if (this.photoTaken){
                //mPhotoFile = new File(savedInstanceState.getString("photoPath"));
                //updatePhotoView(120, 120);
                filePath = savedInstanceState.getString("photoPath");
                Helper.displayImageIntoImageView(filePath, mPhotoView, getActivity());
            } else{
                //mPhotoFile = Repository.get(getActivity()).getPhotoFile(mContact, getActivity());
               /* if (mContact.getCurrentPhotoPath() != null){
                    Log.i("tag", "ovsaaffsaasfsss");
                    mPhotoFile = new File(mContact.getCurrentPhotoPath());
                }else mPhotoFile = Repository.get(getActivity()).getPhotoFile(mContact, getActivity());*/
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


            mContact.setFirstname(editTextBoxName.getText().toString());
            mContact.setLastname(editTextBoxLastname.getText().toString());
            mContact.setEmail(editTextBoxEmail.getText().toString());

            if(photoTaken)  mContact.setCurrentPhotoPath(filePath);

            Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();
            //getActivity().finish();

        });

        mCameraButton = root.findViewById(R.id.camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = captureImage.resolveActivity(getActivity().getPackageManager()) != null;
        mCameraButton.setEnabled(canTakePhoto);
       // if (canTakePhoto && mPhotoFile != null) {

          /*  Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.example.email.fileprovider",
                    mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);*/
        //}
        mCameraButton.setOnClickListener((View v) -> {
            /*if (mPhotoFile == null) mPhotoFile =  Repository.get(getActivity()).getPhotoFile(mContact, getActivity());
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.example.email.fileprovider",
                    mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(captureImage, REQUEST_TAKE_PHOTO);*/
            File f = Repository.get(getActivity()).getPhotoFile(mContact, getActivity());
            //filePath = f.getAbsolutePath();
            tempFilePath = f.getAbsolutePath();
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.example.email.fileprovider",
                    f);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            startActivityForResult(captureImage, REQUEST_TAKE_PHOTO);
        });



        //if(photoTaken || mContact.getCurrentPhotoPath() != null) Helper.displayImageIntoImageView(m);
        /*if(photoTaken)updatePhotoView(120, 120);
        if (mContact.getCurrentPhotoPath() != null) updatePhotoView(120, 120);*/
        if (filePath != null) Helper.displayImageIntoImageView(filePath, mPhotoView, getActivity());
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("firstName", editTextBoxName.getText().toString());
        savedInstanceState.putString("lastName", editTextBoxLastname.getText().toString());
        savedInstanceState.putString("email", editTextBoxEmail.getText().toString());
        savedInstanceState.putBoolean("photoTaken", photoTaken);

        //if (photoTaken && mPhotoFile != null)  savedInstanceState.putString("photoPath", mPhotoFile.getAbsolutePath());
        if (photoTaken)  savedInstanceState.putString("photoPath", filePath);

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




/*    private void updatePhotoView(int imageWidth, int imageHeight) {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            //Log.i("test","asraf");
            mPhotoView.setImageResource(mContact.getAvatar());

        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), imageWidth, imageHeight);
            //Bitmap bitmap =  PictureUtils.getScaledBitmap(mPhotoView, mPhotoFile.getAbsolutePath());
            mPhotoView.setImageBitmap(bitmap);
        }
    }*/



    public static ContactFragment newInstance(int idContact) {
        Bundle args = new Bundle();
        args.putInt(CONTACT_KEY_ID , idContact);
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
