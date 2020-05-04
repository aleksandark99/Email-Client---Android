package com.example.email.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.activities.ContactActivity;
import com.example.email.activities.ContactsActivity;
import com.example.email.model.Contact;
import com.example.email.retrofit.contacts.ContactService;
import com.example.email.retrofit.contacts.RetrofitContactClient;
import com.example.email.utility.Helper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>  {

    private ArrayList<Contact> mContacts;
    private Context mContext;
    private Contact currentContact;

    public ContactsAdapter(Context mContext){

        Log.i("TAG: ", "POZVAN ContactsAdapter konstruktor");

        this.mContext = mContext;
    }

    public void setData(ArrayList<Contact> contacts){
        this.mContacts = contacts;
        notifyDataSetChanged();
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

        return new ViewHolder(cv);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //Set the values inside the given view
        Log.i("TAG: ", "POZVANA onBindViewHolder()");
        CardView cardView = holder.cardView;

        currentContact = mContacts.get(position);
        if (currentContact.getPhotoPath() != null) {

            Helper.displayImageIntoImageView(currentContact.getPhotoPath(), holder.imageView, mContext);

        } else {

            Drawable drawable = cardView.getResources().getDrawable(R.drawable.dummy_contact_photo);
            holder.imageView.setImageDrawable(drawable);

        } //kom na photo_cam_branchu

        holder.imageView.setContentDescription(currentContact.getFirstName() + " " + currentContact.getLastName());
        TextView textView = (TextView)cardView.findViewById(R.id.contact_display_name);
        textView.setText(currentContact.getDisplayName());

        LinearLayout ll = cardView.findViewById(R.id.linear_layout);
        int color= ((int)(Math.random()*16777215)) | (0xFF << 24);
        ll.setBackgroundColor(color);

        ll.setOnClickListener((View v) -> {

            Log.i("string rep iz adapter", currentContact.toString());

            Intent intent = ContactActivity.newIntent(mContext,  position, mContacts);
            //Intent intent = new Intent(mContext, ContactActivity.class);
            //intent.putExtra("contact", currentContact);
            mContext.startActivity(intent);

        });
    }

    @Override
    public int getItemCount(){
        //Return the number of items in the data set
        return mContacts.size();
    }

}
