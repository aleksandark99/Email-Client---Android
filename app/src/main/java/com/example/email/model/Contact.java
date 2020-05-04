package com.example.email.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Contact implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("photoPath")
    @Expose
    private String photoPath;

    @SerializedName("note")
    @Expose
    private String note;


    public Contact(){
        this.firstName = "";
        this.lastName = "";
        this.displayName = "";
        this.photoPath = "";
        this.email = "";
        this.note = "";
    }

    public Contact(Integer id, String firstName, String lastName, String displayName, String email, String photoPath, String note) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.email = email;
        this.photoPath = photoPath;
        this.note = note;
    }

    protected Contact(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        displayName = in.readString();
        email = in.readString();
        photoPath = in.readString();
        note = in.readString();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhotoFilename() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Log.i("timestampe", timeStamp);
        return "IMG_" + getId() + "_" + timeStamp + ".jpg";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        //dest.writeStringArray(new String[]{this.id.toString(), this.firstName, this.lastName, this.email, this.photoPath, this.note});

        dest.writeInt(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.displayName);
        dest.writeString(this.email);
        dest.writeString(this.photoPath);
        dest.writeString(this.note);
    }

    public static final Parcelable.Creator<Contact> CREATOR= new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
             // TODO Auto-generated method stub
            return new Contact(source);  //using parcelable constructor
        }

        @Override
        public Contact[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Contact[size];
        }

    };

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
