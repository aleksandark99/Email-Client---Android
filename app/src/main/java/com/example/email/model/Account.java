package com.example.email.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("smtpAddress")
    @Expose
    private String smtpAddress;

    @SerializedName("smtpPort")
    @Expose
    private int smtpPort;

    @SerializedName("inServerType")
    @Expose
    private int inServerType;

    @SerializedName("inServerAddress")
    @Expose
    private String inServerAddress;

    @SerializedName("inServerPort")
    @Expose
    private int inServerPort;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("displayName")
    @Expose
    private String displayName;

    @SerializedName("authentication")
    @Expose
    private boolean authentication;


    public Account(String smtpAddress, int smtpPort, int inServerType, String inServerAddress,  int inServerPort, String username, boolean authentication, String password , String displayName){
        this.smtpAddress = smtpAddress;
        this.smtpPort = smtpPort;
        this.inServerType = inServerType;
        this.inServerAddress = inServerAddress;
        this.inServerPort = inServerPort;
        this.authentication = authentication;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
    }

    public Account(){

    }




    protected Account(Parcel in) {
        id = in.readInt();
        smtpAddress = in.readString();
        smtpPort = in.readInt();
        inServerType = in.readInt();
        inServerAddress = in.readString();
        inServerPort = in.readInt();
        authentication = in.readInt() == 1;
        username = in.readString();
        password = in.readString();
        displayName = in.readString();

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.smtpAddress);
        dest.writeInt(this.smtpPort);
        dest.writeInt(this.inServerType);
        dest.writeString(this.inServerAddress);
        dest.writeInt(this.inServerPort);
        dest.writeInt(this.isAuthentication() ? 1 :0);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.displayName);
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

    public String getSmtpAddress() {
        return smtpAddress;
    }

    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public int getInServerType() {
        return inServerType;
    }

    public void setInServerType(int inServerType) {
        this.inServerType = inServerType;
    }

    public String getInServerAddress() {
        return inServerAddress;
    }

    public void setInServerAddress(String inServerAddress) {
        this.inServerAddress = inServerAddress;
    }

    public int getInServerPort() {
        return inServerPort;
    }

    public void setInServerPort(int inServerPort) {
        this.inServerPort = inServerPort;
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public static Creator<Account> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", smtpAddress='" + smtpAddress + '\'' +
                ", smtpPort=" + smtpPort +
                ", inServerType=" + inServerType +
                ", inServerAddress='" + inServerAddress + '\'' +
                ", inServerPort=" + inServerPort +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                ", authentication=" + authentication +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Account) {
            if( ((Account) obj).getId() == this.getId()   ){
                return true;
            } else return false;
        } else return false;
    }

    @Override
    public int hashCode() {
        return this.getId();
    }
}
