package com.example.email.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.example.email.R;
import com.example.email.activities.ContactActivity;
import com.example.email.model.Contact;
import com.example.email.repository.Repository;
import com.example.email.utility.Helper;
import com.example.email.utility.PictureUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>  {

    private ArrayList<Contact> mContacts;
    private Context mContext;
    private Contact currentContact;


    public ContactsAdapter(ArrayList<Contact> mContacts, Context mContext){

        Log.i("TAG: ", "POZVAN ContactsAdapter konstruktor");
        this.mContacts = mContacts;
        this.mContext = mContext;

    }

    //Provide a reference to the views used in the recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private LinearLayout mLinearLayout;
        private ImageView imageView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            mLinearLayout = cardView.findViewById(R.id.linear_layout);
            imageView = cardView.findViewById(R.id.image_profile);
            Log.i("TAG: ", "POZVAN ViewHolder konstruktor");
        }

    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Create a new view
        Log.i("TAG: ", "POZVANA onCreateViewHolder iz ViewHoldera");
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card_item, parent, false);
        //ImageView imageView = cv.findViewById(R.id.image_profile);


        return new ViewHolder(cv);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //Set the values inside the given view
        Log.i("TAG: ", "POZVANA onBindViewHolder()");
        CardView cardView = holder.cardView;

        currentContact = mContacts.get(position);
        //Log.i("Tag objekat: ", currentContact.toString());
        //Log.i("pozicija: ", String.valueOf(position));
       // ImageView imageView = (ImageView)cardView.findViewById(R.id.image_profile);


        if (currentContact.getCurrentPhotoPath() != null) {
            Log.i("USAO U", "IF");

            Log.i("Photo path: ", currentContact.getCurrentPhotoPath());
            //width = getSizeOfView(imageView, "width");
            //height = getSizeOfView(imageView, "height");
            //Log.i("WIDTH",String.valueOf(imageView.getWidth()));
            //Log.i("HEIGHT",String.valueOf(imageView.getHeight()));

            //prvi komit na gallery_brnchu
            //Bitmap bitmap = PictureUtils.getScaledBitmap(currentContact.getCurrentPhotoPath(), 120   , 120);

            //imageView.setImageBitmap(bitmap);

            Helper.displayImageIntoImageView(currentContact.getCurrentPhotoPath(), holder.imageView, mContext);
            /*Glide.with(mContext)
                    .load(currentContact.getCurrentPhotoPath())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.imageView);*/



        } else {
                //Drawable drawable = AppCompatResources.getDrawable(mContext, R.drawable.dummy_contact_photo);
                Drawable drawable = cardView.getResources().getDrawable(currentContact.getAvatar());
                holder.imageView.setImageDrawable(drawable);
                //imageView.setImageResource(R.drawable.dummy_contact_photo);
                //imageView.setImageResource(R.drawable.ic_left_arrow_blue);

        } //kom na photo_cam_branchu


        holder.imageView.setContentDescription(mContacts.get(position).getFirstname() + " " + mContacts.get(position).getLastname());
        TextView textView = (TextView)cardView.findViewById(R.id.contact_name);
        textView.setText(mContacts.get(position).getFirstname() + " " + mContacts.get(position).getLastname());

        LinearLayout ll = cardView.findViewById(R.id.linear_layout);
        int color= ((int)(Math.random()*16777215)) | (0xFF << 24);
        ll.setBackgroundColor(color);


        ll.setOnClickListener((View v) -> {

            Intent intent = ContactActivity.newIntent(mContext, mContacts.get(position).getId());
            mContext.startActivity(intent);

        });

    }
    @Override
    public int getItemCount(){
        //Return the number of items in the data set
        return mContacts.size();
    }



}
