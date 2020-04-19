package com.example.email.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.example.email.model.Contact;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>  {

    private ArrayList<Contact> mContacts;
    private Context mContext;

    public ContactsAdapter(ArrayList<Contact> mContacts, Context mContext){
        this.mContacts = mContacts;
        this.mContext = mContext;
    }

    //Provide a reference to the views used in the recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private LinearLayout mLinearLayout;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            mLinearLayout = cardView.findViewById(R.id.linear_layout);
        }

    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Create a new view
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card_item, parent, false);
        return new ViewHolder(cv);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //Set the values inside the given view
        CardView cardView = holder.cardView;

        ImageView imageView = (ImageView)cardView.findViewById(R.id.image_profile);
        Drawable drawable = cardView.getResources().getDrawable(mContacts.get(position).getAvatar());
        imageView.setImageDrawable(drawable);

        imageView.setContentDescription(mContacts.get(position).getFirstname() + " " + mContacts.get(position).getLastname());
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
