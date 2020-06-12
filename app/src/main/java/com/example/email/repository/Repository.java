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
        setStaticMessages();

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


    private void setStaticMessages() {
        Message m1;
        Message m2;
        Message m3, m4, m5, m6;

        Tag t1;
        Tag t2;
        Tag t3;
        ArrayList<Tag> tags1;
        ArrayList<Tag> tags2;


        //   messages = new ArrayList<Message>();
        m1 = new Message();
        m2 = new Message();
        m3 = new Message();
        m4 = new Message();
        m5 = new Message();
        m6 = new Message();

        t1 = new Tag(1, "t1433333332");
        t2 = new Tag(2, "t2422434242432432");
        t3 = new Tag(3, "t342432432424342");

        t1.setTagName("tag1name");
        t2.setTagName("tag2name");
        t3.setTagName("sssss");


        tags1 = new ArrayList<Tag>();
        tags2 = new ArrayList<Tag>();
        /////
        m1.setFrom("a");
        m2.setFrom("m2From");
        m3.setFrom("m2From");
        m1.setSubject("m1Subject");
        m2.setSubject("m2Subject");
        m3.setSubject("m3Subject");

        m1.setUnread(true);
        m2.setUnread(false);
        m3.setUnread(true);


        tags1.add(t1);
        tags1.add(t3);
        tags2.add(t1);
        tags2.add(t2);
        tags2.add(t3);
        m1.setTags(tags1);
        m2.setTags(tags2);
        m3.setTags(tags1);
        m4.setTags(tags2);
        m5.setTags(tags2);
        m6.setTags(tags1);
        m1.setContent("Content za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lorContent za m1 lor");
        m2.setContent("Content za m23");
        m3.setContent("Content za m3");
        m4.setContent("Content za m1");
        m5.setContent("Content za m23");
        m6.setContent("Content za m3");
        ArrayList<String> as1 = new ArrayList<String>();
        as1.add("To person1");
        as1.add("To person1a");
        as1.add("To person1b");
        ArrayList<String> as2 = new ArrayList<String>();
        as2.add("To xyz");
        as2.add("To person2a");
        m1.setTo(as2);
        m1.setCc(as2);
        m2.setTo(as1);
        m2.setCc(as2);
        m3.setTo(as1);
        m3.setCc(as2);
        m4.setTo(as1);
        m4.setCc(as2);
        m5.setTo(as1);
        m5.setCc(as2);
        m6.setTo(as1);
        m6.setCc(as2);
        m1.setFrom("a");
        m2.setFrom("From person2");
        m3.setFrom("From person3");
        m4.setFrom("From person4");
        m5.setFrom("From person5");
        m6.setFrom("From person6");

        ArrayList<Attachment> at1 = new ArrayList<>();
        ArrayList<Attachment> at2 = new ArrayList<>();
        ArrayList<Attachment> at3 = new ArrayList<>();
        Attachment a1 = new Attachment("attachment1");
        Attachment a2 = new Attachment("attachment3");
        Attachment a3 = new Attachment("attachment2");
        Attachment a4 = new Attachment("at1Slika");
        Attachment a5 = new Attachment("pdfFile");
        at1.add(a1);
        at1.add(a5);

        at2.add(a2);

        at3.add(a2);
        at3.add(a4);
        at3.add(a3);

        m1.setAttachments(at1);
        m5.setAttachments(at3);
        m3.setAttachments(at2);
        m1.setId(1);
        m2.setId(2);
        m3.setId(3);
        m4.setId(4);
        m5.setId(5);
        m6.setId(6);

        messages.add(m4);
        messages.add(m5);
        messages.add(m6);
        messages.add(m1);
        messages.add(m2);
        messages.add(m3);
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
}
