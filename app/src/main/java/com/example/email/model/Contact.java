package com.example.email.model;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Contact {


    private int id, mAvatar;
    private String mFirstname;



    private String mLastname;
    private String mEmail;
    private String mCurrentPhotoPath;

    public Contact(int id, String firstname, String lastname, String email, int mAvatar) {
        this.id = id;
        mFirstname = firstname;
        mLastname = lastname;
        mEmail = email;
        this.mAvatar = mAvatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String firstname) {
        mFirstname = firstname;
    }

    public String getLastname() {
        return mLastname;
    }

    public void setLastname(String lastname) {
        mLastname = lastname;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public int getAvatar() {
        return mAvatar;
    }

    public void setAvatar(int avatar) {
        mAvatar = avatar;
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        mCurrentPhotoPath = currentPhotoPath;
    }


    public String getPhotoFilename()  {
        //return "IMG_" + getId() + "_" + getEmail() + "_" + ".jpg";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + ".jpg";
        mCurrentPhotoPath = imageFileName;
        return  imageFileName;
    }

}
