package com.example.email.utility;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.email.R;
import com.example.email.model.Tag;
import com.example.email.repository.Repository;

import java.util.ArrayList;

public class Helper {

    public static boolean DoesItContainString(ArrayList<String> s,String contains){
        for (String ss:s
        ) {
            if(ss.toLowerCase().contains(contains.toLowerCase())){
                return true;
            }

        }
        return false;
    };

    public static boolean DoesItContainTag(ArrayList<Tag> s, String contains){
        for (Tag ss:s
        ) {
            if(ss.getTagName().toLowerCase().contains(contains.toLowerCase())){
                return true;
            }


        }
        return false;
    };

    public static void displayImageIntoImageView(String imageFilePath, ImageView imageView, Context context){

        Glide.with(context)
                .load(imageFilePath)
                .apply(new RequestOptions().centerCrop())
                .error(R.drawable.ic_person_black_24dp)
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

    public static int getActiveAccountId(){
        if(Repository.activeAccount!=null){
            return Repository.activeAccount.getId();
        }else{
            return 0;
        }
    }
}
