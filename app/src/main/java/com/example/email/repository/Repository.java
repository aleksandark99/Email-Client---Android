package com.example.email.repository;

import android.content.Context;

import com.example.email.R;
import com.example.email.model.Attachment;
import com.example.email.model.Contact;
import com.example.email.model.Message;
import com.example.email.model.items.Tag;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/*Dummy repository Singleton class for storing different objects*/
public class Repository {

    private final int[] avatars = {R.drawable.kotur,R.drawable.dejan, R.drawable.brboric, R.drawable.ziga};
    private final int NUMBER_OF_CONTACTS = avatars.length;

    private static Repository sRepository;
    private ArrayList<Contact> mContacts;

    private ArrayList<Message> messages;



    public static Repository get(Context context) {
        if (sRepository == null) {
            sRepository = new Repository(context);
        }
        return sRepository;
    }

    private Repository(Context context) {
        mContacts = IntStream.rangeClosed(1, NUMBER_OF_CONTACTS)
                .boxed()
                .map(idContact -> new Contact(idContact, "Firstname " + idContact,
                        "Lastname " + idContact,
                        "email@contact" + idContact + ".com",
                        avatars[idContact - 1]
                ))
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Contact> getContacts(){
        return mContacts;
    }

    public Contact findContactById(int idContact){
        return mContacts.stream().filter(contact -> contact.getId() == idContact).findFirst().orElse(null);
    }

    public ArrayList<Message> getMessages(){


        Message m1;
        Message m2;
        Message m3,m4,m5,m6;

        Tag t1;
        Tag t2;
        Tag t3;
        ArrayList<Tag> tags1;
        ArrayList<Tag> tags2;


        messages = new ArrayList<Message>();
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
        m1.setFrom("m1Frasdsdasdadsadsasdaasdom");
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
        m1.setContent("Content za m1");
        m2.setContent("Content za m23");
        m3.setContent("Content za m3");
        m4.setContent("Content za m1");
        m5.setContent("Content za m23");
        m6.setContent("Content za m3");
        ArrayList<String> as1=new ArrayList<String>();
        as1.add("To person1");
        as1.add("To person1a");
        as1.add("To person1b");
        ArrayList<String> as2=new ArrayList<String>();
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
        m1.setFrom("From person1");
        m2.setFrom("From person2");
        m3.setFrom("From person3");
        m4.setFrom("From person4");
        m5.setFrom("From person5");
        m6.setFrom("From person6");

        ArrayList<Attachment> at1=new ArrayList<>();
        ArrayList<Attachment>at2=new ArrayList<>();
        ArrayList<Attachment>at3=new ArrayList<>();
        Attachment a1= new Attachment("attachment1");
        Attachment a2= new Attachment("attachment3");
        Attachment a3= new Attachment("attachment2");
        Attachment a4= new Attachment("at1Slika");
        Attachment a5= new Attachment("pdfFile");
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


        return  messages;
    }

    public boolean DeleteMessage(long id){
        for (Message m: messages
             ) {
            if(m.getId()==id){
                messages.remove(m);
                return true;
            }

        }

        return false;
    }
}
