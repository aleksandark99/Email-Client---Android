package com.example.email.utility;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
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
                .error(R.drawable.dummy_contact_photo)
                .override(imageView.getWidth(), imageView.getHeight())
                .into(imageView);
    }

    public static String getPicturePath(Uri selectedImageUri, Context context){


        // timer = new Thread(() -> {
           // try {

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                //Uri imageUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = context.getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                return picturePath;

           // } catch (InterruptedException ie) {ie.printStackTrace();} catch (Exception e){e.printStackTrace();}
        //});
       // timer.start();




    }

}
