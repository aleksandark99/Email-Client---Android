package com.example.email.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.email.R;
import com.example.email.model.Contact;
import com.example.email.repository.Repository;

public class ContactFragment extends Fragment {

    private static final String CONTACT_KEY_ID = "ftn.sit.emailme.fragments.crime_id";
    private Contact mContact;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int contactId = getArguments().getInt(CONTACT_KEY_ID);
        mContact = Repository.get(getActivity()).findContactById(contactId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_contact, container, false);

        ImageView imageView = root.findViewById(R.id.image_profile);
        if (mContact.getAvatar() != -1){
            Drawable drawableContact = container.getResources().getDrawable(mContact.getAvatar());
            imageView.setImageDrawable(drawableContact);
        } else {
            //set dummy image..
        }


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

        return root;
    }

    public static ContactFragment newInstance(int idContact) {
        Bundle args = new Bundle();
        args.putInt(CONTACT_KEY_ID , idContact);
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
