package com.example.email.repository;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.email.R;
import com.example.email.model.Contact;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
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
        mContacts = new ArrayList<Contact>();
       /* mContacts = IntStream.rangeClosed(1, NUMBER_OF_CONTACTS)
                .boxed()
                .map(idContact -> new Contact(idContact, "Firstname " + idContact,
                        "Lastname " + idContact,
                        "email@contact" + idContact + ".com",
                        avatars[idContact - 1]


                ))
                .collect(Collectors.toCollection(ArrayList::new));*/

    }

    public File getPhotoFile(Contact contact, Context context) {
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }
        File f = new File(externalFilesDir, contact.getPhotoFilename());
        Log.i("IZ rEPOZITORIJA: ", f.getAbsolutePath());
        return f;
    }

    public ArrayList<Contact> getContacts(){
        return mContacts;
    }

    public Contact findContactById(int idContact){
        return mContacts.stream().filter(contact -> contact.getId() == idContact).findFirst().orElse(null);
    }

    public int newId(){
        if (mContacts.size() > 0){
            return mContacts.stream().max(Comparator.comparing(Contact::getId)).get().getId() + 1;
        }
        return 0;
    }

}
