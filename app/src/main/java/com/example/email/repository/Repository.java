package com.example.email.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.example.email.model.Account;
import com.example.email.model.Attachment;
import com.example.email.model.Contact;
import com.example.email.model.Message;
import com.example.email.model.User;
import com.example.email.model.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;



/*Dummy repository Singleton class for storing different objects*/


public class Repository {

    public static User loggedUser = null;
    public static String jwt = null;
    public static Account activeAccount = null;
    public static boolean ascending=true;
    public static SharedPreferences sharedPreferences = null;



    private static Repository sRepository;

    private ArrayList<Contact> mContacts;

    private ArrayList<Message> messages;
    private ArrayList<Tag> tags;


    public static Repository get(Context context) {
        if (sRepository == null) {
            sRepository = new Repository(context);
        }
        return sRepository;
    }

    private Repository(Context context) {
        mContacts = new ArrayList<Contact>();
        messages = new ArrayList<Message>();
        tags = new ArrayList<Tag>();
        setStaticTags();
    }


    public File getPhotoFile(Contact contact, Context context) {
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }
        File f = new File(externalFilesDir, contact.getPhotoFilename());
        //Log.i("IZ rEPOZITORIJA: ", f.getAbsolutePath());
        return f;
    }

    public ArrayList<Contact> getContacts() {
        return mContacts;
    }

    public Contact findContactById(int idContact) {
        return mContacts.stream().filter(contact -> contact.getId() == idContact).findFirst().orElse(null);
    }

    public Contact findContactByEmail(String email) {
        return loggedUser.getContacts().stream().filter(contact -> contact.getEmail().toLowerCase().equals(email.toLowerCase())).findFirst().orElse(null);
    }

    //tag methods
    public ArrayList<Tag> getMyTags() {

        return tags;
    }

    public void addTag(String tagname) {
        Tag t = new Tag();
        t.setTagName(tagname);
        tags.add(t);
    }

    public void editTag(String tagname, String newTagname) {
        for (Tag t : tags
        ) {
            if (t.getTagName().toLowerCase().equals(tagname.toLowerCase())) {
                t.setTagName(newTagname);
            }

        }
    }

    public void removeTag(String s) {
        for (Tag tag : tags
        ) {
            if (tag.getTagName().toLowerCase().equals(s.toLowerCase())) {
                tags.remove(tag);
                break;
            }

        }
    }


    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> m){
        this.messages=m;
    }

    public boolean DeleteMessage(long id) {
        for (Message m : messages
        ) {
            if (m.getId() == id) {
                messages.remove(m);
                return true;
            }

        }

        return false;
    }

    public int newId() {
        if (mContacts.size() > 0) {
            return mContacts.stream().max(Comparator.comparing(Contact::getId)).get().getId() + 1;
        }
        return 0;
    }



    private void setStaticTags() {
        Tag t1;
        Tag t2;
        Tag t3;
        t1 = new Tag(1, "t1433333332");
        t2 = new Tag(2, "t2422434242432432");
        t3 = new Tag(3, "t342432432424342");

        //tags = new ArrayList<Tag>();
        tags.add(t1);
        tags.add(t2);
        tags.add(t3);
    }
    public static boolean loggedUserHaveAccount(){
        return !loggedUser.getAccounts().isEmpty();
    }
//setActiveAccountForLoginActivity
    public static SharedPreferences getSharedPreferences(Context context){
        if (sharedPreferences == null) sharedPreferences = context.getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void setActiveAccountForLoginActivity(int idOfLastUsedAccount){
        activeAccount = loggedUser.getAccounts()
                                   .stream().filter(acc -> acc.getId() == idOfLastUsedAccount)
                                   .findFirst().orElse(null);
    }

    public static void setNewActiveAccount(String email){
        activeAccount =  loggedUser.getAccounts().stream()
                                       .filter(acc -> acc.getUsername().equals(email))
                                       .findFirst().orElse(null);
    }

    public static void removeAccountById(int idRemove){
        for (Account a : loggedUser.getAccounts()){
            if (a.getId() == idRemove) loggedUser.getAccounts().remove(a); return;
        }
    }

    public static Tag findTagById(int id){
        return loggedUser.getTags().stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public static Integer findIdOfSendingAccoount(String email){
        return loggedUser.getAccounts()
                .stream()
                .filter(acc -> acc.getUsername().equals(email))
                .findFirst().map(acc-> acc.getId())
                .orElse(-1);
    }
}
