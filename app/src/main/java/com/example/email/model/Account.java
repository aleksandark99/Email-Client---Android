package com.example.email.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {

    private int id;
    private String email;

    public Account(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public Account() {
    }

    protected Account(Parcel in) {
        id = in.readInt();
        email = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        //dest.writeStringArray(new String[]{this.id.toString(), this.firstName, this.lastName, this.email, this.photoPath, this.note});

        dest.writeInt(this.id);
        dest.writeString(this.email);

    }

    public static final Parcelable.Creator<Account> CREATOR= new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new Account(source);  //using parcelable constructor
        }

        @Override
        public Account[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Account[size];
        }

    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
