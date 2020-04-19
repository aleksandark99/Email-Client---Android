package com.example.email.repository;

import android.content.Context;

import com.example.email.R;
import com.example.email.model.Contact;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/*Dummy repository Singleton class for storing different objects*/
public class Repository {

    private final int[] avatars = {R.drawable.kotur,R.drawable.dejan, R.drawable.brboric, R.drawable.ziga};
    private final int NUMBER_OF_CONTACTS = avatars.length;

    private static Repository sRepository;
    private ArrayList<Contact> mContacts;


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

}
