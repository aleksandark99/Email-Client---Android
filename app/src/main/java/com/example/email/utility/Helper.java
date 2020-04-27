package com.example.email.utility;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.email.R;

public class Helper {


    public static void displayImageIntoImageView(String imageFilePath, ImageView imageView, Context context){

        Glide.with(context)
                .load(imageFilePath)
                .apply(new RequestOptions().centerCrop())
                .override(imageView.getWidth(), imageView.getHeight())
                .into(imageView);
    }

}
