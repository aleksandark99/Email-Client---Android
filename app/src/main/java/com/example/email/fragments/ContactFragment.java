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
import com.example.email.utility.PictureUtils;

import java.io.File;
import java.io.IOException;

public class ContactFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;

    private static final String CONTACT_KEY_ID = "ftn.sit.emailme.fragments.contact_id";
    private Contact mContact;
    private Button mCameraButton;
    private File mPhotoFile;
    private ImageView mPhotoView;
    private int imageWidth, imageHeight;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int contactId = getArguments().getInt(CONTACT_KEY_ID);
        mContact = Repository.get(getActivity()).findContactById(contactId);
       // mPhotoFile =
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_contact, container, false);

        ImageView imageView = root.findViewById(R.id.image_profile);
        /*if (mContact.getAvatar() != -1){
            Drawable drawableContact = container.getResources().getDrawable(mContact.getAvatar());
            imageView.setImageDrawable(drawableContact);
        } else {
            //set dummy image..
        }*/


        EditText firstnameEditText = (EditText)root.findViewById(R.id.first_name_input_box);
        firstnameEditText.setText(mContact.getFirstname());

        EditText lastnameEditText = (EditText)root.findViewById(R.id.last_name_input_box);
        lastnameEditText.setText(mContact.getLastname());

        EditText emailEditText = (EditText)root.findViewById(R.id.email_input_box);
        emailEditText.setText(mContact.getEmail());

        int color = ((int)(Math.random()*16777215)) | (0xFF << 24);

        CardView cardViewTop = root.findViewById(R.id.card_view_top);
        cardViewTop.setCardBackgroundColor(color);

        CardView cardViewBottom = root.findViewById(R.id.card_view_bottom);
        cardViewBottom.setCardBackgroundColor(color);

        LinearLayout saveChanges = root.findViewById(R.id.linear_layout_bottom);
        saveChanges.setOnClickListener((View v) -> {
            Toast.makeText(getActivity(), "Saving changes...", Toast.LENGTH_SHORT).show();
        });

        mCameraButton = root.findViewById(R.id.camera);
        mCameraButton.setOnClickListener((View v) -> {
           // Toast.makeText(getActivity(), "Saving changes...", Toast.LENGTH_SHORT).show();
            //dispatchTakePictureIntent();
        });

        mPhotoView = root.findViewById(R.id.image_profile);

        checkForPhoto(container);

        imageWidth = mPhotoView.getMeasuredWidth(); imageHeight = mPhotoView.getMeasuredWidth();

        return root;
    }

    private void checkForPhoto(ViewGroup container){
        if (mContact.getCurrentPhotoPath().equals("") && mContact.getAvatar() == -1){
            File files = new File(Environment.getExternalStorageDirectory().toString());
            for (File img : files.listFiles()){
                if (img.getAbsolutePath().equals(mContact.getCurrentPhotoPath())){
                    mPhotoFile = img;
                    updatePhotoView();
                    break;
                }
            }
           Drawable drawableContact = container.getResources().getDrawable(mContact.getAvatar());

            mPhotoView.setImageDrawable(drawableContact);


        }
    }



    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            // Create the File where the photo should go
           File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
           mPhotoFile = null;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            updatePhotoView();
            galleryAddPic();
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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mPhotoFile.getAbsolutePath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public static ContactFragment newInstance(int idContact) {
        Bundle args = new Bundle();
        args.putInt(CONTACT_KEY_ID , idContact);
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
