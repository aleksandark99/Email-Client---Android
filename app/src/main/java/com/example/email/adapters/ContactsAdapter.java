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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.activities.ContactActivity;
import com.example.email.model.Contact;
import com.example.email.repository.Repository;
import com.example.email.utility.PictureUtils;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>  {

    private ArrayList<Contact> mContacts;
    private Context mContext;
    private Contact currentContact;
    private int width, height;

    public ContactsAdapter(ArrayList<Contact> mContacts, Context mContext){
        this.mContacts = mContacts;
        this.mContext = mContext;
        Log.i("mContacts size: ", String.valueOf(mContacts.size()));
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
        currentContact = mContacts.get(position);

        ImageView imageView = (ImageView)cardView.findViewById(R.id.image_profile);
        //Drawable drawable = cardView.getResources().getDrawable(mContacts.get(position).getAvatar());
        //imageView.setImageDrawable(drawable);

        //Contact contact = mContacts.get(position);
        Repository repository = Repository.get(mContext);

        ViewTreeObserver observer = imageView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /* Interface definition for a callback to be invoked when the global layout state
             or the visibility of views within the view tree changes.
             Therefore it's a good idea to de-register the observer after the first "pass" happens.
             It would be interesting, though, to know why we see the following logged lines TWICE.


            FROM THE BOOK
            In this chapter, you had to use a crude estimate of the size you should scale down to. This is not ideal,
            but it works and is quick to implement.
            With the out-of-the-box APIs you can use a tool called ViewTreeObserver. ViewTreeObserver is an
            object that you can get from any view in your Activity’s hierarchy:
            ViewTreeObserver observer = mImageView.getViewTreeObserver();
            You can register a variety of listeners on a ViewTreeObserver, including OnGlobalLayoutListener.
            This listener fires an event whenever a layout pass happens.
            For this challenge, adjust your code so that it uses the dimensions of mPhotoView when they are valid,
            and waits until a layout pass before initially calling updatePhotoView().
             */
            @Override
            public void onGlobalLayout() {
                width = imageView.getMeasuredWidth();
                height = imageView.getMeasuredHeight();
                Log.d("TAG", "imageWidth: " + width);
                Log.d("TAG", "imageHeight: " + height);



                if (currentContact.getCurrentPhotoPath() != null) {
                    Log.i("WIDTH",String.valueOf(imageView.getWidth()));
                    Log.i("HEIGHT",String.valueOf(imageView.getHeight()));
                    Log.i("Photo path: ", currentContact.getCurrentPhotoPath());

                    Bitmap bitmap = PictureUtils.getScaledBitmap(currentContact.getCurrentPhotoPath(), width, height);
                    Log.i("iz adaptera ",currentContact.getCurrentPhotoPath());
                    imageView.setImageBitmap(bitmap);
                } else {
                    if (currentContact.getAvatar() != 0){
                        Drawable drawable = cardView.getResources().getDrawable(mContacts.get(position).getAvatar());
                        imageView.setImageDrawable(drawable);
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });





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
