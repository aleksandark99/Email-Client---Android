package com.example.email.model;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Contact {


    private int id, mAvatar;
    private String mFirstname, mLastname, mEmail;

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


    public File getPhotoFile(Contact contact, Context context) {
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, contact.getPhotoFilename());
    }

    public String getPhotoFilename() {
        return "IMG_" + getId() + "_" + getEmail() + "_" + ".jpg";
    }

}
