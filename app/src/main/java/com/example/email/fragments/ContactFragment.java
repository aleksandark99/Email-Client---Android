package com.example.email.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
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

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactFragment extends Fragment {

    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_GALLERY_PHOTO = 3;

    private static final String CONTACT_KEY = "com.example.email.fragments.contact";

    private EditText editTextBoxName, editTextBoxLastname, editTextBoxDisplayName, editTextBoxEmail, editTextBoxNotes;
    private String filePath, tempFilePath;
    private String first_Name,last_Name, display_Name, Email, notes;

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

        mContact = getArguments().getParcelable(CONTACT_KEY);

        if(savedInstanceState == null){

            filePath = mContact.getPhotoPath();
            first_Name = mContact.getFirstName(); last_Name = mContact.getLastName(); display_Name = mContact.getDisplayName(); Email = mContact.getEmail(); notes = mContact.getNote();
            
        } else {
            String name = savedInstanceState.getString("firstName"); //editTextBoxName.setText(name);
            String lastName = savedInstanceState.getString("lastName"); //editTextBoxLastname.setText(lastName);
            String email = savedInstanceState.getString("email"); //editTextBoxEmail.setText(email);
            String displayName = savedInstanceState.getString("displayName");
            String note = savedInstanceState.getString("notes");

            first_Name = name; last_Name = lastName; display_Name = displayName; Email = email; notes = note;

            boolean photoTaken = savedInstanceState.getBoolean("photoTaken"); this.photoTaken = photoTaken;

            if (this.photoTaken) filePath = savedInstanceState.getString("photoPath");
            else filePath = mContact.getPhotoPath();

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
        editTextBoxDisplayName = root.findViewById(R.id.display_name_input_box_edit_box); editTextBoxDisplayName.setText(display_Name);
        editTextBoxEmail = root.findViewById(R.id.email_input_box_edit_box); editTextBoxEmail.setText(Email);
        editTextBoxNotes = root.findViewById(R.id.notes_input_box_edit_box); editTextBoxNotes.setText(notes);

        int color = ((int)(Math.random()*16777215)) | (0xFF << 24);

        CardView cardViewTop = root.findViewById(R.id.card_view_top);
        cardViewTop.setCardBackgroundColor(color);

        CardView cardViewBottom = root.findViewById(R.id.card_view_bottom);
        cardViewBottom.setCardBackgroundColor(color);

        LinearLayout saveChanges = root.findViewById(R.id.linear_layout_bottom);
        saveChanges.setOnClickListener((View v) -> {

            mContact.setFirstName(editTextBoxName.getText().toString());
            mContact.setLastName(editTextBoxLastname.getText().toString());
            mContact.setDisplayName(editTextBoxDisplayName.getText().toString());
            mContact.setEmail(editTextBoxEmail.getText().toString());
            mContact.setNote(editTextBoxNotes.getText().toString());

            if(photoTaken)  mContact.setPhotoPath(filePath);

            updateContact();

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

        final Intent pickImage = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
        pickImage.setType("image/*");

        mGalleryButton.setOnClickListener((View v) -> {
            startActivityForResult(pickImage, REQUEST_GALLERY_PHOTO);
        });

        Helper.displayImageIntoImageView(filePath, mPhotoView, getActivity());
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("firstName", editTextBoxName.getText().toString());
        savedInstanceState.putString("lastName", editTextBoxLastname.getText().toString());
        savedInstanceState.putString("displayName", editTextBoxDisplayName.getText().toString());
        savedInstanceState.putString("email", editTextBoxEmail.getText().toString());
        savedInstanceState.putString("notes", editTextBoxNotes.getText().toString());

        savedInstanceState.putBoolean("photoTaken", photoTaken);

        if (photoTaken)  savedInstanceState.putString("photoPath", filePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            photoTaken = true;
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

    private void updateContact(){
        //final ArrayList<Contact> contacts;
        Retrofit mRetrofit = RetrofitContactClient.getRetrofitInstance();
        ContactService mContactService = mRetrofit.create(ContactService.class);

        Call<Void> call = mContactService.updateContact(mContact);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (!response.isSuccessful()){
                    Log.i("GRESKA U ContactFragment", String.valueOf(response.code()));
                }
                Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Changes NOT saved", Toast.LENGTH_SHORT).show();
                Log.i("FAILURE", t.toString());
            }
        });

    }

    public static ContactFragment newInstance(Contact contact) {
        Bundle args = new Bundle();
        args.putParcelable(CONTACT_KEY, contact);
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
